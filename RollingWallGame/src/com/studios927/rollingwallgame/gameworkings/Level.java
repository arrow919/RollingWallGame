package com.studios927.rollingwallgame.gameworkings;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.studios927.rollingwallgame.database.LevelInfo;

import java.util.ArrayList;

public class Level {
    private Circle[] circles;
    private Rect finish;
    private Circle player;
    private LevelInfo levelInfo;
    private Paint finishPaint;

    public Level(LevelInfo info) {
        circles = new Circle[0];
        finish = new Rect(1000, 1000, 1040, 1040);
        player = new Circle(0, 0);
        levelInfo = info;
        finishPaint = new Paint(Color.BLACK);
    }

    public Level(String parseString, LevelInfo info) {
        parseLevel(parseString);
        levelInfo = info;
        finishPaint = new Paint(Color.BLACK);
    }

    private void parseLevel(String parseString) {
        String[] rawData = parseString.split(";");
        ArrayList<Circle> circles = new ArrayList<Circle>();
        {
            String[] data;
            for (int count = 0; count < rawData.length; count++) {
                data = rawData[count].split(",");
                if (count == 0) {
                    player = new Circle(Integer.valueOf(data[0]), Integer.valueOf(data[1]));
                } else if (count == 1) {
                    finish = new Rect(Integer.valueOf(data[0]) - 20, Integer.valueOf(data[1]) - 20, Integer.valueOf(data[0]) + 20, Integer.valueOf(data[1]) + 20);
                } else {
                    circles.add(new Circle(Integer.valueOf(data[0]), Integer.valueOf(data[1])));
                }
            }

        }
        this.circles = new Circle[circles.size()];
        circles.toArray(this.circles);
    }

    public boolean isTouchingEdge() {

        for (int count = 0; count < circles.length; count++) {
            if (circles[count].hit(player.getX(), player.getY())) {
                return true;
            }
        }
        return false;
    }

    public boolean isTouchingFinish() { return player.getX() + 60 >= finish.right && player.getX() - 60 <= finish.left && player.getY() + 60 >= finish.bottom && player.getY() - 60 <= finish.top;
    }

    public void onDraw(Canvas canvas) {
        for (int count = 0; count < circles.length; count++) {
            circles[count].draw(canvas, player.getX(), player.getY());
        }
        canvas.drawRect(Math.round(finish.left - player.getX() + UpdateThread.getCenterX()), Math.round(finish.top - player.getY() + UpdateThread.getCenterY()), Math.round(finish.right - player.getX() + UpdateThread.getCenterX()), Math.round(finish.bottom - player.getY() + UpdateThread.getCenterY()), finishPaint);
    }

    public void setPlayerPos(double x, double y) {
        player.setX(x);
        player.setY(y);
    }

    public Circle getPlayer() {
        return player;
    }

    public LevelInfo getInfo() {
        return levelInfo;
    }
}
