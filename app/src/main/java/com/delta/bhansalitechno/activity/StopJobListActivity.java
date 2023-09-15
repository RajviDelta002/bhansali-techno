package com.delta.bhansalitechno.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.delta.bhansalitechno.R;
import com.delta.bhansalitechno.adapter.ListPopupWindowAdapter;
import com.delta.bhansalitechno.adapter.RadioAdapter;
import com.delta.bhansalitechno.adapter.StopJobListAdapter;
import com.delta.bhansalitechno.api.ApiUtil;
import com.delta.bhansalitechno.bottomsheets.AppExitFragment;
import com.delta.bhansalitechno.bottomsheets.LogoutFragment;
import com.delta.bhansalitechno.bottomsheets.MessageFragment;
import com.delta.bhansalitechno.bottomsheets.NoInternetFragment;
import com.delta.bhansalitechno.fargments.TextListFragmentWithFilter;
import com.delta.bhansalitechno.interfaces.OnResponse;
import com.delta.bhansalitechno.interfaces.RecyclerViewClickListener;
import com.delta.bhansalitechno.model.JobNoModel;
import com.delta.bhansalitechno.model.RadioModel;
import com.delta.bhansalitechno.model.StopJobNoModel;
import com.delta.bhansalitechno.model.TextListModel;
import com.delta.bhansalitechno.utils.ConnectionDetector;
import com.delta.bhansalitechno.utils.NetworkUtils;
import com.delta.bhansalitechno.utils.PrefManager;
import com.delta.bhansalitechno.utils.Utils;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import es.voghdev.pdfviewpager.library.PDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;
import okhttp3.RequestBody;

import static com.delta.bhansalitechno.utils.AppConfig.API_CHECK_JOB_START;
import static com.delta.bhansalitechno.utils.AppConfig.API_JOB_NO;
import static com.delta.bhansalitechno.utils.AppConfig.API_RADIO_BUTTON;
import static com.delta.bhansalitechno.utils.AppConfig.API_START_JOB;
import static com.delta.bhansalitechno.utils.AppConfig.API_STOP_JOB;
import static com.delta.bhansalitechno.utils.AppConfig.API_TEXT_LIST;
import static com.delta.bhansalitechno.utils.AppConfig.KEY_AFOSTROPHE;
import static com.delta.bhansalitechno.utils.AppConfig.KEY_BTM_SHT;
import static com.delta.bhansalitechno.utils.AppConfig.KEY_EMPER;
import static com.delta.bhansalitechno.utils.AppConfig.KEY_U0026;
import static com.delta.bhansalitechno.utils.AppConfig.KEY_U0027;
import static com.delta.bhansalitechno.utils.AppConfig.NULL;

public class StopJobListActivity extends AppCompatActivity implements View.OnClickListener, RecyclerViewClickListener {

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
    private TextView tvHome, tvFilter, tvRotation;
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

    private AlertDialog alertDialog;

    String checkJobStartOrNot = "";

    private AppUpdateManager appUpdateManager;
    private static final int IMMEDIATE_APP_UPDATE_REQ_CODE = 124;

    private RecyclerView rvRadioBtn;
    private RecyclerView rvStopJObList;
    ArrayList<RadioModel> radioList = new ArrayList<>();
    ArrayList<TextListModel> holdReasonList = new ArrayList<>();

    String reasonId;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_stop_job_list);

            appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());
            checkUpdate();

            prefManager = new PrefManager(StopJobListActivity.this);
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

            tvHome = findViewById(R.id.tvHome);
            tvFilter = findViewById(R.id.tvFilter);
            tvRotation = findViewById(R.id.tvRotation);

            rvRadioBtn = findViewById(R.id.rvRadioBtn);
            rvStopJObList = findViewById(R.id.rvStopJObList);

            imgFilter.setOnClickListener(this);
            tvHome.setOnClickListener(this);
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

            //apiJobNoList();

            //apiStartJob();

            apiHoldReasonAPI();
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
                case R.id.tvHome:
                    prefManager.setMachineName("");
                    startActivity(new Intent(StopJobListActivity.this, MachineActivity.class));
                    finish();
                    break;
                case R.id.imgFilter:
                case R.id.tvFilter:
                    /*if (lylFilter.getVisibility() == View.VISIBLE) {
                        lylFilter.setVisibility(View.GONE);
                        //lylDisplayData.setVisibility(View.GONE);
                        tvFilter.setText("Show");
                    } else {
                        lylFilter.setVisibility(View.VISIBLE);
                        //lylDisplayData.setVisibility(View.VISIBLE);
                        tvFilter.setText("Hide");
                    }*/

                    if (rvStopJObList.getVisibility() == View.VISIBLE) {
                        rvStopJObList.setVisibility(View.GONE);
                        tvFilter.setText("Show");
                    } else {
                        rvStopJObList.setVisibility(View.VISIBLE);
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
                        //Toast.makeText(this, "Please select Job No first.", Toast.LENGTH_SHORT).show();
                        showErrorMessage("Please select Job No first.");
                    } else {
                        if (checkJobStartOrNot.equalsIgnoreCase("False")) {
                            //apiStartJobAPICardShow();
                        }
                    }
                    //startBtnFun();
                    //ltLogout.setVisibility(View.GONE);
                    break;
                case R.id.imgStopBtn:
                    stopFun(startJobNoList, 0);
                    /*stopBtnFun();
                    lylFilter.setVisibility(View.VISIBLE);
                    imgRotation.setVisibility(View.GONE);
                    tvRotation.setVisibility(View.GONE);
                    ltLogout.setVisibility(View.VISIBLE);*/
                    //deleteContents(new File(DashboardActivityNew.this.getExternalFilesDir("BhansaliTechno") + "/BhansaliTechno"));
                    //deleteFileCreateFolder();
                    //deleteRecursive(new File(DashboardActivityNew.this.getExternalFilesDir("BhansaliTechno") + "/BhansaliTechno", file));
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

    private void checkUpdate() {
        try {
            com.google.android.play.core.tasks.Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
            appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    startUpdateFlow(appUpdateInfo);
                } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    startUpdateFlow(appUpdateInfo);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startUpdateFlow(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, this, IMMEDIATE_APP_UPDATE_REQ_CODE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    public boolean isInternet() {
        return new ConnectionDetector(StopJobListActivity.this).isInternetConnected();
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
            String pathName = StopJobListActivity.this.getExternalFilesDir("BhansaliTechno").toString();
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
                new StopJobListActivity.DownloadingTask().execute();
            } else {
                webView.setVisibility(View.GONE);
                lylPdfShow.setVisibility(View.VISIBLE);
                pdfViewPager.setVisibility(View.VISIBLE);
                //imgRotation.setVisibility(View.VISIBLE);
                tvRotation.setVisibility(View.VISIBLE);
                pdfViewPager = new PDFViewPager(StopJobListActivity.this, getPdfPath());
                pdfViewPager.setId(R.id.pdfViewPager);
                adapter = new PDFPagerAdapter(StopJobListActivity.this, getPdfPath());
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
                adapter = new PDFPagerAdapter(StopJobListActivity.this, getPdfPath());
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
        File f = new File(StopJobListActivity.this.getExternalFilesDir("BhansaliTechno"), file);
        return f.getAbsolutePath();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pdfViewPager.getAdapter() != null) {
            ((PDFPagerAdapter) pdfViewPager.getAdapter()).close();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onItemClick(View view, int position) {
        try {
            switch (view.getId()) {
                case R.id.btnHold:
                    holdFun(startJobNoList, position);
                    break;
                case R.id.btnStop:
                    stopFun(startJobNoList, position);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*@Override
    public void onItemClick(View view, int position) {
        try {
            if (view.getId() == R.id.rdBtn){
                tvJobNo.setEnabled(false);
                tvItemCode.setEnabled(false);
                tvPlanQty.setEnabled(false);
                tvHome.setVisibility(View.GONE);
                lylFilter.setVisibility(View.GONE);
                imgStartBtn.setVisibility(View.GONE);
                lylStop.setVisibility(View.GONE);
                lylDisplayData.setVisibility(View.VISIBLE);
                pdfView.setVisibility(View.GONE);
                ltLogout.setVisibility(View.GONE);

                String urlPdf = radioList.get(position).getPDFFile();
                mainUrl = urlPdf.replace(KEY_U0027, KEY_AFOSTROPHE).replace(KEY_U0026, KEY_EMPER);
                file = mainUrl.substring(mainUrl.lastIndexOf('/') + 1);
                openPDFView();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    @SuppressLint("StaticFieldLeak")
    private class DownloadingTask extends AsyncTask<String, Integer, String> {

        File outputFile = null;
        String fileName = file;
        String storage = StopJobListActivity.this.getExternalFilesDir("BhansaliTechno").toString();
        File folder = new File(storage);
        URL url;

        {
            try {
                url = new URL(mainUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(StopJobListActivity.this, "MalformedURL", Toast.LENGTH_SHORT).show();
            }
        }

        HttpURLConnection c;

        {
            try {
                c = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(StopJobListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(StopJobListActivity.this);
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

                    Toast.makeText(StopJobListActivity.this, "Document Load Successfully", Toast.LENGTH_SHORT).show();
                    webView.setVisibility(View.GONE);
                    pdfViewPager.setVisibility(View.VISIBLE);
                    //imgRotation.setVisibility(View.VISIBLE);
                    tvRotation.setVisibility(View.VISIBLE);
                    pdfViewPager = new PDFViewPager(StopJobListActivity.this, getPdfPath());
                    pdfViewPager.setId(R.id.pdfViewPager);
                    adapter = new PDFPagerAdapter(StopJobListActivity.this, getPdfPath());
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
                    Toast.makeText(StopJobListActivity.this, c.getResponseMessage(), Toast.LENGTH_SHORT).show();
                    //progressDialog.dismiss();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Handler().postDelayed(() -> {
                }, 3000);
                c.disconnect();
                try {
                    Toast.makeText(StopJobListActivity.this, c.getResponseMessage(), Toast.LENGTH_SHORT).show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                progressDialog.dismiss();
            }
            super.onPostExecute(result);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                c.setRequestMethod("GET");
                c.connect();
                if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                } else {
                    if (!folder.exists()) {
                        folder.mkdirs();
                    }
                    outputFile = new File(StopJobListActivity.this.getExternalFilesDir("BhansaliTechno"), fileName);

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
                StopJobListActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(StopJobListActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                //Toast.makeText(StopJobListActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
                prefManager.setMachineCode("");
                prefManager.setMachineNo("");
                prefManager.setMachineId("");
                prefManager.setMachineName("");
                prefManager.setMachine("");
                prefManager.setProcess("");
                prefManager.setShop("");
                prefManager.setJobNo("");
                prefManager.setJobId("");
                prefManager.setItmName("");
                prefManager.setPlanQty("");
                prefManager.setHandlerTime("");
                prefManager.setStartTime("");

                startActivity(new Intent(StopJobListActivity.this, LoginActivity.class));
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
            File dir = new File(StopJobListActivity.this.getExternalFilesDir("BhansaliTechno") + "/BhansaliTechno");
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
                ProgressDialog p = new ProgressDialog(StopJobListActivity.this);
                p.setCancelable(false);
                p.setMessage("Loading...");
                p.show();

                jobNoList.clear();

                HashMap<String, RequestBody> map = new HashMap<>();
                //map.put("EmpId", NetworkUtils.createPartFromString(prefManager.getUserName()));
                map.put("MachineId", NetworkUtils.createPartFromString(prefManager.getMachineId()));

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
                                    String DoneQty = object.has("DoneQty") ? object.getString("DoneQty").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String PendingQty = object.has("PendingQty") ? object.getString("PendingQty").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String RefNo = object.has("RefNo") ? object.getString("RefNo").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String PCNo = object.has("PCNo") ? object.getString("PCNo").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;

                                    String PartNo = object.has("PartNo") ? object.getString("PartNo").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String HubPartNo = object.has("HubPartNo") ? object.getString("HubPartNo").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String Size = object.has("Size") ? object.getString("Size").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String Desc = object.has("Desc") ? object.getString("Desc").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;

                                    String Status = object.has("Status") ? object.getString("Status").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String HoldReasonTextListId = object.has("HoldReasonTextListId") ? object.getString("HoldReasonTextListId").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String HoldReason = object.has("HoldReason") ? object.getString("HoldReason").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String JobStartStopRemarks = object.has("JobStartStopRemarks") ? object.getString("JobStartStopRemarks").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;

                                    //TextLists model = new TextLists(JobListId, JobNo);
                                    JobNoModel noModel = new JobNoModel(JobListId, JobNo, Dt, EmployeeId, ProcessId, ItmId, StartDateTime, EndDateTime,
                                            CycleTime, Qty, Remarks, InsertedOn, LastUpdatedOn, InsertedByUserId, LastUpdatedByUserId, EmployeeName, ProcessName,
                                            ItmName, ShopName, MachineName, "", DoneQty, PendingQty, RefNo, PCNo, PartNo, HubPartNo, Size, Desc, Status, HoldReasonTextListId, HoldReason, JobStartStopRemarks, false);
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
                TextListFragmentWithFilter fragment = new TextListFragmentWithFilter(getApplicationContext(), jobNoList, (textListId, textList, refNo) -> {
                    tvJobNo.setError(null);
                    //tvJobNo.setText(textList + " ( " + refNo + " ) ");
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
                            tvPlanQty.setText(String.format("%.0f", Double.parseDouble(jobNoList.get(i).getPendingQty())));

                            prefManager.setShop(jobNoList.get(i).getShopName());
                            prefManager.setMachine(jobNoList.get(i).getMachineName());
                            prefManager.setProcess(jobNoList.get(i).getProcessName());
                            prefManager.setItmName(jobNoList.get(i).getItmName());
                            prefManager.setPlanQty(String.format("%.0f", Double.parseDouble(jobNoList.get(i).getPendingQty())));
                        }
                    }
                });
                fragment.show(getSupportFragmentManager(), "LIST_JOB_NO");
            } else {
                showErrorMessage("No Record Found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showErrorMessage(String message) {
        final MessageFragment m = new MessageFragment(message);
        new Handler(Looper.myLooper()).post(() -> m.show(StopJobListActivity.this.getSupportFragmentManager(), KEY_BTM_SHT));
    }

    private void showNoInternet() {
        final NoInternetFragment n = new NoInternetFragment(() -> {

        });
        new Handler(Looper.myLooper()).post(() -> n.show(StopJobListActivity.this.getSupportFragmentManager(), KEY_BTM_SHT));
    }

    private void apiCheckStartJob() {
        try {
            if (isInternet()) {
                checkJobNoList.clear();

                ProgressDialog p = new ProgressDialog(StopJobListActivity.this);
                p.setCancelable(false);
                p.setMessage("Loading...");
                p.show();

                HashMap<String, RequestBody> map = new HashMap<>();
                //map.put("JobListId", NetworkUtils.createPartFromString(prefManager.getJobId()));
                map.put("data", NetworkUtils.createPartFromString(prefManager.getJobListArray()));
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
                                    String PendingQty = object.has("PendingQty") ? object.getString("PendingQty").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;

                                    StopJobNoModel stopJobNoModel = new StopJobNoModel(JobListStartStopId, JobListId, ActivityByEmployeeId, ProcessId,
                                            ItmId, StartDateTime, EndDateTime, Qty, Remarks, InsertedOn, LastUpdatedOn, InsertedByUserId,
                                            LastUpdatedByUserId, PDFFile, JobNo, EmployeeName, ProcessName, ItmName, ShopName, MachineName, PlanQty, PendingQty);
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
                            if (!checkJobNoList.get(0).getPlanQty().equalsIgnoreCase("")) {
                                //tvPlanQty.setText(String.format("%.0f", Double.parseDouble(checkJobNoList.get(0).getQty())));
                                tvPlanQty.setText(String.format("%.0f", Double.parseDouble(checkJobNoList.get(0).getPendingQty())));
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
                            tvHome.setVisibility(View.GONE);
                            lylFilter.setVisibility(View.GONE);
                            imgStartBtn.setVisibility(View.GONE);
                            lylStop.setVisibility(View.GONE);
                            //lylDisplayData.setVisibility(View.VISIBLE);
                            lylDisplayData.setVisibility(View.GONE);
                            pdfView.setVisibility(View.GONE);
                            ltLogout.setVisibility(View.GONE);

                            String urlPdf = checkJobNoList.get(0).getPDFFile();
                            mainUrl = urlPdf.replace(KEY_U0027, KEY_AFOSTROPHE).replace(KEY_U0026, KEY_EMPER);
                            file = mainUrl.substring(mainUrl.lastIndexOf('/') + 1);
                            openPDFView();

                            apiStartJobAPICardShow();

                            //apiRadioBtnList();
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
                        prefManager.setJobListArray("");
                        startActivity(new Intent(StopJobListActivity.this, StartJobListActivity.class));
                        finish();
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

    private void apiStartJobAPICardShow() {
        try {
            if (isInternet()) {
                ProgressDialog p = new ProgressDialog(StopJobListActivity.this);
                p.setCancelable(false);
                p.setMessage("Loading...");
                p.show();

                startJobNoList.clear();

                HashMap<String, RequestBody> map = new HashMap<>();
                //map.put("JobListId", NetworkUtils.createPartFromString(prefManager.getJobId()));
                map.put("data", NetworkUtils.createPartFromString(prefManager.getJobListArray()));
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
                                    String DoneQty = object.has("DoneQty") ? object.getString("DoneQty").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String PendingQty = object.has("PendingQty") ? object.getString("PendingQty").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String RefNo = object.has("RefNo") ? object.getString("RefNo").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String PCNo = object.has("PCNo") ? object.getString("PCNo").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;

                                    String PartNo = object.has("PartNo") ? object.getString("PartNo").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String HubPartNo = object.has("HubPartNo") ? object.getString("HubPartNo").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String Size = object.has("Size") ? object.getString("Size").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String Desc = object.has("Desc") ? object.getString("Desc").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;

                                    String Status = object.has("Status") ? object.getString("Status").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String HoldReasonTextListId = object.has("HoldReasonTextListId") ? object.getString("HoldReasonTextListId").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String HoldReason = object.has("HoldReason") ? object.getString("HoldReason").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String JobStartStopRemarks = object.has("JobStartStopRemarks") ? object.getString("JobStartStopRemarks").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;

                                    //TextLists model = new TextLists(JobListId, JobNo);
                                    JobNoModel noModel = new JobNoModel(JobListId, JobNo, Dt, EmployeeId, ProcessId, ItmId, StartDateTime, EndDateTime,
                                            CycleTime, Qty, Remarks, InsertedOn, LastUpdatedOn, InsertedByUserId, LastUpdatedByUserId, EmployeeName, ProcessName,
                                            ItmName, ShopName, MachineName, PDFFile, DoneQty, PendingQty, RefNo, PCNo, PartNo, HubPartNo, Size, Desc, Status, HoldReasonTextListId, HoldReason, JobStartStopRemarks, false);
                                    startJobNoList.add(noModel);
                                }

                                stopDataSetIntoUI();

                                apiRadioBtnList();

                                tvJobNo.setEnabled(false);
                                tvItemCode.setEnabled(false);
                                tvPlanQty.setEnabled(false);
                                tvHome.setVisibility(View.VISIBLE);// Advance Visible
                                lylFilter.setVisibility(View.GONE);
                                imgStartBtn.setVisibility(View.GONE);
                                lylStop.setVisibility(View.GONE);
                                lylDisplayData.setVisibility(View.GONE);
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
                        //apiCheckStartJob();
                        //tvHome.setVisibility(View.VISIBLE);// Advance Visible
                        //showErrorMessage(message);
                    }

                    @Override
                    public void onNullResponse() {
                        if (p.isShowing()) {
                            p.dismiss();
                        }
                        Toast.makeText(StopJobListActivity.this, "Null Response", Toast.LENGTH_SHORT).show();
                        //showErrorMessage("Null Response");
                    }

                    @Override
                    public void onExceptionFired(String error) {
                        if (p.isShowing()) {
                            p.dismiss();
                        }
                        Toast.makeText(StopJobListActivity.this, error, Toast.LENGTH_SHORT).show();

                        //showErrorMessage(error);
                    }

                    @Override
                    public void on400(int responseCode) {
                        if (p.isShowing()) {
                            p.dismiss();
                        }
                        Toast.makeText(StopJobListActivity.this, String.valueOf(responseCode), Toast.LENGTH_SHORT).show();
                        //showErrorMessage(String.valueOf(responseCode));
                    }

                    @Override
                    public void onFailure(@NonNull Throwable t) {
                        if (p.isShowing()) {
                            p.dismiss();
                        }
                        Toast.makeText(StopJobListActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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

    private void stopDataSetIntoUI() {
        try {
            StopJobListAdapter startJobListAdapter = new StopJobListAdapter(startJobNoList);
            rvStopJObList.setLayoutManager(new LinearLayoutManager(StopJobListActivity.this, LinearLayoutManager.HORIZONTAL, false));
            rvStopJObList.setItemAnimator(new DefaultItemAnimator());
            rvStopJObList.setFocusable(false);
            rvStopJObList.setAdapter(startJobListAdapter);
            startJobListAdapter.setItemClick(this);
            rvStopJObList.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void apiStopJob(String jobListId, String qty, String remarks, String status, String acceptedQty, String holdReason) {
        try {
            if (isInternet()) {
                stopJobNoList.clear();

                ProgressDialog p = new ProgressDialog(StopJobListActivity.this);
                p.setCancelable(false);
                p.setMessage("Loading...");
                p.show();

                HashMap<String, RequestBody> map = new HashMap<>();
                //map.put("JobListId", NetworkUtils.createPartFromString(prefManager.getJobId()));
                map.put("JobListId", NetworkUtils.createPartFromString(jobListId));
                map.put("EmpId", NetworkUtils.createPartFromString(prefManager.getUserName()));
                map.put("UserId", NetworkUtils.createPartFromString(prefManager.getUserId()));
                map.put("Qty", NetworkUtils.createPartFromString(qty));
                map.put("Remarks", NetworkUtils.createPartFromString(remarks));
                map.put("Status", NetworkUtils.createPartFromString(status));
                map.put("AcceptedQty", NetworkUtils.createPartFromString(acceptedQty));
                map.put("HoldReason", NetworkUtils.createPartFromString(holdReason));

                new ApiUtil(this, API_STOP_JOB, map, null, new OnResponse() {
                    @Override
                    public void onSuccess(JSONArray jsonArray) {
                        try {
                            apiCheckStartJob();

                            /*prefManager.setJobListArray("");
                            startActivity(new Intent(StopJobListActivity.this, StartJobListActivity.class));
                            finish();*/


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
                            //deleteEmptyFolder(new File(DashboardActivityNew.this.getExternalFilesDir("BhansaliTechno") + "/BhansaliTechno"));

                            alertDialog.dismiss();

                            if (p.isShowing()) {
                                p.dismiss();
                            }

                            checkJobStartOrNot = "False";
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
                            lylFilter.setVisibility(View.GONE); //Advance Commit
                            tvHome.setVisibility(View.VISIBLE);
                            imgRotation.setVisibility(View.GONE);
                            tvRotation.setVisibility(View.VISIBLE);
                            ltLogout.setVisibility(View.VISIBLE);
                            rvRadioBtn.setVisibility(View.GONE);
                            radioList.clear();

                            prefManager.setJobId("");
                            prefManager.setJobNo("");
                            prefManager.setShop("");
                            prefManager.setMachine("");
                            prefManager.setProcess("");
                            prefManager.setItmName("");
                            prefManager.setPlanQty("");
                            prefManager.setHandlerTime("");
                            prefManager.setStartTime("");

                            Toast.makeText(StopJobListActivity.this, "Job Done Success.", Toast.LENGTH_SHORT).show();

                            //apiJobNoList();

                            //apiStartJobAPICardShow(); //Comment 08-09-2023
                        } catch (Exception e) {
                            if (p.isShowing()) {
                                p.dismiss();
                            }
                            alertDialog.dismiss();
                            e.printStackTrace();
                            //showErrorMessage(e.getLocalizedMessage());
                            FirebaseCrashlytics.getInstance().recordException(e);
                        }
                    }

                    @Override
                    public void on209(String message) {
                        if (p.isShowing()) {
                            p.dismiss();
                        }
                        Toast.makeText(StopJobListActivity.this, message, Toast.LENGTH_SHORT).show();
                        //showErrorMessage(message);
                    }

                    @Override
                    public void onNullResponse() {
                        if (p.isShowing()) {
                            p.dismiss();
                        }
                        Toast.makeText(StopJobListActivity.this, "Null Response", Toast.LENGTH_SHORT).show();
                        //showErrorMessage("Null Response");
                    }

                    @Override
                    public void onExceptionFired(String error) {
                        if (p.isShowing()) {
                            p.dismiss();
                        }
                        //showErrorMessage(error);
                        Toast.makeText(StopJobListActivity.this, error, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void on400(int responseCode) {
                        if (p.isShowing()) {
                            p.dismiss();
                        }
                        //showErrorMessage(String.valueOf(responseCode));
                        Toast.makeText(StopJobListActivity.this, String.valueOf(responseCode), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(@NonNull Throwable t) {
                        if (p.isShowing()) {
                            p.dismiss();
                        }
                        //showErrorMessage(t.getLocalizedMessage());
                        Toast.makeText(StopJobListActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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

    private void stopFun(ArrayList<JobNoModel> list, int pos) {
        try {
            View confirmDialog = LayoutInflater.from(StopJobListActivity.this).inflate(R.layout.layout_stop_dlg, null);
            AppCompatButton btnYes = confirmDialog.findViewById(R.id.btnYes);
            AppCompatButton btnNo = confirmDialog.findViewById(R.id.btnNo);

            EditText edtProductionQty = confirmDialog.findViewById(R.id.edtProductionQty);
            EditText edtAcceptProductionQty = confirmDialog.findViewById(R.id.edtAcceptProductionQty);
            edtAcceptProductionQty.setText("0");
            EditText edtRemarks = confirmDialog.findViewById(R.id.edtRemarks);

            final AlertDialog.Builder alert = new AlertDialog.Builder(StopJobListActivity.this);
            alert.setView(confirmDialog);
            //alert.setTitle("Password");
            //Creating an alert dialog
            alertDialog = alert.create();
            alertDialog.show();
            alertDialog.setCancelable(true);
            btnYes.setOnClickListener(view -> {
                if (edtProductionQty.getText().toString().isEmpty()) {
                    Toast.makeText(StopJobListActivity.this, "Please enter production quantity.", Toast.LENGTH_SHORT).show();
                } else if (edtAcceptProductionQty.getText().toString().isEmpty()) {
                    Toast.makeText(StopJobListActivity.this, "Please enter acceptance quantity.", Toast.LENGTH_SHORT).show();
                } else if (Double.parseDouble(edtProductionQty.getText().toString().trim()) > Double.parseDouble(tvPlanQty.getText().toString().trim())) {
                    Toast.makeText(StopJobListActivity.this, "Quantity can't be greater than available quantity.", Toast.LENGTH_SHORT).show();
                } else if (Double.parseDouble(edtProductionQty.getText().toString().trim())
                        + Double.parseDouble(edtAcceptProductionQty.getText().toString().trim()) > Double.parseDouble(list.get(pos).getPendingQty())) {
                    Toast.makeText(StopJobListActivity.this, "Quantity and accepted qty can't be greater than available quantity.", Toast.LENGTH_SHORT).show();
                } else {
                    apiStopJob(list.get(pos).getJobListId(), edtProductionQty.getText().toString().trim(),
                            edtRemarks.getText().toString().trim(), "Stop", edtAcceptProductionQty.getText().toString().trim(), "");
                }
            });
            btnNo.setOnClickListener(v1 -> alertDialog.dismiss());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void holdFun(ArrayList<JobNoModel> list, int pos) {
        try {
            reasonId = "";

            View confirmDialog = LayoutInflater.from(StopJobListActivity.this).inflate(R.layout.layout_hold_dlg, null);
            AppCompatButton btnYes = confirmDialog.findViewById(R.id.btnYes);
            AppCompatButton btnNo = confirmDialog.findViewById(R.id.btnNo);

            TextView dropdownValue = confirmDialog.findViewById(R.id.edtHoldReason1);

            final AlertDialog.Builder alert = new AlertDialog.Builder(StopJobListActivity.this);
            alert.setView(confirmDialog);
            //alert.setTitle("Password");
            //Creating an alert dialog
            alertDialog = alert.create();
            alertDialog.show();
            alertDialog.setCancelable(true);
            dropdownValue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ListPopupWindow listPopupWindow = createListPopupWindow(dropdownValue, holdReasonList);
                    listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                            reasonId = holdReasonList.get(pos).getId();
                            dropdownValue.setText(holdReasonList.get(pos).getName());
                            listPopupWindow.dismiss();
                        }
                    });
                    listPopupWindow.show();
                }
            });
            btnYes.setOnClickListener(view -> {
                if (dropdownValue.getText().toString().isEmpty()) {
                    Toast.makeText(StopJobListActivity.this, "Please select hold reason.", Toast.LENGTH_SHORT).show();
                } else {
                    //apiStopJob(dropdownValue.getText().toString().trim(), "");
                    apiStopJob(list.get(pos).getJobListId(), "",
                            "", "Hold", "", reasonId);
                }
            });
            btnNo.setOnClickListener(v1 -> alertDialog.dismiss());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ListPopupWindow createListPopupWindow(View anchor, List<TextListModel> items) {
        final ListPopupWindow popup = new ListPopupWindow(StopJobListActivity.this);
        ListAdapter adapter = new ListPopupWindowAdapter(items);
        popup.setAnchorView(anchor);
        popup.setWidth(((View) anchor.getParent()).getWidth() / 2);
        popup.setAdapter(adapter);
        return popup;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == IMMEDIATE_APP_UPDATE_REQ_CODE) {
                if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(getApplicationContext(), "Update canceled by user!", Toast.LENGTH_LONG).show();
                } else if (resultCode == RESULT_OK) {
                    Toast.makeText(getApplicationContext(), "Update success!" + resultCode, Toast.LENGTH_LONG).show();
                } else {
                    //Toast.makeText(getApplicationContext(), "Update Failed! Result Code", Toast.LENGTH_LONG).show();
                    checkUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void apiRadioBtnList() {
        try {
            if (isInternet()) {
                ProgressDialog p = new ProgressDialog(StopJobListActivity.this);
                p.setCancelable(false);
                p.setMessage("Loading...");
                p.show();

                radioList.clear();

                HashMap<String, RequestBody> map = new HashMap<>();
                map.put("EmpId", NetworkUtils.createPartFromString(prefManager.getUserName()));
                map.put("data", NetworkUtils.createPartFromString(prefManager.getJobListArray()));
                map.put("UserId", NetworkUtils.createPartFromString(prefManager.getUserId()));

                new ApiUtil(this, API_RADIO_BUTTON, map, null, new OnResponse() {
                    @Override
                    public void onSuccess(JSONArray jsonArray) {
                        try {
                            if (p.isShowing()) {
                                p.dismiss();
                            }
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String FUId = object.has("FUId") ? object.getString("FUId") : NULL;
                                    String LnNo = object.has("LnNo") ? object.getString("LnNo").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String RecordId = object.has("RecordId") ? object.getString("RecordId").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String FormName = object.has("FormName") ? object.getString("FormName").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String FilePath = object.has("FilePath") ? object.getString("FilePath").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String InsertedOn = object.has("InsertedOn") ? object.getString("InsertedOn").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String LastUpdatedOn = object.has("LastUpdatedOn") ? object.getString("LastUpdatedOn").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String InsertedByUserId = object.has("InsertedByUserId") ? object.getString("InsertedByUserId").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String LastUpdatedByUserId = object.has("LastUpdatedByUserId") ? object.getString("LastUpdatedByUserId").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String FileName = object.has("FileName") ? object.getString("FileName").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String imageType = object.has("imageType") ? object.getString("imageType").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String FileTypeTextListId = object.has("FileTypeTextListId") ? object.getString("FileTypeTextListId").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String PDFFile = object.has("PDFFile") ? object.getString("PDFFile").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String FileType = object.has("FileType") ? object.getString("FileType").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;

                                    RadioModel model = new RadioModel(FUId, LnNo, RecordId, FormName, FilePath, InsertedOn, LastUpdatedOn,
                                            InsertedByUserId, LastUpdatedByUserId, FileName, imageType, FileTypeTextListId, PDFFile, FileType);
                                    radioList.add(model);
                                }
                                rvRadioBtn.setVisibility(View.VISIBLE);
                                RadioAdapter radioAdapter = new RadioAdapter(radioList, "", (id, name, no, code) -> {
                                    //String urlPdf = code;
                                    mainUrl = code.replace(KEY_U0027, KEY_AFOSTROPHE).replace(KEY_U0026, KEY_EMPER);
                                    file = mainUrl.substring(mainUrl.lastIndexOf('/') + 1);
                                    openPDFView();
                                });
                                LinearLayoutManager mManager = new LinearLayoutManager(StopJobListActivity.this, LinearLayoutManager.HORIZONTAL, false);
                                rvRadioBtn.setLayoutManager(mManager);
                                rvRadioBtn.setAdapter(radioAdapter);

                                String urlPdf = radioList.get(0).getPDFFile();
                                mainUrl = urlPdf.replace(KEY_U0027, KEY_AFOSTROPHE).replace(KEY_U0026, KEY_EMPER);
                                file = mainUrl.substring(mainUrl.lastIndexOf('/') + 1);
                                openPDFView();
                            }
                        } catch (JSONException e) {
                            if (p.isShowing()) {
                                p.dismiss();
                            }
                            e.printStackTrace();
                            //showErrorMessage(e.getLocalizedMessage());
                            FirebaseCrashlytics.getInstance().recordException(e);
                        }
                    }

                    @Override
                    public void on209(String message) {
                        if (p.isShowing()) {
                            p.dismiss();
                        }
                        rvRadioBtn.setVisibility(View.GONE);
                        Toast.makeText(StopJobListActivity.this, message, Toast.LENGTH_SHORT).show();
                        //showErrorMessage(message);
                    }

                    @Override
                    public void onNullResponse() {
                        if (p.isShowing()) {
                            p.dismiss();
                        }
                        rvRadioBtn.setVisibility(View.GONE);
                        Toast.makeText(StopJobListActivity.this, "Null Response", Toast.LENGTH_SHORT).show();
                        //showErrorMessage("Null Response");
                    }

                    @Override
                    public void onExceptionFired(String error) {
                        if (p.isShowing()) {
                            p.dismiss();
                        }
                        rvRadioBtn.setVisibility(View.GONE);
                        Toast.makeText(StopJobListActivity.this, error, Toast.LENGTH_SHORT).show();
                        //showErrorMessage(error);
                    }

                    @Override
                    public void on400(int responseCode) {
                        if (p.isShowing()) {
                            p.dismiss();
                        }
                        rvRadioBtn.setVisibility(View.GONE);
                        Toast.makeText(StopJobListActivity.this, String.valueOf(responseCode), Toast.LENGTH_SHORT).show();
                        //showErrorMessage(String.valueOf(responseCode));
                    }

                    @Override
                    public void onFailure(@NonNull Throwable t) {
                        if (p.isShowing()) {
                            p.dismiss();
                        }
                        rvRadioBtn.setVisibility(View.GONE);
                        Toast.makeText(StopJobListActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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

    private void apiHoldReasonAPI() {
        try {
            holdReasonList.clear();

            HashMap<String, RequestBody> map = new HashMap<>();
            map.put("Group", NetworkUtils.createPartFromString("Job Hold Reason"));

            new ApiUtil(StopJobListActivity.this, API_TEXT_LIST, map, null, new OnResponse() {
                @Override
                public void onSuccess(JSONArray jsonArray) {
                    try {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String LocId = Utils.getReplacedString(object.has("TextListId") ? object.getString("TextListId") : NULL);
                            String Name = Utils.getReplacedString(object.has("Text") ? object.getString("Text") : NULL);
                            TextListModel textListModel = new TextListModel(LocId, Name);
                            holdReasonList.add(textListModel);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        FirebaseCrashlytics.getInstance().recordException(e);
                    }
                }

                @Override
                public void on209(String message) {
                    Toast.makeText(StopJobListActivity.this, message, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNullResponse() {
                    Toast.makeText(StopJobListActivity.this, "Null Response", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onExceptionFired(String error) {
                    Toast.makeText(StopJobListActivity.this, error, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void on400(int responseCode) {
                    Toast.makeText(StopJobListActivity.this, String.valueOf(responseCode), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(@NonNull Throwable t) {
                    Toast.makeText(StopJobListActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }).request();
        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }
}
