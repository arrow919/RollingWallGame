package com.studios927.rollingwallgame;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.R;
import com.studios927.rollingwallgame.database.GameDataSource;
import com.studios927.rollingwallgame.ui.HorizontalScrollView;

import java.util.logging.Logger;

public class WorldSelectActivity extends Activity {
    public static final String WORLD = "com.studios927.rollingwallgame.WORLD";
    HorizontalScrollView worlds;
    public static final int WORLD_COUNT = 5;
    public static GameDataSource gameData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeFullScreen();
        setContentView(R.layout.worldselectlayout);
        Button back = (Button) findViewById(R.id.wsback);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        worlds = (HorizontalScrollView) findViewById(R.id.wslistview);
    }

    @Override
    public void onResume() {
        super.onResume();
        gameData = new GameDataSource(getApplicationContext());
        gameData.open();
        int lastUnlocked = gameData.getLastUnlockedWorld();
        worlds.setLastUnlocked(lastUnlocked);
        Logger.getLogger("WorldSelectActivity").warning("Last Unlocked: " + lastUnlocked);
        worlds.startScroller();
    }

    @Override
    public void onPause() {
        super.onPause();
        gameData.close();
        worlds.killScroller();
    }

    private void makeFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
