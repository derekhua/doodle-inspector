package com.example.aleksey.doodle_inspector;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

public class ResultsScreen extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_screen);

        Typeface slab_typeface = Typeface.createFromAsset(getAssets(), "fonts/SlabThing.ttf");
        TextView myTextView = (TextView)findViewById(R.id.resultTextView);

        TextView scoreText = (TextView)findViewById(R.id.scoreTextView);
        TextView scoreText1 = (TextView)findViewById(R.id.score1TextView);
        TextView scoreText2 = (TextView)findViewById(R.id.score2TextView);
        int score = getIntent().getExtras().getInt("score");
        int score1 = getIntent().getExtras().getInt("score1");
        int score2 = getIntent().getExtras().getInt("score2");

        if (score < 0) {
            scoreText1.setText(score1 + "");
            scoreText2.setText(score2 + "");
        }
        else
            scoreText.setText(score+"");

        Button txt = (Button) findViewById(R.id.doodleAgainButton);
        txt.setTypeface(slab_typeface);
        myTextView.setTypeface(slab_typeface);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setRating((float)3.5);
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

    public void startAgain(View v){
        //go to finish page
        //finish();
        Intent i = new Intent(this, MainScreen.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
}
