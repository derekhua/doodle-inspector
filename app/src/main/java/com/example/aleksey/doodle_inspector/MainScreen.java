package com.example.aleksey.doodle_inspector;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

public class MainScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        Typeface dumb_typeface = Typeface.createFromAsset(getAssets(), "fonts/3Dumb.ttf");
        TextView title = (TextView)findViewById(R.id.title);
        TextView tag = (TextView)findViewById(R.id.tag);
        Button singleMatch = (Button) findViewById(R.id.start_button);
        Button doubleMatch = (Button) findViewById(R.id.start1v1button);
        singleMatch.setTypeface(dumb_typeface);
        doubleMatch.setTypeface(dumb_typeface);
        title.setTypeface(dumb_typeface);
        tag.setTypeface(dumb_typeface);

        ImageView image = (ImageView) findViewById(R.id.logo);
        Animation slideLogo = AnimationUtils.loadAnimation(this, R.anim.slide);
        Animation slideTitle = AnimationUtils.loadAnimation(this, R.anim.slide);
        slideLogo.setStartOffset(600);
        image.startAnimation(slideLogo);
        title.startAnimation(slideTitle);

        final Animation wobble = AnimationUtils.loadAnimation(this, R.anim.wobble);
        final ImageView img = (ImageView) findViewById(R.id.logo);
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                img.startAnimation(wobble);
                // your code here
            }
        });

        final Button button = (Button) findViewById(R.id.start_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), DrawActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });
        final Button button1v1 = (Button) findViewById(R.id.start1v1button);
        button1v1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), OneVOneActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
