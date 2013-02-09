package com.studios927.rollingwallgame.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class GameDataSource {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;

    public GameDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        database.close();
    }

    public void addLevelInfo(LevelInfo info) {
        if (!database.isOpen()) {
            database = dbHelper.getWritableDatabase();
        }
        database.replace(MySQLiteHelper.TABLE_LEVELS, null, info.toContentValues());

    }

    public LevelInfo getLevelInfo(int world, int level) {
        Cursor cursor = database.query(MySQLiteHelper.TABLE_LEVELS, MySQLiteHelper.COLUMN_TABLE_ALL,
                MySQLiteHelper.COLUMN_ID + " = " + LevelInfo.toId(world, level), null, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            int gotWorld = cursor.getInt(cursor.getColumnIndex(MySQLiteHelper.COLUMN_WORLD));
            int gotLevel = cursor.getInt(cursor.getColumnIndex(MySQLiteHelper.COLUMN_LEVEL));
            int gotComplete = cursor.getInt(cursor.getColumnIndex(MySQLiteHelper.COLUMN_COMPLETE));
            long gotTime = cursor.getLong(cursor.getColumnIndex(MySQLiteHelper.COLUMN_TIME));
            cursor.close();
            return new LevelInfo(gotWorld, gotLevel, gotTime, gotComplete);
        }
        cursor.close();
        return new LevelInfo(world, level, 0, 0);
    }

    public int getLastUnlockedWorld() {
        Cursor cursor = database.query(MySQLiteHelper.TABLE_LEVELS, new String[]{MySQLiteHelper.COLUMN_WORLD,
                MySQLiteHelper.COLUMN_LEVEL}, MySQLiteHelper.COLUMN_COMPLETE + " = " + 1, null, null, null, null);
        int last = 1;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int world = cursor.getInt(cursor.getColumnIndex(MySQLiteHelper.COLUMN_WORLD));
            int level = cursor.getInt(cursor.getColumnIndex(MySQLiteHelper.COLUMN_LEVEL));
            if (level == 21) {
                if (world + 1 > last) {
                    last = world + 1;
                }
            } else {
                if (world > last) {
                    last = world;
                }
            }
            cursor.moveToNext();
        }
        cursor.close();
        return last;
    }

    public int getLastUnlockedLevel(int world) {
        Cursor cursor = database.query(MySQLiteHelper.TABLE_LEVELS, new String[]{MySQLiteHelper.COLUMN_LEVEL},
                MySQLiteHelper.COLUMN_COMPLETE + " = " + 1 + " AND " + MySQLiteHelper.COLUMN_WORLD + " = " + world,
                null, null, null, null);
        int last = 0;
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            int level = cursor.getInt(cursor.getColumnIndex(MySQLiteHelper.COLUMN_LEVEL));
            if (level > last) {
                last = level;
            }
            cursor.moveToNext();

        }
        cursor.close();
        return last + 1;
    }
}
