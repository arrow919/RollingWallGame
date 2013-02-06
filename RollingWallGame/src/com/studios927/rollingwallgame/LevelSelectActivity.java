package com.studios927.rollingwallgame;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

import com.example.R;
import com.studios927.rollingwallgame.database.GameDataSource;
import com.studios927.rollingwallgame.ui.LevelGridView;

public class LevelSelectActivity extends Activity {
    private int world;
    public static final String LEVEL = "LEVEL";
    public static GameDataSource gameData;
    private LevelGridView levels;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeFullScreen();
        setContentView(R.layout.levelselectlayout);
        world = getIntent().getExtras().getInt(WorldSelectActivity.WORLD);
        // System.out.println(world);
        findViewById(R.id.lsback).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        levels = ((LevelGridView) findViewById(R.id.lslevelgrid));
        levels.setWorld(world);
    }

    @Override
    public void onResume() {
        super.onResume();
        gameData = new GameDataSource(getApplicationContext());
        gameData.open();
        levels.setLastEnabled(gameData.getLastUnlockedLevel(world));
        
    }

    @Override
    public void onPause() {
        super.onPause();
        gameData.close();
    }

    private void makeFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
