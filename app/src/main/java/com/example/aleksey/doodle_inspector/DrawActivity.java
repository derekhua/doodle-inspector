package com.example.aleksey.doodle_inspector;

import android.graphics.Bitmap;
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

public class DrawActivity extends ActionBarActivity {

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://ec2-54-165-233-14.compute-1.amazonaws.com:3000");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    Emitter.Listener connectEmitter;
    Emitter.Listener helloEmitter;
    Emitter.Listener disconnectEmitter;
    Emitter.Listener imageEmitter;

    private DrawView drawView;
    private ImageButton currPaint;
    int numberOfImages = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        connectEmitter = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d("DrawActivity: ", "SOCKETLOG: socket connected");
                mSocket.emit("findMatch", "testing");
            }
        };
        helloEmitter = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                for (int i = 0; i < args.length; i++) {
                    Log.d("DrawActivity: ", "SOCKETLOG: " + args[i]);
                }
            }
        };
        disconnectEmitter = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d("DrawActivity: ", "SOCKETLOG: socket disconnected");
            }
        };
        imageEmitter = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                //mSocket.emit("imageProb", DrawView.getBase64Image());
            }
        };

        mSocket.on(Socket.EVENT_CONNECT, connectEmitter)
        .on("foundMatch", helloEmitter)
        .on(Socket.EVENT_DISCONNECT, disconnectEmitter)
        .on("imageProb", imageEmitter);

        mSocket.connect();  // initiate connection to socket server

        drawView = (DrawView) findViewById(R.id.drawing);
        LinearLayout paintLayout = (LinearLayout)findViewById(R.id.paint_colors);
        currPaint = (ImageButton)paintLayout.getChildAt(0);
        String color = currPaint.getTag().toString();
        drawView.setColor(color);
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
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
        mSocket.emit("imageProb", DrawView.getBase64Image());
        if (numberOfImages == 5) {
            //go to finish page
        }
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        mSocket.disconnect();
        mSocket.off(Socket.EVENT_CONNECT, connectEmitter);
        mSocket.off(Socket.EVENT_DISCONNECT, disconnectEmitter);
        mSocket.off("hello", helloEmitter);
        Log.d("DrawActivity: ", "SOCKETLOG: Socket off");

    }
}
