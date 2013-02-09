package com.studios927.rollingwallgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.R;
import com.studios927.rollingwallgame.gameworkings.UpdateThread;
import com.studios927.rollingwallgame.ui.GameSurfaceView;

public class RollingWallGameActivity extends Activity {
	private Button worldSelect;
	private Button support;

	private GameSurfaceView gameSurface;
	private ViewGroup mainLayout;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		makeFullScreen();
		setContentView(R.layout.main);
		mainLayout = (ViewGroup) findViewById(R.id.mainlayout);
		worldSelect = (Button) findViewById(R.id.worldselectbutton);
		worldSelect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View args) {
				Intent intent = new Intent(getApplicationContext(),
						WorldSelectActivity.class);
				startActivity(intent);
			}

		});
		support = (Button) findViewById(R.id.supportbutton);
		support.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View args) {
				Intent intent = new Intent(getApplicationContext(),
						SupportActivity.class);
				startActivity(intent);
			}
		});
		gameSurface = new GameSurfaceView(getApplicationContext(), 0, 0);
		gameSurface.setMenuOrGame(true);
		gameSurface.setBoundaries(600, 600);
		mainLayout.addView(gameSurface);
		mainLayout.removeView(worldSelect);
		mainLayout.addView(worldSelect);
		mainLayout.removeView(support);
		mainLayout.addView(support);
		UpdateThread
				.setOrientation(((WindowManager) getSystemService(WINDOW_SERVICE))
						.getDefaultDisplay().getOrientation());
	}

	private void makeFullScreen() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
}