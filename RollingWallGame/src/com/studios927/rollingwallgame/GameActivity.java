package com.studios927.rollingwallgame;

import java.util.logging.Logger;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;

import com.studios927.rollingwallgame.gameworkings.GameMessage;
import com.studios927.rollingwallgame.gameworkings.UpdateThread;
import com.studios927.rollingwallgame.ui.GameSurfaceView;
import com.studios927.rollingwallgame.ui.LoseDialog;
import com.studios927.rollingwallgame.ui.WinDialog;

public class GameActivity extends Activity {
	private GameSurfaceView gameSurface;
	public static int level;
	public static int world;
	private static Handler handler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		makeFullScreen();
		level = this.getIntent().getExtras().getInt(LevelSelectActivity.LEVEL);
		world = this.getIntent().getExtras().getInt(WorldSelectActivity.WORLD);
		gameSurface = new GameSurfaceView(getApplicationContext(), world, level);
		gameSurface.setUpdateThreadHandler(new DialogHandler());
		setContentView(gameSurface);
		UpdateThread
				.setOrientation(((WindowManager) getSystemService(WINDOW_SERVICE))
						.getDefaultDisplay().getOrientation());

	}

	@Override
	public void onResume() {
		super.onResume();
		gameSurface.resume();
	}

	@Override
	public void onPause() {
		super.onPause();
		gameSurface.pause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		gameSurface.killThread();
	}

	private void makeFullScreen() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	public static void setHandler(Handler newHandler) {
		handler = newHandler;
	}

	private class DialogHandler extends Handler {
		public void handleMessage(Message msg) {
			if (GameMessage.WIN.equals(msg.obj)) {
				if (!GameSurfaceView.getLevel().getInfo().getFinishedBool()
						|| gameSurface.getRunTime() < GameSurfaceView
								.getLevel().getInfo().getTime()) {
					GameSurfaceView.getLevel().getInfo()
							.setTime(gameSurface.getRunTime());
					GameSurfaceView.getLevel().getInfo().setCompleted(1);
					gameSurface.getGameData().addLevelInfo(
							GameSurfaceView.getLevel().getInfo());
				}
				final WinDialog dialog = new WinDialog(GameActivity.this);
				dialog.setHandler(new Handler() {
					@Override
					public void handleMessage(Message msg) {
						if (GameMessage.NEXT.equals(msg.obj)) {
							if (level == 21) {
								dialog.cancel();
								finish();
								Logger.getLogger(GameActivity.class.getName())
										.warning("Not making it here");
								Message message = Message.obtain();
								message.obj = GameMessage.NEXT;
								handler.sendMessage(message);
							} else {
								dialog.cancel();
								level += 1;
								gameSurface = new GameSurfaceView(
										getApplicationContext(), world, level);
								gameSurface
										.setUpdateThreadHandler(new DialogHandler());
								setContentView(gameSurface);
							}
						} else if (GameMessage.BACK.equals(msg.obj)) {
							finish();
						}
					}
				});
				dialog.setTime(gameSurface.getRunTime());
				dialog.show();
			} else if (GameMessage.LOSE.equals(msg.obj)) {
				final LoseDialog dialog = new LoseDialog(GameActivity.this);
				dialog.setHandler(new Handler() {
					@Override
					public void handleMessage(Message msg) {
						if (GameMessage.RETRY.equals(msg.obj)) {
							dialog.cancel();
							gameSurface.killThread();
							gameSurface.restart(world, level);
						} else if (GameMessage.BACK.equals(msg.obj)) {
							dialog.cancel();
							finish();
						}
					}
				});
				dialog.show();
			} else if (GameMessage.PAUSE.equals(msg.obj)) {
				// PauseDialog dialog = new PauseDialog(getContext());
				// dialog.show();
			}
		}
	}

}
