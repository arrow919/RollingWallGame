package com.studios927.rollingwallgame.gameworkings;

import java.util.Random;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Circle {
    private static Random r = new Random();
    public static int radius = 15;
    private double cx;
    private double cy;
    private Paint paint;

    public Circle(int x, int y) {
        cx = x;
        cy = y;
        paint = new Paint();
        paint.setColor(Color.rgb(r.nextInt(150) + 50, r.nextInt(150) + 50, r.nextInt(150) + 50));
    }

    public void draw(Canvas canvas, double xOffset, double yOffset) {
        canvas.drawCircle(Math.round(cx - xOffset + UpdateThread.getCenterX()), Math.round(cy - yOffset + UpdateThread.getCenterY()), radius, paint);
    }

    public boolean hit(double x, double y) {
        double xDist = cx - x;
        double yDist = cy - y;
        double distance = Math.sqrt((xDist * xDist) + (yDist * yDist));
        return distance < radius * 2;
    }

    public double getX() {
        return cx;
    }

    public double getY() {
        return cy;
    }

    public void setX(double newX) {
        cx = newX;
    }

    public void setY(double newY) {
        cy = newY;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }
}
