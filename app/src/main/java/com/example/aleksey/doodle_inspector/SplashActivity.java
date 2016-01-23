package com.example.aleksey.doodle_inspector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Derek on 1/23/16.
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Go to MainScreen
        Intent intent = new Intent(this, MainScreen.class);
        startActivity(intent);
        finish();
    }
}