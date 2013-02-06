package com.studios927.rollingwallgame.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

	public static final String TABLE_LEVELS = "levels";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_WORLD = "world";
	public static final String COLUMN_LEVEL = "level";
	public static final String COLUMN_COMPLETE = "completed";
	public static final String COLUMN_TIME = "time1";

	public static final String[] COLUMN_TABLE_ALL = { COLUMN_ID, COLUMN_WORLD, COLUMN_LEVEL, COLUMN_COMPLETE, COLUMN_TIME };
	private static final String DATABASE_NAME = "levels.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table " + TABLE_LEVELS + "( " + COLUMN_ID + " integer primary key autoincrement, " + COLUMN_WORLD
			+ " text, " + COLUMN_LEVEL + " text, " + COLUMN_COMPLETE + " text, " + COLUMN_TIME + " integer)";

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySQLiteHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LEVELS);
		onCreate(db);
	}

}
