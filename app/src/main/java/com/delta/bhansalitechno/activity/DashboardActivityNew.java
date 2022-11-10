package com.delta.bhansalitechno.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.airbnb.lottie.LottieAnimationView;
import com.delta.bhansalitechno.R;
import com.delta.bhansalitechno.api.ApiUtil;
import com.delta.bhansalitechno.bottomsheets.AppExitFragment;
import com.delta.bhansalitechno.bottomsheets.LogoutFragment;
import com.delta.bhansalitechno.bottomsheets.MessageFragment;
import com.delta.bhansalitechno.bottomsheets.NoInternetFragment;
import com.delta.bhansalitechno.fargments.TextListFragmentWithFilter;
import com.delta.bhansalitechno.interfaces.OnResponse;
import com.delta.bhansalitechno.model.JobNoModel;
import com.delta.bhansalitechno.model.StopJobNoModel;
import com.delta.bhansalitechno.utils.ConnectionDetector;
import com.delta.bhansalitechno.utils.NetworkUtils;
import com.delta.bhansalitechno.utils.PrefManager;
import com.delta.bhansalitechno.utils.Utils;
import com.delta.bhansalitechno.utils.ZoomLinearLayout;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import es.voghdev.pdfviewpager.library.PDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;
import okhttp3.RequestBody;

import static com.delta.bhansalitechno.utils.AppConfig.API_CHECK_JOB_START;
import static com.delta.bhansalitechno.utils.AppConfig.API_JOB_NO;
import static com.delta.bhansalitechno.utils.AppConfig.API_START_JOB;
import static com.delta.bhansalitechno.utils.AppConfig.API_STOP_JOB;
import static com.delta.bhansalitechno.utils.AppConfig.API_VERSION;
import static com.delta.bhansalitechno.utils.AppConfig.KEY_AFOSTROPHE;
import static com.delta.bhansalitechno.utils.AppConfig.KEY_BTM_SHT;
import static com.delta.bhansalitechno.utils.AppConfig.KEY_EMPER;
import static com.delta.bhansalitechno.utils.AppConfig.KEY_U0026;
import static com.delta.bhansalitechno.utils.AppConfig.KEY_U0027;
import static com.delta.bhansalitechno.utils.AppConfig.NULL;

public class DashboardActivityNew extends AppCompatActivity implements View.OnClickListener {

    private PrefManager prefManager;

    private LinearLayout lylFilter;
    private ImageView imgFilter, imgRotation, imgFullScreen, imgStartBtn, imgExitScreen;
    private TextInputEditText tvJobNo, tvItemCode;
    private TextInputEditText tvPlanQty, tvProductionQty;
    private LinearLayout lylDisplayData, lylStop;
    private WebView webView;
    private PDFViewPager pdfViewPager;
    private LinearLayout lylSwipe;
    private LinearLayout lylPdfShow;
    private TextView tvStartTime;
    private LottieAnimationView ltLogout;
    private TextView tvFilter, tvRotation;
    private TextView tvItemCodeShow;
    private TextView tvShopName;
    private TextView tvGroupShow;
    private TextView tvMachineName;
    private TextView tvDrawingNoShow;
    private TextView tvProcessName;

    private String mainUrl = "";
    private String file;
    private PDFPagerAdapter adapter;
    private ProgressDialog progressDialog = null;
    private int screenOrientation = 1;

    long MillisecondTime, StartTime;
    Handler handler;
    int Seconds, Minutes, hour;
    int rotation;

    PDFView pdfView;

    ArrayList<JobNoModel> jobNoList = new ArrayList<>();
    ArrayList<JobNoModel> startJobNoList = new ArrayList<>();
    ArrayList<StopJobNoModel> checkJobNoList = new ArrayList<>();
    ArrayList<StopJobNoModel> stopJobNoList = new ArrayList<>();
    String selectedJobId = "";
    String selectedJobName = "";

    private android.app.AlertDialog alertDialog;

    String checkJobStartOrNot = "";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_dashboard_new);
            prefManager = new PrefManager(DashboardActivityNew.this);
            rotation = 90;

            imgFilter = findViewById(R.id.imgFilter);
            lylFilter = findViewById(R.id.lylFilter);
            TextView tvNameWithCompany = findViewById(R.id.tvNameWithCompany);
            tvNameWithCompany.setText(prefManager.getFirstName());
            imgFullScreen = findViewById(R.id.imgFullScreen);

            tvJobNo = findViewById(R.id.tvJobNo);
            tvItemCode = findViewById(R.id.tvItemCode);
            tvPlanQty = findViewById(R.id.tvPlanQty);
            imgStartBtn = findViewById(R.id.imgStartBtn);

            lylStop = findViewById(R.id.lylStop);
            tvProductionQty = findViewById(R.id.tvProductionQty);
            ImageView imgStopBtn = findViewById(R.id.imgStopBtn);

            lylDisplayData = findViewById(R.id.lylDisplayData);
            tvItemCodeShow = findViewById(R.id.tvItemCodeShow);
            tvGroupShow = findViewById(R.id.tvGroupShow);
            tvDrawingNoShow = findViewById(R.id.tvDrawingNoShow);
            tvShopName = findViewById(R.id.tvShopName);
            tvMachineName = findViewById(R.id.tvMachineName);
            tvProcessName = findViewById(R.id.tvProcessName);

            webView = findViewById(R.id.webView);
            imgExitScreen = findViewById(R.id.imgExitScreen);

            pdfViewPager = findViewById(R.id.pdfViewPager);
            lylSwipe = findViewById(R.id.lylSwipe);
            TextView tvDismiss = findViewById(R.id.tvDismiss);
            lylPdfShow = findViewById(R.id.lylPdfShow);
            tvStartTime = findViewById(R.id.tvStartTime);
            imgRotation = findViewById(R.id.imgRotation);

            tvFilter = findViewById(R.id.tvFilter);
            tvRotation = findViewById(R.id.tvRotation);

            imgFilter.setOnClickListener(this);
            tvFilter.setOnClickListener(this);
            imgFullScreen.setOnClickListener(this);
            tvJobNo.setOnClickListener(this);
            //tvItemCode.setOnClickListener(this);
            imgStartBtn.setOnClickListener(this);
            imgStopBtn.setOnClickListener(this);
            imgExitScreen.setOnClickListener(this);
            tvDismiss.setOnClickListener(this);
            imgRotation.setOnClickListener(this);
            tvRotation.setOnClickListener(this);

            ltLogout = findViewById(R.id.ltLogout);
            ltLogout.setOnClickListener(v -> {
                showAppLogoutMessage();
            });

            pdfView = findViewById(R.id.idPDFView);

            apiJobNoList();
            apiVersion();
            apiCheckStartJob();

            /*final ZoomLinearLayout zoomLinearLayout = (ZoomLinearLayout) findViewById(R.id.zoom_linear_layout);
            zoomLinearLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    zoomLinearLayout.init(DashboardActivityNew.this);
                    return false;
                }
            });*/
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
                case R.id.tvFilter:
                    if (lylFilter.getVisibility() == View.VISIBLE) {
                        lylFilter.setVisibility(View.GONE);
                        //lylDisplayData.setVisibility(View.GONE);
                        tvFilter.setText("Show");
                    } else {
                        lylFilter.setVisibility(View.VISIBLE);
                        //lylDisplayData.setVisibility(View.VISIBLE);
                        tvFilter.setText("Hide");
                    }
                    break;
                case R.id.imgFullScreen:
                    imgExitScreen.setVisibility(View.VISIBLE);
                    imgFilter.setVisibility(View.GONE);
                    tvFilter.setVisibility(View.GONE);
                    imgFullScreen.setVisibility(View.GONE);
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    break;
                case R.id.tvJobNo:
                    funJobNo();
                    break;
                /*case R.id.tvItemCode:
                    Toast.makeText(this, "Select Item Code Value", Toast.LENGTH_SHORT).show();
                    break;*/
                case R.id.imgStartBtn:
                    if (Objects.requireNonNull(tvJobNo.getText()).toString().isEmpty()) {
                        Toast.makeText(this, "Please select Job No first.", Toast.LENGTH_SHORT).show();
                    } else {
                        if (checkJobStartOrNot.equalsIgnoreCase("False")) {
                            apiStartJob();
                        }
                    }
                    //startBtnFun();
                    //ltLogout.setVisibility(View.GONE);
                    break;
                case R.id.imgStopBtn:
                    stopFun();
                    /*stopBtnFun();
                    lylFilter.setVisibility(View.VISIBLE);
                    imgRotation.setVisibility(View.GONE);
                    tvRotation.setVisibility(View.GONE);
                    ltLogout.setVisibility(View.VISIBLE);*/
                    //deleteContents(new File(DashboardActivityNew.this.getExternalFilesDir(null) + "/BhansaliTechno"));
                    //deleteFileCreateFolder();
                    //deleteRecursive(new File(DashboardActivityNew.this.getExternalFilesDir(null) + "/BhansaliTechno", file));
                    break;
                case R.id.imgExitScreen:
                    imgExitScreen.setVisibility(View.GONE);
                    imgFilter.setVisibility(View.VISIBLE);
                    tvFilter.setVisibility(View.VISIBLE);
                    imgFullScreen.setVisibility(View.VISIBLE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    break;
                case R.id.tvDismiss:
                    lylSwipe.setVisibility(View.GONE);
                    break;
                case R.id.imgRotation:
                case R.id.tvRotation:
                    rotationPdf();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    @Override
    public void onBackPressed() {
        showAppExitMessage();
    }

    public boolean isInternet() {
        return new ConnectionDetector(DashboardActivityNew.this).isInternetConnected();
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

    private void openPDFView() {
        try {
            String pathName = DashboardActivityNew.this.getExternalFilesDir(null) + "/BhansaliTechno";
            String fileName = "";
            File directory = new File(pathName);
            File[] files = directory.listFiles();
            try {
                if (files != null && files.length > 0) {
                    for (File value : files) {
                        Log.d("Files", "FileName:" + value.getName());
                        if (value.getName().equals(file)) {
                            fileName = value.getName();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (!file.equals(fileName)) {
                new DashboardActivityNew.DownloadingTask().execute();
            } else {
                webView.setVisibility(View.GONE);
                lylPdfShow.setVisibility(View.VISIBLE);
                pdfViewPager.setVisibility(View.VISIBLE);
                //imgRotation.setVisibility(View.VISIBLE);
                tvRotation.setVisibility(View.VISIBLE);
                pdfViewPager = new PDFViewPager(DashboardActivityNew.this, getPdfPath());
                pdfViewPager.setId(R.id.pdfViewPager);
                adapter = new PDFPagerAdapter(DashboardActivityNew.this, getPdfPath());
                pdfViewPager.setAdapter(adapter);

                if (adapter != null && adapter.getCount() > 1) {
                    lylSwipe.setVisibility(View.VISIBLE);
                } else {
                    lylSwipe.setVisibility(View.GONE);
                }
                lylPdfShow.removeAllViewsInLayout();
                lylPdfShow.addView(pdfViewPager, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                //lylPdfShow.setScaleX(1.25f);
                //lylPdfShow.setScaleY(2f);
                adapter = new PDFPagerAdapter(DashboardActivityNew.this, getPdfPath());
                pdfViewPager.setAdapter(adapter);
                if (adapter != null && adapter.getCount() > 1) {
                    lylSwipe.setVisibility(View.VISIBLE);
                } else {
                    lylSwipe.setVisibility(View.GONE);
                }
                /*if (prefManager.getStartTime().equalsIgnoreCase("")){
                    new Handler(Looper.getMainLooper()).post(() -> {
                        handler = new Handler();
                        StartTime = Long.parseLong(prefManager.getStartTime());
                        prefManager.setStartTime(String.valueOf(StartTime));
                        handler.postDelayed(runnable, 0);
                    });
                }else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        handler = new Handler();
                        StartTime = SystemClock.uptimeMillis();
                        prefManager.setStartTime(String.valueOf(StartTime));
                        handler.postDelayed(runnable, 0);
                    });
                }*/
                new Handler(Looper.getMainLooper()).post(() -> {
                    handler = new Handler();
                    StartTime = SystemClock.uptimeMillis();
                    //prefManager.setStartTime(String.valueOf(StartTime));
                    handler.postDelayed(runnable, 0);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    private String getPdfPath() {
        File f = new File(DashboardActivityNew.this.getExternalFilesDir(null) + "/BhansaliTechno", file);
        return f.getAbsolutePath();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((PDFPagerAdapter) pdfViewPager.getAdapter()).close();
    }

    @SuppressLint("StaticFieldLeak")
    private class DownloadingTask extends AsyncTask<String, Integer, String> {

        File outputFile = null;
        String fileName = file;
        String storage = DashboardActivityNew.this.getExternalFilesDir(null) + "/BhansaliTechno";
        File folder = new File(storage);
        URL url;

        {
            try {
                url = new URL(mainUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(DashboardActivityNew.this, "MalformedURL", Toast.LENGTH_SHORT).show();
            }
        }

        HttpURLConnection c;

        {
            try {
                c = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(DashboardActivityNew.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(DashboardActivityNew.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.show();

        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if (outputFile != null) {
                    progressDialog.dismiss();

                    Toast.makeText(DashboardActivityNew.this, "Document Load Successfully", Toast.LENGTH_SHORT).show();
                    webView.setVisibility(View.GONE);
                    pdfViewPager.setVisibility(View.VISIBLE);
                    //imgRotation.setVisibility(View.VISIBLE);
                    tvRotation.setVisibility(View.VISIBLE);
                    pdfViewPager = new PDFViewPager(DashboardActivityNew.this, getPdfPath());
                    pdfViewPager.setId(R.id.pdfViewPager);
                    adapter = new PDFPagerAdapter(DashboardActivityNew.this, getPdfPath());
                    pdfViewPager.setAdapter(adapter);

                    if (adapter != null && adapter.getCount() > 1) {
                        lylSwipe.setVisibility(View.VISIBLE);
                    } else {
                        lylSwipe.setVisibility(View.GONE);
                    }
                    lylPdfShow.removeAllViewsInLayout();
                    lylPdfShow.addView(pdfViewPager, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

                    /*if (prefManager.getStartTime().equalsIgnoreCase("")){
                        new Handler(Looper.getMainLooper()).post(() -> {
                            handler = new Handler();
                            StartTime = Long.parseLong(prefManager.getStartTime());
                            prefManager.setStartTime(String.valueOf(StartTime));
                            handler.postDelayed(runnable, 0);
                        });
                    }else {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            handler = new Handler();
                            StartTime = SystemClock.uptimeMillis();
                            prefManager.setStartTime(String.valueOf(StartTime));
                            handler.postDelayed(runnable, 0);
                        });
                    }*/

                    new Handler(Looper.getMainLooper()).post(() -> {
                        handler = new Handler();
                        StartTime = SystemClock.uptimeMillis();
                        handler.postDelayed(runnable, 0);
                    });
                } else {
                    new Handler().postDelayed(() -> {
                    }, 3000);
                    c.disconnect();
                    Toast.makeText(DashboardActivityNew.this, c.getResponseMessage(), Toast.LENGTH_SHORT).show();
                    //progressDialog.dismiss();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Handler().postDelayed(() -> {
                }, 3000);
                c.disconnect();
                try {
                    Toast.makeText(DashboardActivityNew.this, c.getResponseMessage(), Toast.LENGTH_SHORT).show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                progressDialog.dismiss();
            }
            super.onPostExecute(result);
        }

        @SuppressWarnings({"ResultOfMethodCallIgnored", "StatementWithEmptyBody"})
        @Override
        protected String doInBackground(String... strings) {
            try {
                c.setRequestMethod("GET");
                c.connect();
                if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                } else {
                    if (!folder.exists()) {
                        folder.mkdir();
                    }
                    outputFile = new File(DashboardActivityNew.this.getExternalFilesDir(null) + "/BhansaliTechno", fileName);
                    //outputFile = new File(fileName);
                    if (!outputFile.exists()) {
                        outputFile.createNewFile();
                    }
                    int fileLength = c.getContentLength();
                    FileOutputStream fos = new FileOutputStream(outputFile);
                    InputStream is = c.getInputStream();
                    byte[] buffer = new byte[4096];
                    int count;
                    long total = 0;
                    while ((count = is.read(buffer)) != -1) {
                        total += count;
                        publishProgress((int) (total * 100 / fileLength));
                        fos.write(buffer, 0, count);
                    }
                    fos.close();
                    is.close();
                }
            } catch (Exception e) {
                c.disconnect();
                progressDialog.dismiss();
                e.printStackTrace();
                outputFile = null;
                Toast.makeText(DashboardActivityNew.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            progressDialog.setIndeterminate(false);
            progressDialog.setMax(100);
            progressDialog.setProgress(progress[0]);
        }
    }

    private Runnable runnable = new Runnable() {
        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        public void run() {
            try {
                MillisecondTime = SystemClock.uptimeMillis() - StartTime;
                Seconds = (int) (MillisecondTime / 1000);
                hour = Seconds / 1000 * 60 * 60 % 24;
                Minutes = Seconds / 60;
                Seconds = Seconds % 60;

                new Handler(Looper.getMainLooper()).post(() -> {
                    String totalTime = String.format("%02d", hour) + " " + String.format("%02d", Minutes) + " " + String.format("%02d", Seconds);
                    prefManager.setHandlerTime(totalTime);
                    tvStartTime.setText(prefManager.getHandlerTime());
                });
                handler.postDelayed(this, 0);

                /*if (prefManager.getHandlerTime().equalsIgnoreCase("")){
                    MillisecondTime = SystemClock.uptimeMillis() - StartTime;
                    //UpdateTime = TimeBuff + MillisecondTime;
                    Seconds = (int) (MillisecondTime / 1000);
                    hour = Seconds / 1000 * 60 * 60 % 24;
                    Minutes = Seconds / 60;
                    Seconds = Seconds % 60;

                    prefManager.setMLTime(String.valueOf(MillisecondTime));
                    //prefManager.setHourTime(String.valueOf(hour));
                    //prefManager.setMinutesTime(String.valueOf(Minutes));
                    //prefManager.setSecondTime(String.valueOf(Seconds));
                    prefManager.setStartTime(String.valueOf(StartTime));
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            String totalTime = String.format("%02d", hour) + ":" + String.format("%02d", Minutes) + ":" + String.format("%02d", Seconds);
                            prefManager.setHandlerTime(totalTime);
                            tvStartTime.setText(prefManager.getHandlerTime());
                        }
                    });
                    *//*new Handler(Looper.getMainLooper()).post(() -> tvStartTime.setText(String.format("%02d", hour) + ":"
                        + String.format("%02d", Minutes) + ":" + String.format("%02d", Seconds)));*//*
                    handler.postDelayed(this, 0);
                }else {
                    //MillisecondTime = SystemClock.uptimeMillis() - Long.parseLong(prefManager.getStartTime());
                    MillisecondTime = Long.parseLong(prefManager.getMLTime()) - Long.parseLong(prefManager.getStartTime());
                    prefManager.setMLTime(String.valueOf(MillisecondTime));
                    //UpdateTime = TimeBuff + MillisecondTime;
                    Seconds = (int) (MillisecondTime / 1000);
                    hour = Seconds / 1000 * 60 * 60 % 24;
                    Minutes = Seconds / 60;
                    Seconds = Seconds % 60;

                    //MillisecondTime = SystemClock.uptimeMillis() - Long.parseLong(prefManager.getStartTime());
                    //prefManager.setMLTime(String.valueOf(MillisecondTime));
//
                    //Seconds = (int) (Long.parseLong(prefManager.getMLTime()) / 1000);
                    //Seconds = Integer.parseInt(prefManager.getSecondTime()) % 60;
                    //prefManager.setSecondTime(String.valueOf(Seconds));
//
                    //hour = Integer.parseInt(prefManager.getSecondTime()) / 1000 * 60 * 60 % 24;
                    //prefManager.setHourTime(String.valueOf(hour));
//
                    //Minutes = Integer.parseInt(prefManager.getSecondTime()) / 60;
                    //prefManager.setMinutesTime(String.valueOf(Minutes));

                    //prefManager.setStartTime(String.valueOf(StartTime));
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            String totalTime = String.format("%02d", hour) + ":" + String.format("%02d", Minutes) + ":" + String.format("%02d", Seconds);
                            prefManager.setHandlerTime(totalTime);
                            tvStartTime.setText(prefManager.getHandlerTime());
                        }
                    });
                    *//*new Handler(Looper.getMainLooper()).post(() -> tvStartTime.setText(String.format("%02d", hour) + ":"
                        + String.format("%02d", Minutes) + ":" + String.format("%02d", Seconds)));*//*
                    handler.postDelayed(this, 0);
                }*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void rotationPdf() {
        try {
            if (rotation == 90) {
                rotation = 180;
                pdfViewPager.setRotation(90);
                //webView.setRotation(90);
                //pdfView.setRotation(90);
            } else if (rotation == 180) {
                rotation = 270;
                pdfViewPager.setRotation(180);
                //webView.setRotation(180);
                //pdfView.setRotation(180);
            } else if (rotation == 270) {
                rotation = 0;
                pdfViewPager.setRotation(270);
                //webView.setRotation(270);
                //pdfView.setRotation(270);
            } else {
                rotation = 90;
                pdfViewPager.setRotation(0);
                //webView.setRotation(0);
                //pdfView.setRotation(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAppExitMessage() {
        try {
            AppExitFragment a = new AppExitFragment(this::closeApp);
            a.show(getSupportFragmentManager(), "bottom_sheet");
        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    private void closeApp() {
        try {
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    private void showAppLogoutMessage() {
        try {
            LogoutFragment logoutFragment = new LogoutFragment(() -> {
                prefManager.setLoggedIn("False");
                startActivity(new Intent(DashboardActivityNew.this, LoginActivity.class));
                finish();
            });
            logoutFragment.show(getSupportFragmentManager(), "bottom_sheet");
        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    private void deleteFileCreateFolder() {
        try {
            File dir = new File(DashboardActivityNew.this.getExternalFilesDir(null) + "/BhansaliTechno");
            if (dir.isDirectory()) {
                String[] children = dir.list();
                for (String child : children) {
                    new File(dir, child).delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteEmptyFolder(File rootFolder) {
        if (!rootFolder.isDirectory()) return;

        File[] childFiles = rootFolder.listFiles();
        if (childFiles == null) return;
        if (childFiles.length == 0) {
            rootFolder.delete();
        } else {
            for (File childFile : childFiles) {
                deleteEmptyFolder(childFile);
            }
        }
    }

    private void apiJobNoList() {
        try {
            if (isInternet()) {
                ProgressDialog p = new ProgressDialog(DashboardActivityNew.this);
                p.setCancelable(false);
                p.setMessage("Loading...");
                p.show();

                jobNoList.clear();

                HashMap<String, RequestBody> map = new HashMap<>();
                map.put("EmpId", NetworkUtils.createPartFromString(prefManager.getUserName()));

                new ApiUtil(this, API_JOB_NO, map, null, new OnResponse() {
                    @Override
                    public void onSuccess(JSONArray jsonArray) {
                        try {
                            if (p.isShowing()) {
                                p.dismiss();
                            }
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String JobListId = object.has("JobListId") ? object.getString("JobListId") : NULL;
                                    String JobNo = object.has("JobNo") ? object.getString("JobNo").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String Dt = object.has("Dt") ? object.getString("Dt").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String EmployeeId = object.has("EmployeeId") ? object.getString("EmployeeId").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String ProcessId = object.has("ProcessId") ? object.getString("ProcessId").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String ItmId = object.has("ItmId") ? object.getString("ItmId").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String StartDateTime = object.has("StartDateTime") ? object.getString("StartDateTime").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String EndDateTime = object.has("EndDateTime") ? object.getString("EndDateTime").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String CycleTime = object.has("CycleTime") ? object.getString("CycleTime").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String Qty = object.has("Qty") ? object.getString("Qty").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String Remarks = object.has("Remarks") ? object.getString("Remarks").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String InsertedOn = object.has("InsertedOn") ? object.getString("InsertedOn").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String LastUpdatedOn = object.has("LastUpdatedOn") ? object.getString("LastUpdatedOn").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String InsertedByUserId = object.has("InsertedByUserId") ? object.getString("InsertedByUserId").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String LastUpdatedByUserId = object.has("LastUpdatedByUserId") ? object.getString("LastUpdatedByUserId").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String EmployeeName = object.has("EmployeeName") ? object.getString("EmployeeName").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String ProcessName = object.has("ProcessName") ? object.getString("ProcessName").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String ItmName = object.has("ItmName") ? object.getString("ItmName").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String ShopName = object.has("ShopName") ? object.getString("ShopName").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String MachineName = object.has("MachineName") ? object.getString("MachineName").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;

                                    //TextLists model = new TextLists(JobListId, JobNo);
                                    JobNoModel noModel = new JobNoModel(JobListId, JobNo, Dt, EmployeeId, ProcessId, ItmId, StartDateTime, EndDateTime,
                                            CycleTime, Qty, Remarks, InsertedOn, LastUpdatedOn, InsertedByUserId, LastUpdatedByUserId, EmployeeName, ProcessName,
                                            ItmName, ShopName, MachineName, "");
                                    jobNoList.add(noModel);
                                }
                            }
                        } catch (JSONException e) {
                            if (p.isShowing()) {
                                p.dismiss();
                            }
                            e.printStackTrace();
                            showErrorMessage(e.getLocalizedMessage());
                            FirebaseCrashlytics.getInstance().recordException(e);
                        }
                    }

                    @Override
                    public void on209(String message) {
                        if (p.isShowing()) {
                            p.dismiss();
                        }
                        showErrorMessage(message);
                    }

                    @Override
                    public void onNullResponse() {
                        if (p.isShowing()) {
                            p.dismiss();
                        }
                        showErrorMessage("Null Response");
                    }

                    @Override
                    public void onExceptionFired(String error) {
                        if (p.isShowing()) {
                            p.dismiss();
                        }
                        showErrorMessage(error);
                    }

                    @Override
                    public void on400(int responseCode) {
                        if (p.isShowing()) {
                            p.dismiss();
                        }
                        showErrorMessage(String.valueOf(responseCode));
                    }

                    @Override
                    public void onFailure(@NonNull Throwable t) {
                        if (p.isShowing()) {
                            p.dismiss();
                        }
                        showErrorMessage(t.getLocalizedMessage());
                    }
                }).request();
            } else {
                showNoInternet();
            }
        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    private void funJobNo() {
        try {
            if (jobNoList.size() > 0) {
                @SuppressLint({"SetTextI18n", "DefaultLocale"})
                TextListFragmentWithFilter fragment = new TextListFragmentWithFilter(getApplicationContext(), jobNoList, (textListId, textList) -> {
                    tvJobNo.setError(null);
                    tvJobNo.setText(textList);
                    selectedJobId = textListId;
                    selectedJobName = textList;
                    prefManager.setJobNo(textList);
                    prefManager.setJobId(textListId);

                    for (int i = 0; i < jobNoList.size(); i++) {
                        if (jobNoList.get(i).getJobListId().equalsIgnoreCase(textListId)) {
                            tvShopName.setText("Shop : " + jobNoList.get(i).getShopName());
                            tvItemCodeShow.setText("Shop : " + jobNoList.get(i).getShopName());
                            tvGroupShow.setText("Machine : " + jobNoList.get(i).getMachineName());
                            tvMachineName.setText("Machine : " + jobNoList.get(i).getMachineName());
                            tvDrawingNoShow.setText("Process : " + jobNoList.get(i).getProcessName());
                            tvProcessName.setText("Process : " + jobNoList.get(i).getProcessName());

                            tvItemCode.setText(jobNoList.get(i).getItmName());
                            tvPlanQty.setText(String.format("%.0f", Double.parseDouble(jobNoList.get(i).getQty())));

                            prefManager.setShop(jobNoList.get(i).getShopName());
                            prefManager.setMachine(jobNoList.get(i).getMachineName());
                            prefManager.setProcess(jobNoList.get(i).getProcessName());
                            prefManager.setItmName(jobNoList.get(i).getItmName());
                            prefManager.setPlanQty(String.format("%.0f", Double.parseDouble(jobNoList.get(i).getQty())));
                        }
                    }
                });
                fragment.show(getSupportFragmentManager(), "LIST_JOB_NO");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showErrorMessage(String message) {
        final MessageFragment m = new MessageFragment(message);
        new Handler(Looper.myLooper()).post(() -> m.show(DashboardActivityNew.this.getSupportFragmentManager(), KEY_BTM_SHT));
    }

    private void showNoInternet() {
        final NoInternetFragment n = new NoInternetFragment(() -> {

        });
        new Handler(Looper.myLooper()).post(() -> n.show(DashboardActivityNew.this.getSupportFragmentManager(), KEY_BTM_SHT));
    }

    private void apiCheckStartJob() {
        try {
            if (isInternet()) {
                checkJobNoList.clear();

                ProgressDialog p = new ProgressDialog(DashboardActivityNew.this);
                p.setCancelable(false);
                p.setMessage("Loading...");
                p.show();

                HashMap<String, RequestBody> map = new HashMap<>();
                map.put("JobListId", NetworkUtils.createPartFromString(prefManager.getJobId()));
                map.put("EmpId", NetworkUtils.createPartFromString(prefManager.getUserName()));
                map.put("UserId", NetworkUtils.createPartFromString(prefManager.getUserId()));

                new ApiUtil(this, API_CHECK_JOB_START, map, null, new OnResponse() {
                    @SuppressLint({"DefaultLocale", "SetTextI18n"})
                    @Override
                    public void onSuccess(JSONArray jsonArray) {
                        try {
                            if (p.isShowing()) {
                                p.dismiss();
                            }
                            checkJobStartOrNot = "True";

                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String JobListStartStopId = object.has("JobListStartStopId") ? object.getString("JobListStartStopId") : NULL;
                                    String JobListId = object.has("JobListId") ? object.getString("JobListId") : NULL;
                                    String ActivityByEmployeeId = object.has("ActivityByEmployeeId") ? object.getString("ActivityByEmployeeId").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String ProcessId = object.has("ProcessId") ? object.getString("ProcessId").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String ItmId = object.has("ItmId") ? object.getString("ItmId").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String StartDateTime = object.has("StartDateTime") ? object.getString("StartDateTime").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String EndDateTime = object.has("EndDateTime") ? object.getString("EndDateTime").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String Qty = object.has("Qty") ? object.getString("Qty").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String Remarks = object.has("Remarks") ? object.getString("Remarks").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String InsertedOn = object.has("InsertedOn") ? object.getString("InsertedOn").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String LastUpdatedOn = object.has("LastUpdatedOn") ? object.getString("LastUpdatedOn").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String InsertedByUserId = object.has("InsertedByUserId") ? object.getString("InsertedByUserId").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String LastUpdatedByUserId = object.has("LastUpdatedByUserId") ? object.getString("LastUpdatedByUserId").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String PDFFile = object.has("PDFFile") ? object.getString("PDFFile").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;

                                    String JobNo = object.has("JobNo") ? object.getString("JobNo").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String EmployeeName = object.has("EmployeeName") ? object.getString("EmployeeName").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String ProcessName = object.has("ProcessName") ? object.getString("ProcessName").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String ItmName = object.has("ItmName") ? object.getString("ItmName").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String ShopName = object.has("ShopName") ? object.getString("ShopName").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String MachineName = object.has("MachineName") ? object.getString("MachineName").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String PlanQty = object.has("PlanQty") ? object.getString("PlanQty").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;

                                    StopJobNoModel stopJobNoModel = new StopJobNoModel(JobListStartStopId, JobListId, ActivityByEmployeeId, ProcessId,
                                            ItmId, StartDateTime, EndDateTime, Qty, Remarks, InsertedOn, LastUpdatedOn, InsertedByUserId,
                                            LastUpdatedByUserId, PDFFile, JobNo, EmployeeName, ProcessName, ItmName, ShopName, MachineName,PlanQty);
                                    checkJobNoList.add(stopJobNoModel);
                                }
                            }

                            tvJobNo.setText(checkJobNoList.get(0).getJobNo());
                            tvShopName.setText("Shop : " + checkJobNoList.get(0).getShopName());
                            tvItemCodeShow.setText("Shop : " + checkJobNoList.get(0).getShopName());
                            tvGroupShow.setText("Machine : " + checkJobNoList.get(0).getMachineName());
                            tvMachineName.setText("Machine : " + checkJobNoList.get(0).getMachineName());
                            tvDrawingNoShow.setText("Process : " + checkJobNoList.get(0).getProcessName());
                            tvProcessName.setText("Process : " + checkJobNoList.get(0).getProcessName());
                            tvItemCode.setText(checkJobNoList.get(0).getItmName());
                            if (!checkJobNoList.get(0).getPlanQty().equalsIgnoreCase("")){
                                //tvPlanQty.setText(String.format("%.0f", Double.parseDouble(checkJobNoList.get(0).getQty())));
                                tvPlanQty.setText(String.format("%.0f", Double.parseDouble(checkJobNoList.get(0).getPlanQty())));
                            }

                            //tvJobNo.setText(prefManager.getJobNo());
                            //tvShopName.setText("Shop : " + prefManager.getShop());
                            //tvItemCodeShow.setText("Shop : " + prefManager.getShop());
                            //tvGroupShow.setText("Machine : " + prefManager.getMachine());
                            //tvMachineName.setText("Machine : " + prefManager.getMachine());
                            //tvDrawingNoShow.setText("Process : " + prefManager.getProcess());
                            //tvProcessName.setText("Process : " + prefManager.getProcess());
                            //tvItemCode.setText(prefManager.getItmName());
                            //tvPlanQty.setText(prefManager.getPlanQty());

                            tvJobNo.setEnabled(false);
                            tvItemCode.setEnabled(false);
                            tvPlanQty.setEnabled(false);
                            lylFilter.setVisibility(View.GONE);
                            imgStartBtn.setVisibility(View.GONE);
                            lylStop.setVisibility(View.GONE);
                            lylDisplayData.setVisibility(View.VISIBLE);
                            pdfView.setVisibility(View.GONE);
                            ltLogout.setVisibility(View.GONE);

                            String urlPdf = checkJobNoList.get(0).getPDFFile();
                            mainUrl = urlPdf.replace(KEY_U0027, KEY_AFOSTROPHE).replace(KEY_U0026, KEY_EMPER);
                            file = mainUrl.substring(mainUrl.lastIndexOf('/') + 1);
                            openPDFView();
                        } catch (JSONException e) {
                            if (p.isShowing()) {
                                p.dismiss();
                            }
                            e.printStackTrace();
                            showErrorMessage(e.getLocalizedMessage());
                            FirebaseCrashlytics.getInstance().recordException(e);
                        }
                    }

                    @Override
                    public void on209(String message) {
                        if (p.isShowing()) {
                            p.dismiss();
                        }
                        checkJobStartOrNot = "False";
                        //showErrorMessage(message);
                    }

                    @Override
                    public void onNullResponse() {
                        if (p.isShowing()) {
                            p.dismiss();
                        }
                        //showErrorMessage("Null Response");
                    }

                    @Override
                    public void onExceptionFired(String error) {
                        if (p.isShowing()) {
                            p.dismiss();
                        }
                        //showErrorMessage(error);
                    }

                    @Override
                    public void on400(int responseCode) {
                        if (p.isShowing()) {
                            p.dismiss();
                        }
                        //showErrorMessage(String.valueOf(responseCode));
                    }

                    @Override
                    public void onFailure(@NonNull Throwable t) {
                        if (p.isShowing()) {
                            p.dismiss();
                        }
                        //showErrorMessage(t.getLocalizedMessage());
                    }
                }).request();
            } else {
                showNoInternet();
            }
        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    private void apiStartJob() {
        try {
            if (isInternet()) {
                ProgressDialog p = new ProgressDialog(DashboardActivityNew.this);
                p.setCancelable(false);
                p.setMessage("Loading...");
                p.show();

                startJobNoList.clear();

                HashMap<String, RequestBody> map = new HashMap<>();
                map.put("JobListId", NetworkUtils.createPartFromString(prefManager.getJobId()));
                map.put("EmpId", NetworkUtils.createPartFromString(prefManager.getUserName()));
                map.put("UserId", NetworkUtils.createPartFromString(prefManager.getUserId()));

                new ApiUtil(this, API_START_JOB, map, null, new OnResponse() {
                    @Override
                    public void onSuccess(JSONArray jsonArray) {
                        try {
                            if (p.isShowing()) {
                                p.dismiss();
                            }
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String JobListId = object.has("JobListId") ? object.getString("JobListId") : NULL;
                                    String JobNo = object.has("JobNo") ? object.getString("JobNo").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String Dt = object.has("Dt") ? object.getString("Dt").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String EmployeeId = object.has("EmployeeId") ? object.getString("EmployeeId").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String ProcessId = object.has("ProcessId") ? object.getString("ProcessId").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String ItmId = object.has("ItmId") ? object.getString("ItmId").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String StartDateTime = object.has("StartDateTime") ? object.getString("StartDateTime").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String EndDateTime = object.has("EndDateTime") ? object.getString("EndDateTime").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String CycleTime = object.has("CycleTime") ? object.getString("CycleTime").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String Qty = object.has("Qty") ? object.getString("Qty").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String Remarks = object.has("Remarks") ? object.getString("Remarks").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String InsertedOn = object.has("InsertedOn") ? object.getString("InsertedOn").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String LastUpdatedOn = object.has("LastUpdatedOn") ? object.getString("LastUpdatedOn").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String InsertedByUserId = object.has("InsertedByUserId") ? object.getString("InsertedByUserId").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String LastUpdatedByUserId = object.has("LastUpdatedByUserId") ? object.getString("LastUpdatedByUserId").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String EmployeeName = object.has("EmployeeName") ? object.getString("EmployeeName").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String ProcessName = object.has("ProcessName") ? object.getString("ProcessName").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String ItmName = object.has("ItmName") ? object.getString("ItmName").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String ShopName = object.has("ShopName") ? object.getString("ShopName").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String MachineName = object.has("MachineName") ? object.getString("MachineName").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String PDFFile = object.has("PDFFile") ? object.getString("PDFFile").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;

                                    //TextLists model = new TextLists(JobListId, JobNo);
                                    JobNoModel noModel = new JobNoModel(JobListId, JobNo, Dt, EmployeeId, ProcessId, ItmId, StartDateTime, EndDateTime,
                                            CycleTime, Qty, Remarks, InsertedOn, LastUpdatedOn, InsertedByUserId, LastUpdatedByUserId, EmployeeName, ProcessName,
                                            ItmName, ShopName, MachineName, PDFFile);
                                    startJobNoList.add(noModel);
                                }

                                tvJobNo.setEnabled(false);
                                tvItemCode.setEnabled(false);
                                tvPlanQty.setEnabled(false);
                                lylFilter.setVisibility(View.GONE);
                                imgStartBtn.setVisibility(View.GONE);
                                lylStop.setVisibility(View.GONE);
                                lylDisplayData.setVisibility(View.VISIBLE);
                                pdfView.setVisibility(View.GONE);
                                ltLogout.setVisibility(View.GONE);

                                String urlPdf = startJobNoList.get(0).getPdfFile();
                                mainUrl = urlPdf.replace(KEY_U0027, KEY_AFOSTROPHE).replace(KEY_U0026, KEY_EMPER);
                                file = mainUrl.substring(mainUrl.lastIndexOf('/') + 1);
                                openPDFView();

                                //String urlPdf = "https://drive.google.com/viewerng/viewer?embedded=true&url=" +startJobNoList.get(0).getPdfFile();
                                //webView(urlPdf);
                            }
                        } catch (JSONException e) {
                            if (p.isShowing()) {
                                p.dismiss();
                            }
                            e.printStackTrace();
                            showErrorMessage(e.getLocalizedMessage());
                            FirebaseCrashlytics.getInstance().recordException(e);
                        }
                    }

                    @Override
                    public void on209(String message) {
                        if (p.isShowing()) {
                            p.dismiss();
                        }
                        showErrorMessage(message);
                    }

                    @Override
                    public void onNullResponse() {
                        if (p.isShowing()) {
                            p.dismiss();
                        }
                        showErrorMessage("Null Response");
                    }

                    @Override
                    public void onExceptionFired(String error) {
                        if (p.isShowing()) {
                            p.dismiss();
                        }
                        showErrorMessage(error);
                    }

                    @Override
                    public void on400(int responseCode) {
                        if (p.isShowing()) {
                            p.dismiss();
                        }
                        showErrorMessage(String.valueOf(responseCode));
                    }

                    @Override
                    public void onFailure(@NonNull Throwable t) {
                        if (p.isShowing()) {
                            p.dismiss();
                        }
                        showErrorMessage(t.getLocalizedMessage());
                    }
                }).request();
            } else {
                showNoInternet();
            }
        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    private void apiStopJob(String qty, String remarks) {
        try {
            if (isInternet()) {
                stopJobNoList.clear();

                ProgressDialog p = new ProgressDialog(DashboardActivityNew.this);
                p.setCancelable(false);
                p.setMessage("Loading...");
                p.show();

                HashMap<String, RequestBody> map = new HashMap<>();
                map.put("JobListId", NetworkUtils.createPartFromString(prefManager.getJobId()));
                map.put("EmpId", NetworkUtils.createPartFromString(prefManager.getUserName()));
                map.put("UserId", NetworkUtils.createPartFromString(prefManager.getUserId()));
                map.put("Qty", NetworkUtils.createPartFromString(qty));
                map.put("Remarks", NetworkUtils.createPartFromString(remarks));

                new ApiUtil(this, API_STOP_JOB, map, null, new OnResponse() {
                    @Override
                    public void onSuccess(JSONArray jsonArray) {
                        try {
                            /*if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String JobListStartStopId = object.has("JobListStartStopId") ? object.getString("JobListStartStopId") : NULL;
                                    String JobListId = object.has("JobListId") ? object.getString("JobListId") : NULL;
                                    String ActivityByEmployeeId = object.has("ActivityByEmployeeId") ? object.getString("ActivityByEmployeeId").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String ProcessId = object.has("ProcessId") ? object.getString("ProcessId").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String ItmId = object.has("ItmId") ? object.getString("ItmId").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String StartDateTime = object.has("StartDateTime") ? object.getString("StartDateTime").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String EndDateTime = object.has("EndDateTime") ? object.getString("EndDateTime").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String Qty = object.has("Qty") ? object.getString("Qty").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String Remarks = object.has("Remarks") ? object.getString("Remarks").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String InsertedOn = object.has("InsertedOn") ? object.getString("InsertedOn").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String LastUpdatedOn = object.has("LastUpdatedOn") ? object.getString("LastUpdatedOn").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String InsertedByUserId = object.has("InsertedByUserId") ? object.getString("InsertedByUserId").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String LastUpdatedByUserId = object.has("LastUpdatedByUserId") ? object.getString("LastUpdatedByUserId").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String PDFFile = object.has("PDFFile") ? object.getString("PDFFile").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;

                                    //String JobNo = object.has("JobNo") ? object.getString("JobNo").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    //String EmployeeName = object.has("EmployeeName") ? object.getString("EmployeeName").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    //String ProcessName = object.has("ProcessName") ? object.getString("ProcessName").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    //String ItmName = object.has("ItmName") ? object.getString("ItmName").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    //String ShopName = object.has("ShopName") ? object.getString("ShopName").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    //String MachineName = object.has("MachineName") ? object.getString("MachineName").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;

                                    StopJobNoModel stopJobNoModel = new StopJobNoModel(JobListStartStopId, JobListId, ActivityByEmployeeId, ProcessId,
                                            ItmId, StartDateTime, EndDateTime, Qty, Remarks, InsertedOn, LastUpdatedOn, InsertedByUserId,
                                            LastUpdatedByUserId, PDFFile, "", "", "", "", "", "", "");
                                    stopJobNoList.add(stopJobNoModel);
                                }
                                *//*for (int i = 0; i < jobNoList.size(); i++) {
                                    if (prefManager.getJobId().equalsIgnoreCase(stopJobNoList.get(0).getJobListId())) {
                                        totalTimeGet(stopJobNoList.get(0).getStartDateTime(), stopJobNoList.get(0).getEndDateTime());
                                    }
                                }*//*
                                //totalTimeGet(stopJobNoList.get(0).getStartDateTime(), stopJobNoList.get(0).getEndDateTime());
                            }*/

                            //deleteFileCreateFolder();
                            //deleteEmptyFolder(new File(DashboardActivityNew.this.getExternalFilesDir(null) + "/BhansaliTechno"));

                            alertDialog.dismiss();

                            if (p.isShowing()) {
                                p.dismiss();
                            }

                            tvJobNo.setText("");
                            tvItemCode.setText("");
                            tvPlanQty.setText("");
                            tvJobNo.setEnabled(true);
                            tvItemCode.setEnabled(true);
                            tvPlanQty.setEnabled(true);
                            tvProductionQty.setEnabled(false);
                            Objects.requireNonNull(tvProductionQty.getText()).clear();
                            imgStartBtn.setVisibility(View.VISIBLE);
                            lylStop.setVisibility(View.GONE);
                            lylDisplayData.setVisibility(View.GONE);
                            lylSwipe.setVisibility(View.GONE);
                            lylPdfShow.setVisibility(View.GONE);
                            pdfViewPager.setVisibility(View.GONE);
                            webView.setVisibility(View.GONE);
                            pdfView.setVisibility(View.GONE);
                            handler.removeCallbacksAndMessages(null);
                            lylFilter.setVisibility(View.VISIBLE);
                            imgRotation.setVisibility(View.GONE);
                            tvRotation.setVisibility(View.GONE);
                            ltLogout.setVisibility(View.VISIBLE);

                            prefManager.setJobId("");
                            prefManager.setJobNo("");
                            prefManager.setShop("");
                            prefManager.setMachine("");
                            prefManager.setProcess("");
                            prefManager.setItmName("");
                            prefManager.setPlanQty("");
                            prefManager.setHandlerTime("");
                            prefManager.setStartTime("");

                            Toast.makeText(DashboardActivityNew.this, "Job Done Success.", Toast.LENGTH_SHORT).show();

                            apiJobNoList();
                        } catch (Exception e) {
                            if (p.isShowing()) {
                                p.dismiss();
                            }
                            alertDialog.dismiss();
                            e.printStackTrace();
                            showErrorMessage(e.getLocalizedMessage());
                            FirebaseCrashlytics.getInstance().recordException(e);
                        }
                    }

                    @Override
                    public void on209(String message) {
                        if (p.isShowing()) {
                            p.dismiss();
                        }
                        showErrorMessage(message);
                    }

                    @Override
                    public void onNullResponse() {
                        if (p.isShowing()) {
                            p.dismiss();
                        }
                        showErrorMessage("Null Response");
                    }

                    @Override
                    public void onExceptionFired(String error) {
                        if (p.isShowing()) {
                            p.dismiss();
                        }
                        showErrorMessage(error);
                    }

                    @Override
                    public void on400(int responseCode) {
                        if (p.isShowing()) {
                            p.dismiss();
                        }
                        showErrorMessage(String.valueOf(responseCode));
                    }

                    @Override
                    public void onFailure(@NonNull Throwable t) {
                        if (p.isShowing()) {
                            p.dismiss();
                        }
                        showErrorMessage(t.getLocalizedMessage());
                    }
                }).request();
            } else {
                showNoInternet();
            }
        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    private void stopFun() {
        View confirmDialog = LayoutInflater.from(DashboardActivityNew.this).inflate(R.layout.password_dlg, null);
        AppCompatButton btnYes = confirmDialog.findViewById(R.id.btnYes);
        AppCompatButton btnNo = confirmDialog.findViewById(R.id.btnNo);

        EditText edtProductionQty = confirmDialog.findViewById(R.id.edtProductionQty);
        EditText edtRemarks = confirmDialog.findViewById(R.id.edtRemarks);

        final android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(DashboardActivityNew.this);
        alert.setView(confirmDialog);
        //alert.setTitle("Password");
        //Creating an alert dialog
        alertDialog = alert.create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        btnYes.setOnClickListener(view -> {
            if (edtProductionQty.getText().toString().isEmpty()) {
                Toast.makeText(DashboardActivityNew.this, "Please enter production quantity.", Toast.LENGTH_SHORT).show();
            } else if (Double.parseDouble(edtProductionQty.getText().toString().trim()) > Double.parseDouble(tvPlanQty.getText().toString().trim())) {
                Toast.makeText(DashboardActivityNew.this, "Quantity can't be greater than available quantity.", Toast.LENGTH_SHORT).show();
            } else {
                apiStopJob(edtProductionQty.getText().toString().trim(), edtRemarks.getText().toString().trim());
            }
        });
        btnNo.setOnClickListener(v1 -> alertDialog.dismiss());
    }

    private void apiVersion() {
        try {
            if (isInternet()) {
                ProgressDialog p = new ProgressDialog(DashboardActivityNew.this);
                p.setCancelable(false);
                p.setMessage("Loading...");
                p.show();

                new ApiUtil(DashboardActivityNew.this, API_VERSION, null, null, new OnResponse() {
                    @Override
                    public void onSuccess(JSONArray array) {
                        try {
                            if (p.isShowing()) {
                                p.dismiss();
                            }
                            if (array.length() > 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    try {
                                        //Getting Version From API
                                        String version = object.getString("FacetText").replace(KEY_U0027, KEY_AFOSTROPHE).replace(KEY_U0026, KEY_EMPER);

                                        //Getting Version From Application
                                        PackageInfo pInfo = DashboardActivityNew.this.getPackageManager().getPackageInfo(getPackageName(), 0);
                                        String appVersion = pInfo.versionName;

                                        if (!appVersion.equalsIgnoreCase(version)) {

                                            AlertDialog.Builder a = new AlertDialog.Builder(DashboardActivityNew.this);
                                            //a.setIcon(getResources().getDrawable(R.mipmap.ic_launcher));
                                            a.setTitle("Here’s an update on your app.");
                                            a.setMessage("We have updated new app in playstore to make a better experience for you. Please download it from Playstore.");
                                            //a.setTitle("DN Construction Inventory Update Found !!");
                                            //a.setMessage("Please, update app from play store to get latest version of app.");
                                            a.setCancelable(false);

                                            a.setPositiveButton("OK", (dialog, which) -> {
                                                try {
                                                    DashboardActivityNew.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + DashboardActivityNew.this.getPackageName())));
                                                } catch (ActivityNotFoundException e) {
                                                    DashboardActivityNew.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + DashboardActivityNew.this.getPackageName())));
                                                    FirebaseCrashlytics.getInstance().recordException(e);
                                                }
                                            });

                                        /*a.setNegativeButton("Keep This Version", (dialog, i1) -> {
                                            dialog.cancel();
                                        });*/

                                            AlertDialog alertDialog = a.create();
                                            alertDialog.setCanceledOnTouchOutside(false);
                                            alertDialog.setCancelable(false);
                                            alertDialog.show();
                                        }
                                    } catch (PackageManager.NameNotFoundException e) {
                                        e.printStackTrace();
                                        FirebaseCrashlytics.getInstance().recordException(e);
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            FirebaseCrashlytics.getInstance().recordException(e);
                        }
                    }

                    @Override
                    public void on209(String message) {
                        if (p.isShowing()) {
                            p.dismiss();
                        }
                        //showErrorMessage(message);
                    }

                    @Override
                    public void onNullResponse() {
                        if (p.isShowing()) {
                            p.dismiss();
                        }
                        //showErrorMessage("Null Response");
                    }

                    @Override
                    public void onExceptionFired(String error) {
                        if (p.isShowing()) {
                            p.dismiss();
                        }
                        //showErrorMessage(error);
                    }

                    @Override
                    public void on400(int responseCode) {
                        if (p.isShowing()) {
                            p.dismiss();
                        }
                        //showErrorMessage(String.valueOf(responseCode));
                    }

                    @Override
                    public void onFailure(@NonNull Throwable t) {
                        if (p.isShowing()) {
                            p.dismiss();
                        }
                        //showErrorMessage(t.getLocalizedMessage());
                    }
                }).request();
            } else {
                showNoInternet();
            }
        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    private void totalTimeGet(String startTime, String stopTime) {
        try {
            String one = Utils.ConvertDateFormat(startTime, "dd-MM-yyyy hh:mm:ss", "dd-MM-yyyy hh:mm:ss");
            String two = Utils.ConvertDateFormat(stopTime, "dd-MM-yyyy hh:mm:ss", "dd-MM-yyyy hh:mm:ss");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
            Date date1 = simpleDateFormat.parse(one);//13:04:53
            Date date2 = simpleDateFormat.parse(two);//12:33:40"

            //long difference = date2.getTime() - date1.getTime();
            //int days = (int) (difference / (1000 * 60 * 60 * 24));
            //int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
            //int min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
            //hours = (hours < 0 ? -hours : hours);
            //Log.i("======= Hours", " :: " + hours);

            long millse = date2.getTime() - date1.getTime();
            long mills = Math.abs(millse);
            int Hours = (int) (mills / (1000 * 60 * 60));
            int Mins = (int) (mills / (1000 * 60)) % 60;
            long Secs = (int) (mills / 1000) % 60;

            Toast.makeText(this, "Total Job Time : " + String.valueOf(Hours) + "hour" + String.valueOf(Mins) + "Mins", Toast.LENGTH_SHORT).show();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
