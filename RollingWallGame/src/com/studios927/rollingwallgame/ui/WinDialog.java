package com.studios927.rollingwallgame.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.R;
import com.studios927.rollingwallgame.gameworkings.GameMessage;

/**
 * Created with IntelliJ IDEA.
 * User: student
 * Date: 5/17/12
 * Time: 5:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class WinDialog extends Dialog {
    private Handler handler = new Handler();
    private long time;

    public WinDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        setContentView(R.layout.windialoglayout);
        Button retryButton = (Button) findViewById(R.id.windialogredobutton);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = Message.obtain();
                message.obj = GameMessage.BACK;
                handler.sendMessage(message);
            }
        });
        Button nextButton = (Button) findViewById(R.id.windialogforwardbutton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = Message.obtain();
                message.obj = GameMessage.NEXT;
                handler.sendMessage(message);
            }
        });
        long seconds = Math.round(time / 1000);
        time -= seconds * 1000;
        String hundredths = String.valueOf(Math.round(time / 10));
        hundredths = hundredths.substring(Math.max(0, hundredths.length() - 2), Math.max(0, hundredths.length() - 1));
        ((TextView) findViewById(R.id.windialogtime)).setText(String.valueOf(seconds) + ":" + hundredths);
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public void onBackPressed() {
        Message message = Message.obtain();
        message.obj=GameMessage.BACK;
        handler.sendMessage(message);
        this.cancel();
    }
}
