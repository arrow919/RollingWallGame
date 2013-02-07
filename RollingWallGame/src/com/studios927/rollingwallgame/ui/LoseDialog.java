package com.studios927.rollingwallgame.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.example.R;
import com.studios927.rollingwallgame.gameworkings.GameMessage;

/**
 * Created with IntelliJ IDEA.
 * User: student
 * Date: 5/17/12
 * Time: 5:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class LoseDialog extends Dialog {
    private Handler handler = new Handler();

    public LoseDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        setContentView(R.layout.losedialoglayout);
        ((Button) findViewById(R.id.losedialogredobutton)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Message message = Message.obtain();
                message.obj = GameMessage.RETRY;
                handler.sendMessage(message);
            }
        });
        ((Button) findViewById(R.id.losedialogbackbutton)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Message message = Message.obtain();
                message.obj = GameMessage.BACK;
                handler.sendMessage(message);
            }
        });
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void onBackPressed() {
        Message message = Message.obtain();
        message.obj = GameMessage.BACK;
        handler.sendMessage(message);
        this.cancel();
    }
}
