package com.example.aleksey.doodle_inspector;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.*;
import com.github.nkzawa.emitter.Emitter;
import android.util.Log;
import android.widget.TextView;
import org.json.JSONObject;

public class OneVOneActivity extends Activity {

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://ec2-54-165-233-14.compute-1.amazonaws.com:3000");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    Emitter.Listener connectEmitter;
    Emitter.Listener disconnectEmitter;
    Emitter.Listener image1v1Emitter;
    Emitter.Listener findMatchEmitter;

    private DrawView drawView;
    private ImageButton currPaint;
    int numberOfImages = 0;
    private CountDownTimer timer;
    private TextView timerTextField;
    private TextView wordTextField;
    private String word = "";

    private String wonResult;
    private String yourResult;
    private String theirResult;


    public OneVOneActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        Typeface dumb_typeface = Typeface.createFromAsset(getAssets(), "fonts/3Dumb.ttf");
        TextView drawWord = (TextView) findViewById(R.id.drawWord);
        TextView timerText = (TextView) findViewById(R.id.Timer);
        drawWord.setTypeface(dumb_typeface);
        timerText.setTypeface(dumb_typeface);

        connectEmitter = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d("OneVOneActivity: ", "SOCKETLOG: socket connected");
                mSocket.emit("findMatch");
            }
        };

        disconnectEmitter = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d("OneVOneActivity: ", "SOCKETLOG: socket disconnected");
            }
        };

        image1v1Emitter = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                final JSONObject json = ((JSONObject) args[0]);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            word = json.getString("word");
                            wonResult = json.getString("won");
                            yourResult = json.getString("yours");
                            theirResult = json.getString("theirs");
                            Log.d("wonResult: ", wonResult);
                            Log.d("yourResult: ", yourResult);
                            Log.d("theirResult: ", theirResult);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        findMatchEmitter = new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                final JSONObject json = ((JSONObject) args[0]);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            word = json.getString("word");
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        wordTextField.setText(word);
                        timer.cancel();
                        timer.start();
                    }
                });
            }
        };

        mSocket.on(Socket.EVENT_CONNECT, connectEmitter)
                .on(Socket.EVENT_DISCONNECT, disconnectEmitter)
                .on("image1v1", image1v1Emitter)
                .on("findMatch", findMatchEmitter);

        mSocket.connect();  // initiate connection to socket server

        drawView = (DrawView) findViewById(R.id.drawing);
        LinearLayout paintLayout = (LinearLayout)findViewById(R.id.paint_colors);
        currPaint = (ImageButton)paintLayout.getChildAt(0);
        String color = currPaint.getTag().toString();
        drawView.setColor(color);
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
        timerTextField = (TextView) findViewById(R.id.Timer);
        wordTextField = (TextView) findViewById(R.id.drawWord);

        timer = new CountDownTimer(15000, 1000) {

            public void onTick(long millisUntilFinished) {
                timerTextField.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                submit(null);
            }
        };


    }

    public void paintClicked(View view){
        //use chosen color
        if(view!=currPaint){
            //update color
            ImageButton imgView = (ImageButton)view;
            String color = view.getTag().toString();
            drawView.setColor(color);

            imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
            currPaint=(ImageButton)view;
        }

    }


    public void submit(View view) {
        //send request

        drawView.getScoreOfPicture();
        drawView.reset();
        numberOfImages++;
        Log.d("OneVOneActivity: ", "SOCKETLOG: sending image");
        String[] imageArray = new String[2];
        mSocket.emit("image1v1", DrawView.getBase64Image(), word);
        mSocket.emit("findMatch");

        if (numberOfImages == 5) {
            //go to finish page
            Intent i = new Intent(this, ResultsScreen.class);
            startActivity(i);

        }
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        mSocket.disconnect();
        mSocket.off(Socket.EVENT_CONNECT, connectEmitter);
        mSocket.off(Socket.EVENT_DISCONNECT, disconnectEmitter);
        mSocket.off("image1v1", image1v1Emitter);
        mSocket.off("findMatch", findMatchEmitter);
        Log.d("OneVOneActivity: ", "SOCKETLOG: Socket off");

    }
}
