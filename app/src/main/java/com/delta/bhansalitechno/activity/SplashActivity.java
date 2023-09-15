package com.delta.bhansalitechno.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.delta.bhansalitechno.R;
import com.delta.bhansalitechno.utils.PrefManager;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

public class SplashActivity extends AppCompatActivity {

    private PrefManager prefManager;

    private int screenOrientation = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            setContentView(R.layout.activity_splash);
            prefManager = new PrefManager(SplashActivity.this);

            /*ImageView imgLogo1 = findViewById(R.id.imgLogo1);
            LinearLayout lylDeltaLogo = findViewById(R.id.lylDeltaLogo);
            Animation mAnimationSlide1 = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.anim_in);
            imgLogo1.startAnimation(mAnimationSlide1);
            lylDeltaLogo.startAnimation(mAnimationSlide1);*/

            new CountDownTimer(2500, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    if (!isFinishing()) {
                        if (prefManager.isLoggedIn().equalsIgnoreCase("True")
                                && !prefManager.getMachineName().isEmpty()
                                && !prefManager.getJobListArray().isEmpty()) {
                            //startActivity(new Intent(SplashActivity.this, DashboardActivityNew.class));
                            startActivity(new Intent(SplashActivity.this, StopJobListActivity.class));
                        }else if (prefManager.isLoggedIn().equalsIgnoreCase("True")
                                && !prefManager.getMachineName().isEmpty()
                                /*&& !prefManager.getJobListArray().isEmpty()*/) {
                            //startActivity(new Intent(SplashActivity.this, DashboardActivityNew.class));
                            startActivity(new Intent(SplashActivity.this, StopJobListActivity.class));
                        } else if (prefManager.isLoggedIn().equalsIgnoreCase("True") /*&& !prefManager.getMachineName().isEmpty()*/) {
                            startActivity(new Intent(SplashActivity.this, MachineActivity.class));
                        } else {
                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        }
                        finish();
                    }
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("ScreenOrientation", screenOrientation);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        screenOrientation = savedInstanceState.getInt("ScreenOrientation");
        super.onRestoreInstanceState(savedInstanceState);
    }
}
