package com.studios927.rollingwallgame.ui;

import java.util.logging.Level;
import java.util.logging.Logger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.example.R;
import com.studios927.rollingwallgame.LevelSelectActivity;
import com.studios927.rollingwallgame.WorldSelectActivity;
import com.studios927.rollingwallgame.gameworkings.RomanNumeral;

public class HorizontalScrollView extends ViewGroup {
	private double lastTouchX;
	private int currentViewIndex;
	private Scroller mScroller;
	private int worldCount;
	private double offset, targetOffset;
	private int buttonWidth, buttonHeight;
	private int screenWidth, screenHeight;
	private int direction;
	private double oneAndAHalfButtonWidth;
	private double minScroll, maxScroll;
	private boolean pressed;
	private int lastUnlocked;

	public HorizontalScrollView(Context context) {
		super(context);
		init();
	}

	public HorizontalScrollView(Context context, AttributeSet attributes) {
		super(context, attributes);
		init();
	}

	private void init() {
		worldCount = WorldSelectActivity.WORLD_COUNT;
		WindowManager wm = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		screenWidth = display.getWidth();
		screenHeight = display.getHeight();
		buttonWidth = screenWidth / 3;
		buttonHeight = screenHeight / 2;
		oneAndAHalfButtonWidth = (int) Math.round(1.5 * buttonWidth);
		minScroll = oneAndAHalfButtonWidth;
		maxScroll = -(oneAndAHalfButtonWidth * (worldCount - 1));
		Button reusable;
		for (int count = 0; count < worldCount; count++) {
			reusable = (Button) LayoutInflater.from(getContext()).inflate(
					R.layout.btn_world, null);
			reusable.setWidth(buttonWidth);
			reusable.setHeight(buttonHeight);
			reusable.setText("World" + '\n' + RomanNumeral.int2roman(count + 1));
			reusable.setId(count);

			addView(reusable);

		}
		mScroller = new Scroller(this);
		mScroller.start();
	}

	public void setCurrentOffset(double newOffset) {
		offset = newOffset;
		post(new Runnable() {

			@Override
			public void run() {

				requestLayout();
			}

		});
	}

	public double getTargetOffset() {
		return targetOffset;
	}

	public double getCurrentOffset() {
		return offset;
	}

	@Override
	protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {

		for (int count = 0; count < worldCount; count++) {
			Button button = (Button) getChildAt(count);
			double x = buttonWidth + oneAndAHalfButtonWidth * count
					+ (int) Math.round(offset);
			double y = buttonHeight / 2;
			button.layout((int) x, (int) y, (int) (x + buttonWidth),
					(int) (y + buttonHeight));

		}
	}

	private void actionDown(MotionEvent event) {
		lastTouchX = event.getX();
		targetOffset = offset;
		if (!mScroller.isScrolling()) {
			pressed = true;
			if (event.getX() >= buttonWidth && event.getX() <= buttonWidth * 2
					&& event.getY() >= buttonHeight * .5f
					&& event.getY() <= buttonHeight * 1.5f) {

				findViewById(currentViewIndex).setPressed(true);
			}
		}
	}

	private void actionMove(MotionEvent event) {
		findViewById(currentViewIndex).setPressed(false);
		pressed = false;
		double moveX = event.getX();
		offset += moveX - lastTouchX;
		if (moveX > lastTouchX) {
			direction = -1;
		} else if (moveX < lastTouchX) {
			direction = 1;
		}
		lastTouchX = moveX;
		if (offset > minScroll) {
			offset = minScroll;
		} else if (offset < maxScroll - oneAndAHalfButtonWidth) {
			offset = maxScroll - oneAndAHalfButtonWidth;
		}
		requestLayout();
	}

	private void actionUp(MotionEvent event) {
		if (pressed) {

			if (event.getX() >= buttonWidth && event.getX() <= buttonWidth * 2
					&& event.getY() >= buttonHeight * .5f
					&& event.getY() <= buttonHeight * 1.5f) {

				if (lastUnlocked >= currentViewIndex + 1) {
					Intent intent = new Intent(getContext(),
							LevelSelectActivity.class);
					intent.putExtra(WorldSelectActivity.WORLD,
							currentViewIndex + 1);

					((Activity) getContext()).startActivity(intent);
				}
			}
			findViewById(currentViewIndex).setPressed(false);
			pressed = false;
		}
		if (offset < 0) {
			if (offset < maxScroll) {
				targetOffset = maxScroll;
				currentViewIndex = worldCount - 1;
			} else if (direction == 1) {
				currentViewIndex = (int) Math.abs(Math
						.floor((double) (offset / oneAndAHalfButtonWidth)));
				targetOffset = (-currentViewIndex) * (oneAndAHalfButtonWidth);
			} else if (direction == -1) {
				currentViewIndex = (int) Math.abs(Math
						.ceil((double) (offset / oneAndAHalfButtonWidth)));
				targetOffset = (-currentViewIndex) * (oneAndAHalfButtonWidth);
			}

		} else {
			targetOffset = 0;
			currentViewIndex = 0;
		}
		synchronized (mScroller) {
			mScroller.notify();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			actionDown(event);
		}
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			actionMove(event);
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			actionUp(event);
		}
		return true;
	}

	public int getCurrentIndex() {
		return (int) Math.round(currentViewIndex);
	}

	public boolean onInterceptTouchEvent(MotionEvent event) {
		onTouchEvent(event);
		return true;
	}

	public void killScroller() {
		mScroller.kill();
	}

	public void startScroller() {
		mScroller = new Scroller(this);
		mScroller.start();
	}

	public void setLastUnlocked(int lastUnlockedWorld) {
		for (int count = 0; count < worldCount; count++) {
			if (count + 1 > lastUnlockedWorld) {
				findViewById(count).setEnabled(false);
			} else {
				findViewById(count).setEnabled(true);
			}

		}
		lastUnlocked = lastUnlockedWorld;
	}
}
