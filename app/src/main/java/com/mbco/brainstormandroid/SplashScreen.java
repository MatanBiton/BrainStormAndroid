package com.mbco.brainstormandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {

    private long startTime, endTime;

    private Requests requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        //initialize requests queue
        requests = Requests.getInstance(this);

        //verifying that the device has all of the needed permissions
        new Permission(this).verifyPermissions();

        //displaying the animation of the app's logo
        ImageView imgLogo = findViewById(R.id.imgLogo);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splashanimation);
        imgLogo.startAnimation(animation);

        //setting the initial start time of the animation
        startTime = SystemClock.elapsedRealtime();

        //activating the thread to check if the device is able to talk to the backend
        new TestConnection(this).start();
    }


    public class TestConnection extends Thread{

        private final Context context;

        public TestConnection(Context context){
            this.context = context;
        }

        @Override
        public void run() {
            super.run();

            //starting to test the connection using an interface on callback with the answer

            requests.TestConnection(new RequestsResultListener<Boolean>() {
                @Override
                public void getResult(Boolean result) {
                    if (result) {
                         //checking how long it has been since the animation started in order to let it finish completely
                        endTime = SystemClock.elapsedRealtime();
                        long deltaTime = endTime - startTime;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(context, Login.class));
                                finish();
                            }
                        }, deltaTime < 3000 ? 3000 - deltaTime : 0);
                    } else{
                        //show Connection error
                    }
                }
            });

        }
    }
}