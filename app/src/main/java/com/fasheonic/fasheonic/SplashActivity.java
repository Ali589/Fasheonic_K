package com.fasheonic.fasheonic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

       ImageView SplashLogo;
    TextView Slogan;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        ImageView display = (ImageView) findViewById(R.id.Flogo);
        TextView Slog = (TextView) findViewById(R.id.SloganTxt);


        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.move_right);
        //  Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.move_right);

        display.startAnimation(animation1);
        Slog.startAnimation(animation1);

        Thread thread = new Thread(){

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try{
                    sleep(3000);

                    startActivity(new Intent(SplashActivity.this,LogSign.class));
                    finish();
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        };
        thread.start();

    }
    public void round1(View v){
        Intent i = new Intent(SplashActivity.this,LogSign.class);
        startActivity(i);
        finish();
    }
}







