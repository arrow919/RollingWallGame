package com.studios927.rollingwallgame;

import java.util.logging.Logger;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.util.FloatMath;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.studios927.rollingwallgame.ui.GameSurfaceView;


public class UpdateThread extends Thread {
    private int targetFPS = 50;
    private long runTime = 0;
    private long totalUpdates;
    private long lastTime;
    private boolean alive, running;
    private SurfaceHolder holder;
    private Paint ball = new Paint(Color.BLUE);
    private static int centerX;
    private static int centerY;
    private Paint black;
    private float[] inR = new float[16];
    private float[] outR = new float[16];
    private float[] I = new float[16];
    private float[] gravData = new float[3];
    private float[] magData = new float[3];
    private float[] orientData = new float[3];
    private static int orientation;
    private Handler handler;

    public UpdateThread(SurfaceHolder surfaceHolder) {
        black = new Paint(Color.BLACK);
        black.setTextSize(24);
        holder = surfaceHolder;
    }

    public void setCenter(int x, int y) {
        centerX = x;
        centerY = y;
    }

    @Override
    public void run() {
        onResume();
        Canvas canvas;
        while (alive) {
            while (!running) {
                try {
                    synchronized (this) {
                        wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            FPSUpdate();
            playerUpdate();
            canvas = null;
            try {
                canvas = holder.lockCanvas();
                if (canvas != null) {
                    canvas.drawColor(Color.WHITE);
                    canvas.drawCircle(centerX, centerY, Circle.radius, ball);
                    renderTime(canvas);
                    synchronized (holder) {
                        GameSurfaceView.getLevel().onDraw(canvas);
                    }
                }
            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas);
                }
            }
            if (GameSurfaceView.getLevel().isTouchingEdge()) {
                logger.warning("Death detected");
                onWallHit();
            }
            if (GameSurfaceView.getLevel().isTouchingFinish()) {
                logger.warning("Win detected");
                onFinishHit();
            }
        }


    }

    public static void setOrientation(int newOrientation) {
        orientation = newOrientation;
        System.out.println("THIS IS THE ORIENTATION: " + orientation);
    }

    public void onPause() {
        running = false;
    }

    public void onResume() {
        System.out.println("Starting");
        alive = true;
        lastTime = System.currentTimeMillis() - 1;
        synchronized (this) {
            running = true;
            notify();
        }
    }

    public void kill() {
        running = true;
        alive = false;
        synchronized (this) {
            notify();
        }
    }

    int count;

    private void FPSUpdate() {
        totalUpdates += 1;
        long newTime = System.currentTimeMillis();
        runTime += newTime - lastTime;
        int currentFPS = (int) Math.round((1000 * totalUpdates / runTime));
        lastTime = newTime;
        if (currentFPS > targetFPS) {
            try {
                // System.out.println("Sleeping");
                sleep(50);
            } catch (InterruptedException e) {

                e.printStackTrace();
            }
        }

    }

    public void setGravData(float[] data) {
        gravData = data;
    }

    public void setMagData(float[] data) {
        magData = data;
    }

    private void renderTime(Canvas canvas) {
        long time = runTime;
        long seconds = Math.round(runTime / 1000);
        time -= seconds * 1000;
        String hundredths = String.valueOf(Math.round(time / 10));
        hundredths = hundredths.substring(Math.max(0, hundredths.length() - 2), Math.max(0, hundredths.length() - 1));
        canvas.drawText(String.valueOf(seconds) + ":" + hundredths, 15, 20, black);
        // System.out.println(time);
    }

    public static int getCenterX() {
        return centerX;
    }

    public static int getCenterY() {
        return centerY;
    }

    private double normalSpeed = 500d / targetFPS;
    private double xSpeed;
    private double ySpeed;
    private static final Logger logger = Logger.getLogger("UpdateThread");


    private void playerUpdate() {
        if (SensorManager.getRotationMatrix(inR, I, gravData, magData)) {
            if (orientation == Surface.ROTATION_90 || orientation == Surface.ROTATION_270) {
                SensorManager.remapCoordinateSystem(inR, SensorManager.AXIS_Y, SensorManager.AXIS_X, outR);
                SensorManager.getOrientation(outR, orientData);

                xSpeed = normalSpeed * -FloatMath.sin(orientData[2]);
                ySpeed = normalSpeed * -FloatMath.sin(orientData[1]);
            } else {
                SensorManager.getOrientation(inR, orientData);

                xSpeed = normalSpeed * FloatMath.sin(orientData[2]);
                ySpeed = normalSpeed * -FloatMath.sin(orientData[1]);
            }
            // logger.warning("X: " + orientData[2] + ", Y: " + orientData[1]);
            GameSurfaceView.getLevel().setPlayerPos(GameSurfaceView.getLevel().getPlayer().getX() + xSpeed, GameSurfaceView.getLevel().getPlayer().getY()
                    + ySpeed);
        }
    }

    private void onFinishHit() {
        running = false;
        Message message = Message.obtain();
        message.obj = GameMessage.WIN;
        handler.sendMessage(message);
    }

    private void onWallHit() {
        running = false;
        Message message = Message.obtain();
        message.obj = GameMessage.LOSE;
        handler.sendMessage(message);
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public long getRunTime() {
        return runTime;
    }
}
