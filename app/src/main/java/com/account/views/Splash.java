package com.account.views;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.account.R;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.snackbar.Snackbar;

import broadcast.NetworkCheack;

public class Splash extends AppCompatActivity {

    private NetworkCheack networkCheack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        networkCheack = new NetworkCheack();

        YoYo.with(Techniques.Tada)
                .duration(700)
                .repeat(5)
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .playOn(findViewById(R.id.iv));
    }

    void handler() {
        Handler handle = new Handler();
        handle.postDelayed(this::showLogin, 3000);
    }

    private void showLogin() {

        Intent intent = new Intent(Splash.this, LoginActivity_.class);
        startActivity(intent);
        finish();
    }


    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(networkCheack);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterNetworkChanges();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (networkCheack.isOnline(getBaseContext())) {
            handler();
        } else {
            Snackbar.make(findViewById(R.id.mySplash), "Connecta te a internet", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (networkCheack.isOnline(getBaseContext())) {
            handler();
        } else {
            Snackbar.make(findViewById(R.id.mySplash), "Connecta te a internet", Snackbar.LENGTH_LONG).show();
        }
    }
}
