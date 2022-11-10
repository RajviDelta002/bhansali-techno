package com.delta.bhansalitechno.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.delta.bhansalitechno.R;
import com.delta.bhansalitechno.utils.ConnectionDetector;
import com.delta.bhansalitechno.utils.PrefManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.util.Objects;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgFilter, imgFullScreen, imgStartBtn, imgStopBtn, imgExitScreen;
    private LinearLayout lylHeaderHide, lylDisplayData, lylStop;
    private LinearLayout lylWebViewShow, lylToolbar;
    private AutoCompleteTextView tvJobNo, tvItemCode;
    private TextInputEditText tvItemCode1, tvPlanQty, tvProductionQty;
    private TextView tvItemCodeShow, tvGroupShow, tvDrawingNoShow;
    private WebView webView;

    private TextView tvFilterText;
    private int screenOrientation = 1;

    private PrefManager prefManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_dashboard);
            prefManager = new PrefManager(DashboardActivity.this);
            prefManager.setLoggedIn("True");

            tvFilterText = findViewById(R.id.tvFilterText);
            imgFilter = findViewById(R.id.imgFilter);
            lylHeaderHide = findViewById(R.id.lylHeaderHide);
            imgFullScreen = findViewById(R.id.imgFullScreen);

            tvJobNo = findViewById(R.id.tvJobNo);
            tvItemCode = findViewById(R.id.tvItemCode);
            tvItemCode1 = findViewById(R.id.tvItemCode1);
            tvPlanQty = findViewById(R.id.tvPlanQty);
            imgStartBtn = findViewById(R.id.imgStartBtn);

            lylStop = findViewById(R.id.lylStop);
            tvProductionQty = findViewById(R.id.tvProductionQty);
            imgStopBtn = findViewById(R.id.imgStopBtn);

            lylDisplayData = findViewById(R.id.lylDisplayData);
            tvItemCodeShow = findViewById(R.id.tvItemCodeShow);
            tvGroupShow = findViewById(R.id.tvGroupShow);
            tvDrawingNoShow = findViewById(R.id.tvDrawingNoShow);
            lylWebViewShow = findViewById(R.id.lylWebViewShow);
            webView = findViewById(R.id.webView);
            imgExitScreen = findViewById(R.id.imgExitScreen);

            lylToolbar = findViewById(R.id.lylToolbar);

            imgFilter.setOnClickListener(this);
            imgFullScreen.setOnClickListener(this);
            tvJobNo.setOnClickListener(this);
            tvItemCode.setOnClickListener(this);
            tvItemCode1.setOnClickListener(this);
            imgStartBtn.setOnClickListener(this);
            imgStopBtn.setOnClickListener(this);
            imgExitScreen.setOnClickListener(this);

            LottieAnimationView ltLogout = findViewById(R.id.ltLogout);
            ltLogout.setOnClickListener(v -> {
                prefManager.setLoggedIn("False");
                startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
                finish();
            });
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

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.imgFilter:
                    if (lylHeaderHide.getVisibility() == View.VISIBLE) {
                        lylHeaderHide.setVisibility(View.GONE);
                    } else {
                        lylHeaderHide.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.imgFullScreen:
                    imgExitScreen.setVisibility(View.VISIBLE);
                    tvFilterText.setVisibility(View.GONE);
                    imgFilter.setVisibility(View.GONE);
                    imgFullScreen.setVisibility(View.GONE);
                    lylHeaderHide.setVisibility(View.GONE);
                    lylToolbar.setVisibility(View.GONE);
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    break;
                case R.id.tvJobNo:
                    break;
                case R.id.tvItemCode:
                    break;
                case R.id.tvItemCode1:
                    break;
                case R.id.imgStartBtn:
                    startBtnFun();
                    break;
                case R.id.imgStopBtn:
                    stopBtnFun();
                    break;
                case R.id.imgExitScreen:
                    imgExitScreen.setVisibility(View.GONE);
                    tvFilterText.setVisibility(View.VISIBLE);
                    imgFilter.setVisibility(View.VISIBLE);
                    imgFullScreen.setVisibility(View.VISIBLE);
                    lylHeaderHide.setVisibility(View.GONE);
                    lylToolbar.setVisibility(View.VISIBLE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    public boolean isInternet() {
        return new ConnectionDetector(DashboardActivity.this).isInternetConnected();
    }

    private void webView() {
        try {
            final ProgressDialog p = new ProgressDialog(DashboardActivity.this);
            p.setCancelable(false);
            p.setCanceledOnTouchOutside(false);
            p.setMessage("Opening Document...");
            if (!p.isShowing()) {
                p.show();
            }

            //Setting Up Web View
            webView.clearCache(true);
            webView.clearHistory();
            WebSettings webSettings = webView.getSettings();
            //webSettings.setJavaScriptEnabled(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.setSupportZoom(true);
            webSettings.setBuiltInZoomControls(true);
            webSettings.setDisplayZoomControls(true);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return true;
                }

                @Override
                public void onLoadResource(WebView view, String url) {
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    if (p.isShowing()) {
                        p.dismiss();
                    }
                    imgFullScreen.setVisibility(View.VISIBLE);
                }
            });
            //  wv.setInitialScale((int) wv.getScale());
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setUseWideViewPort(true);
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            webView.requestFocus();
            webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
            webView.setScrollbarFadingEnabled(false);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                webSettings.setAllowUniversalAccessFromFileURLs(true);
                webSettings.setAllowFileAccessFromFileURLs(true);
            }

            webView.loadUrl("http://deltaierp.com/");
        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    private void startBtnFun() {
        try {
            lylHeaderHide.setVisibility(View.GONE);
            tvJobNo.setEnabled(false);
            tvItemCode.setEnabled(false);
            tvItemCode1.setEnabled(false);
            tvPlanQty.setEnabled(false);
            imgStartBtn.setVisibility(View.GONE);
            lylStop.setVisibility(View.VISIBLE);
            lylDisplayData.setVisibility(View.VISIBLE);
            lylWebViewShow.setVisibility(View.VISIBLE);
            if (isInternet()) {
                webView();
            } else {
                Toast.makeText(this, "No Internet.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    private void stopBtnFun() {
        tvJobNo.setEnabled(true);
        tvItemCode.setEnabled(true);
        tvItemCode1.setEnabled(true);
        tvPlanQty.setEnabled(true);
        tvProductionQty.setEnabled(false);
        Objects.requireNonNull(tvProductionQty.getText()).clear();
        imgStartBtn.setVisibility(View.VISIBLE);
        lylStop.setVisibility(View.GONE);
        lylDisplayData.setVisibility(View.GONE);
        lylWebViewShow.setVisibility(View.GONE);
    }

    /*@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }*/
}
