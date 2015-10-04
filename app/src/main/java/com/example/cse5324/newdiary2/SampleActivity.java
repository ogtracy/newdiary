package com.example.cse5324.newdiary2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
public class SampleActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";
    private RatingBar ratebar;
    private TextView value;
    private Button Submit;
    private float rate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ratebar = (RatingBar) findViewById(R.id.ratingBar);
        value = (TextView) findViewById(R.id.textView3);
        Submit = (Button) findViewById(R.id.saveButton);
        ratebar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                if(rating== (float) 0.5) {
                    value.setText(String.valueOf("WORST DAY"));
                }
                if(rating== (float) 1.0) {
                    value.setText(String.valueOf("NO COOL STUFF"));
                }
                if(rating== (float) 1.5) {
                    value.setText(String.valueOf("WENT SOMEWHAT FINE"));
                }

                if(rating== (float) 2) {
                    value.setText(String.valueOf("HAD A GREAT TIME"));
                }
                if(rating== (float) 2.5) {
                    value.setText(String.valueOf("SOME GOODTHINGS HAPPENED"));
                }
                if(rating== (float) 3) {
                    value.setText(String.valueOf("SPECIAL OCCASION"));
                }
                if(rating== (float) 3.5) {
                    value.setText(String.valueOf("FELT ENTHUSIASTIC"));}
                if(rating== (float) 4) {
                    value.setText(String.valueOf("MIRACLES HAPPENED LIFE CHANGING"));
                }
                if(rating== (float) 4.5) {
                    value.setText(String.valueOf("PRECIOUS DAY TO REMEMBER"));
                }
                if(rating== (float) 4.5) {
                    value.setText(String.valueOf("NO WORDS TO SAY"));
                }
                if (rating == (float) 5) {
                    value.setText(String.valueOf("DAY OF MY LIFE"));
                }


            }
        });
        Submit.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ratebar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    public void onRatingChanged(RatingBar ratingBar, float rating,
                                                boolean fromUser) {
                        rate = rating;
                    }
                });






            }

        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sample, menu);
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