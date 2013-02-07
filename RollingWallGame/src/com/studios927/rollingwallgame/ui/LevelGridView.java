package com.studios927.rollingwallgame.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.*;
import android.widget.Button;

import com.example.R;
import com.studios927.rollingwallgame.GameActivity;
import com.studios927.rollingwallgame.LevelSelectActivity;
import com.studios927.rollingwallgame.WorldSelectActivity;
import com.studios927.rollingwallgame.gameworkings.GameMessage;

public class LevelGridView extends ViewGroup {
    public static final int levelsPerColumn = 3;
    public static final int levelsPerRow = 7;
    private int displayWidth, displayHeight;
    private int buttonDimension;
    private int xCenterOffset, yCenterOffset;
    private int world;

    public LevelGridView(Context context) {
        super(context);
        init();

    }

    public LevelGridView(Context context, AttributeSet attributes) {
        super(context, attributes);
        init();
    }

    private void init() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        displayHeight = display.getHeight();
        displayWidth = display.getWidth();
        buttonDimension = (int) (displayWidth / 8.5f);
        int width = (int) (buttonDimension * (levelsPerRow + ((levelsPerRow - 1) * .1f)));
        int height = (int) (float) (buttonDimension * (levelsPerColumn + ((levelsPerColumn - 1) * .1f)));
        xCenterOffset = (displayWidth - width) / 2;
        yCenterOffset = (int) ((displayHeight - height) / 2.5f);
        Button reusable;
        for (int countC = 0; countC < levelsPerColumn; countC++) {
            for (int countR = 0; countR < levelsPerRow; countR++) {
                final int levelNumber = countC * levelsPerRow + countR;
                reusable = (Button) LayoutInflater.from(getContext()).inflate(R.layout.btn_level, null);
                reusable.setWidth(buttonDimension);
                reusable.setHeight(buttonDimension);
                reusable.setText(String.valueOf(levelNumber + 1));
                reusable.setId(levelNumber);
                // reusable.setGravity(Gravity.CENTER);
                reusable.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), GameActivity.class);
                        intent.putExtra(LevelSelectActivity.LEVEL, levelNumber + 1);
                        intent.putExtra(WorldSelectActivity.WORLD, world);
                        ((Activity) getContext()).startActivity(intent);
                        GameActivity.setHandler(new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                if (GameMessage.NEXT.equals(msg.obj)) {
                                    ((Activity) getContext()).finish();
                                }
                            }
                        });
                    }
                });
                addView(reusable);

            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        for (int count = 0; count < levelsPerColumn * levelsPerRow; count++) {
            int row = (int) Math.floor(count / levelsPerRow);
            int column = count - row * levelsPerRow;
            Button button = (Button) getChildAt(count);
            int x = column * buttonDimension * 11 / 10;
            int y = row * buttonDimension * 11 / 10;
            button.layout(x + xCenterOffset, y + yCenterOffset, x + buttonDimension + xCenterOffset, y + buttonDimension + yCenterOffset);
        }
    }

    public void setWorld(int world) {
        this.world = world;
    }

    public void setLastEnabled(int lastEnabled) {
        for (int count = 0; count < levelsPerColumn * levelsPerRow; count++) {
            if (count + 1 > lastEnabled) {
                findViewById(count).setEnabled(false);
            } else {
                findViewById(count).setEnabled(true);
            }
        }
    }

}
