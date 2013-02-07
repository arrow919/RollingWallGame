package com.studios927.rollingwallgame.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.studios927.rollingwallgame.database.GameDataSource;
import com.studios927.rollingwallgame.gameworkings.Level;
import com.studios927.rollingwallgame.gameworkings.UpdateThread;

public class GameSurfaceView extends SurfaceView implements
		SurfaceHolder.Callback {
	private UpdateThread updateThread;
	private static Level levelData;
	private int world;
	private int level;
	private GameDataSource gameData;
	private final SensorEventListener sl = new SensorEventListener() {
		@Override
		public void onAccuracyChanged(Sensor sensor, int i) {

		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			// Gets the value of the sensor that has been changed
			switch (event.sensor.getType()) {
			case Sensor.TYPE_ACCELEROMETER:
				updateThread.setGravData(event.values.clone());
				break;
			case Sensor.TYPE_MAGNETIC_FIELD:
				updateThread.setMagData(event.values.clone());
				break;
			}

		}
	};
	// Locate the SensorManager using Activity.getSystemService

	private SensorManager sm;

	public GameSurfaceView(Context context, int world, int level) {
		super(context);
		this.world = world;
		this.level = level;
		init();

	}

	private void init() {
		gameData = new GameDataSource(getContext());
		gameData.open();
		loadLevel();
		updateThread = new UpdateThread(getHolder());
		getHolder().addCallback(this);
		// make the GamePanel focusable so it can handle events
		setFocusable(true);
		sm = (SensorManager) getContext().getSystemService(
				Context.SENSOR_SERVICE);
		registerListener();

	}

	public void loadLevel() {
		Logger.getLogger("GameSurfaceView").warning(
				"World Level: " + world + ", " + level);
		AssetManager assetManager = getContext().getAssets();
		InputStream stream = null;
		BufferedReader reader = null;
		try {
			stream = assetManager.open((world) + "-" + (level));
			reader = new BufferedReader(new InputStreamReader(stream));
			levelData = new Level(reader.readLine(), gameData.getLevelInfo(
					world, level));
		} catch (IOException e) {
			levelData = new Level(gameData.getLevelInfo(world, level));
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
		}

	}

	public void killThread() {
		updateThread.kill();
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {

	}

	boolean created = false;

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		if (!created) {
			created = true;
			WindowManager wm = (WindowManager) getContext().getSystemService(
					Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			updateThread.setCenter((int) display.getWidth() / 2,
					(int) display.getHeight() / 2);
			Canvas canvas = arg0.lockCanvas();
			canvas.drawColor(Color.WHITE);
			arg0.unlockCanvasAndPost(canvas);
			updateThread.start();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		
	}

	public void restart(int world, int level) {
		this.world = world;
		this.level = level;
		gameData = new GameDataSource(getContext());
		gameData.open();
		loadLevel();
		boolean menuOrGame = updateThread.menuOrGame;
		int xBoundary = updateThread.xBoundary;
		int yBoundary = updateThread.yBoundary;
		updateThread = new UpdateThread(getHolder());
		updateThread.setMenuOrGame(menuOrGame);
		updateThread.setBoundaries(xBoundary, yBoundary);
		getHolder().addCallback(this);
	}

	private void unregisterListener() {
		sm.unregisterListener(sl);
		registered = false;
	}

	private void registerListener() {
		sm.registerListener(sl, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_GAME);
		sm.registerListener(sl,
				sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
				SensorManager.SENSOR_DELAY_GAME);

		registered = true;
	}

	public void setUpdateThreadHandler(Handler handler) {
		updateThread.setHandler(handler);
	}

	public static Level getLevel() {
		return levelData;
	}

	public long getRunTime() {
		return updateThread.getRunTime();
	}

	public GameDataSource getGameData() {
		return gameData;
	}

	public void setMenuOrGame(boolean value) {
		updateThread.setMenuOrGame(value);
	}

	public void setBoundaries(int x, int y) {
		updateThread.setBoundaries(x, y);
	}

	boolean registered = false;

	public void pause() {
		updateThread.onPause();
		gameData.close();
		if (registered) {
			unregisterListener();
		}
	}

	public void resume() {
		updateThread.onResume();
		gameData.open();
		if (!registered) {
			registerListener();
		}
	}
}
