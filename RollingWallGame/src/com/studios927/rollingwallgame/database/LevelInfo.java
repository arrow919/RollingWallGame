package com.studios927.rollingwallgame.database;

import com.studios927.rollingwallgame.ui.LevelGridView;

import android.content.ContentValues;

public class LevelInfo {
    private int world;
    private int level;
    private long time;
    private long id;
    private int completed;

    public LevelInfo(int world, int level, long time, int completed) {
        this.world = world;
        this.level = level;
        this.time = time;
        id = toId(world, level);
    }

    public void setTime(long time) {
        this.time = time;

    }

    public long getTime() {
        return time;
    }

    public int getWorld() {
        return world;
    }

    public int getLevel() {
        return level;
    }

    public long getId() {
        return id;
    }

    public static int toId(int world, int level) {
        return world * LevelGridView.levelsPerColumn * LevelGridView.levelsPerRow + level;
    }

    public int getCompletedInt() {
        return completed;
    }

    public boolean getFinishedBool() {
        return completed != 0;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_WORLD, world);
        values.put(MySQLiteHelper.COLUMN_COMPLETE, completed);
        values.put(MySQLiteHelper.COLUMN_ID, id);
        values.put(MySQLiteHelper.COLUMN_LEVEL, level);
        values.put(MySQLiteHelper.COLUMN_TIME, time);
        return values;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }
}
