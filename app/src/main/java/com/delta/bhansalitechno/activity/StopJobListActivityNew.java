package com.delta.bhansalitechno.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
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
import com.delta.bhansalitechno.PDFView;
import com.delta.bhansalitechno.R;
import com.delta.bhansalitechno.adapter.ListPopupWindowAdapter;
import com.delta.bhansalitechno.adapter.ListPopupWindowAdapterforlocation;
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
import com.delta.bhansalitechno.listener.OnLoadCompleteListener;
import com.delta.bhansalitechno.listener.OnPageChangeListener;
import com.delta.bhansalitechno.listener.OnPageErrorListener;
import com.delta.bhansalitechno.model.JobNoModel;
import com.delta.bhansalitechno.model.LocationModel;
import com.delta.bhansalitechno.model.RadioModel;
import com.delta.bhansalitechno.model.StopJobNoModel;
import com.delta.bhansalitechno.model.TextListModel;
import com.delta.bhansalitechno.scroll.ScrollHandle;
import com.delta.bhansalitechno.util.FitPolicy;
import com.delta.bhansalitechno.util.ProgressRequestBody;
import com.delta.bhansalitechno.utils.ConnectionDetector;
import com.delta.bhansalitechno.utils.MoveViewTouchListener;
import com.delta.bhansalitechno.utils.NetworkUtils;
import com.delta.bhansalitechno.utils.PrefManager;
import com.delta.bhansalitechno.utils.Utils;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.shockwave.pdfium.PdfDocument;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
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
import es.voghdev.pdfviewpager.library.adapter.PdfScale;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;

import static com.delta.bhansalitechno.utils.AppConfig.API_CHECK_JOB_START;
import static com.delta.bhansalitechno.utils.AppConfig.API_JOB_NO;
import static com.delta.bhansalitechno.utils.AppConfig.API_LOCATION;
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

public class StopJobListActivityNew extends AppCompatActivity implements View.OnClickListener, RecyclerViewClickListener, ProgressRequestBody.UploadCallbacks {

    private PrefManager prefManager;

    private LinearLayout lylSwipe;
    private TextView tvStartTime;
    private LottieAnimationView ltLogout;
    private TextView tvFilter, tvRotation;
    private PDFView pdfView;

    private String mainUrl = "";
    private String file;
    private ProgressDialog progressDialog = null;
    private int screenOrientation = 1;

    long MillisecondTime, StartTime;
    Handler handler;
    int Seconds, Minutes, hour;
    int rotation;

    ArrayList<JobNoModel> startJobNoList = new ArrayList<>();
    ArrayList<StopJobNoModel> checkJobNoList = new ArrayList<>();
    ArrayList<StopJobNoModel> stopJobNoList = new ArrayList<>();

    private AlertDialog alertDialog;

    String checkJobStartOrNot = "";

    private AppUpdateManager appUpdateManager;
    private static final int IMMEDIATE_APP_UPDATE_REQ_CODE = 124;

    private RecyclerView rvRadioBtn;
    private RecyclerView rvStopJObList;
    ArrayList<RadioModel> radioList = new ArrayList<>();
    ArrayList<TextListModel> holdReasonList = new ArrayList<>();
    ArrayList<LocationModel> locationList = new ArrayList<>();

    String reasonId;
    String locationId;
    Handler shiftTimeHandler;

    Integer pageNumber = 0;
    String pdfFileName;

    //TODO: Attachment Field
    private static final float maxHeight = 1280.0f;
    private static final float maxWidth = 1280.0f;
    private Uri imageUriGlobal;
    //Uri selectedImageUri1;
    //private Bitmap thumbnail1;
    private static final int PERMISSION_REQUEST_CODE = 200;
    protected static final int PICK_REQUEST_CAMERA_IMAGE = 1888;
    private static final int PICK_REQUEST_GALLERY_IMAGE = 1;
    private static final int PICK_REQUEST_FILE = 101;
    public Uri filePath;
    private String attachmentpath = "";
    LinearLayout attachfile;
    TextView attachementfilename;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_stop_job_list_new);

            appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());
            checkUpdate();

            prefManager = new PrefManager(StopJobListActivityNew.this);
            rotation = 90;

            TextView tvNameWithCompany = findViewById(R.id.tvNameWithCompany);
            tvNameWithCompany.setText(prefManager.getFirstName());

            lylSwipe = findViewById(R.id.lylSwipe);
            TextView tvDismiss = findViewById(R.id.tvDismiss);
            tvStartTime = findViewById(R.id.tvStartTime);

            tvFilter = findViewById(R.id.tvFilter);
            tvRotation = findViewById(R.id.tvRotation);

            rvRadioBtn = findViewById(R.id.rvRadioBtn);
            rvStopJObList = findViewById(R.id.rvStopJObList);


            tvFilter.setOnClickListener(this);
            tvDismiss.setOnClickListener(this);
            tvRotation.setOnClickListener(this);

            ltLogout = findViewById(R.id.ltLogout);
            ltLogout.setOnClickListener(v -> {
                for (int i = 0; i < startJobNoList.size(); i++) {
                    if (startJobNoList.get(i).getStatus().equalsIgnoreCase("")) {
                        openDialog();
                        break;
                    } else {
                        showAppLogoutMessage();
                    }
                }
            });

            pdfView = findViewById(R.id.idPDFView);
            //pdfView.setOnTouchListener(new MoveViewTouchListener(pdfView));

            apiHoldReasonAPI();
            apiCheckStartJob();
            apiLocationAPI();


            /*final ZoomLinearLayout zoomLinearLayout = (ZoomLinearLayout) findViewById(R.id.zoom_linear_layout);
            zoomLinearLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    zoomLinearLayout.init(DashboardActivityNew.this);
                    return false;
                }
            });*/

            new Handler(Looper.getMainLooper()).post(() -> {
                shiftTimeHandler = new Handler();
                shiftTimeHandler.postDelayed(runnable1, 30000);
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
                case R.id.tvFilter:
                    if (rvStopJObList.getVisibility() == View.VISIBLE) {
                        rvStopJObList.setVisibility(View.GONE);
                        //rvRadioBtn.setVisibility(View.GONE);
                        tvFilter.setText("Show");
                    } else {
                        rvStopJObList.setVisibility(View.VISIBLE);
                        //rvRadioBtn.setVisibility(View.VISIBLE);
                        tvFilter.setText("Hide");
                    }
                    break;
                case R.id.tvDismiss:
                    lylSwipe.setVisibility(View.GONE);
                    break;
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
        return new ConnectionDetector(StopJobListActivityNew.this).isInternetConnected();
    }

    private void openPDFView() {
        try {
            String pathName = StopJobListActivityNew.this.getExternalFilesDir("BhansaliTechno").toString();
            String fileName = "";
            File directory = new File(pathName);
            File[] files = directory.listFiles();
            try {
                if (files != null && files.length > 0) {
                    for (File value : files) {
                        if (value.getName().equals(file)) {
                            fileName = value.getName();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (!file.equals(fileName)) {
                new StopJobListActivityNew.DownloadingTask().execute();
            } else {
                setPDF(getPdfPath());
                //setPDF(getPdfPath(), 0);
                //setPDF1(getPdfPath());

                new Handler(Looper.getMainLooper()).post(() -> {
                    handler = new Handler();
                    StartTime = SystemClock.uptimeMillis();
                    handler.postDelayed(runnable, 0);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    public int getScreenWidth(Context ctx) {
        int w = 0;
        if (ctx instanceof Activity) {
            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((Activity) ctx).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            w = displaymetrics.widthPixels;
        }
        return w;
    }

    private PdfScale getPdfScale() {
        PdfScale scale = new PdfScale();
        scale.setScale(3.0f);
        scale.setCenterX(getScreenWidth(this) / 2);
        scale.setCenterY(0f);
        return scale;
    }

    private String getPdfPath() {
        File f = new File(StopJobListActivityNew.this.getExternalFilesDir("BhansaliTechno"), file);
        return f.getAbsolutePath();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

    @SuppressLint("StaticFieldLeak")
    private class DownloadingTask extends AsyncTask<String, Integer, String> {

        File outputFile = null;
        String fileName = file;
        String storage = StopJobListActivityNew.this.getExternalFilesDir("BhansaliTechno").toString();
        File folder = new File(storage);
        URL url;

        {
            try {
                url = new URL(mainUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(StopJobListActivityNew.this, "MalformedURL", Toast.LENGTH_SHORT).show();
            }
        }

        HttpURLConnection c;

        {
            try {
                c = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(StopJobListActivityNew.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(StopJobListActivityNew.this);
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
                    Toast.makeText(StopJobListActivityNew.this, "Document Load Successfully", Toast.LENGTH_SHORT).show();
                    setPDF(getPdfPath());
                    //setPDF(getPdfPath(), 0);
                    //setPDF1(getPdfPath());
                    new Handler(Looper.getMainLooper()).post(() -> {
                        handler = new Handler();
                        StartTime = SystemClock.uptimeMillis();
                        handler.postDelayed(runnable, 0);
                    });
                } else {
                    new Handler().postDelayed(() -> {
                    }, 3000);
                    c.disconnect();
                    Toast.makeText(StopJobListActivityNew.this, c.getResponseMessage(), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Handler().postDelayed(() -> {
                }, 3000);
                c.disconnect();
                try {
                    Toast.makeText(StopJobListActivityNew.this, c.getResponseMessage(), Toast.LENGTH_SHORT).show();
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
                    outputFile = new File(StopJobListActivityNew.this.getExternalFilesDir("BhansaliTechno"), fileName);

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
                StopJobListActivityNew.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(StopJobListActivityNew.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
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

    public void setPDF(String asset) {
        try {
            pdfView.fromFile(new File(asset)).defaultPage(0).pageFitPolicy(FitPolicy.BOTH)
                    .enableSwipe(true).enableAntialiasing(true).swipeHorizontal(true).onLoad(new OnLoadCompleteListener() {
                @Override
                public void loadComplete(int nbPages) {
                    //pdfView.fitToWidth(0);
                    pdfView.setVisibility(View.VISIBLE);
                    tvRotation.setVisibility(View.VISIBLE);
                }
            }).load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPDF(String asset, int pages) {
        pdfView.fromFile(new File(asset)).swipeHorizontal(false).onLoad(new OnLoadCompleteListener() {
            @Override
            public void loadComplete(int nbPages) {
                pdfView.fitToWidth(0);   // Goto to specific page
                //pdfView.setMinZoom(60);
                //pdfView.setMidZoom(80);
                //pdfView.setMaxZoom(100);
                pdfView.setVisibility(View.VISIBLE);
                tvRotation.setVisibility(View.VISIBLE);
            }
        }).defaultPage(pages).enableAntialiasing(true).load();
    }

    public void setPDF1(String asset) {
        pdfFileName = asset;
        pdfView.fromFile(new File(asset))
                .enableSwipe(true)
                .defaultPage(pageNumber)
                .onPageChange(new OnPageChangeListener() {
                    @Override
                    public void onPageChanged(int page, int pageCount) {
                        pageNumber = page;
                        setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
                    }
                })
                .enableAnnotationRendering(true)
                .enableAntialiasing(true)
                .enableDoubletap(true)
                .fitEachPage(true)
                .swipeHorizontal(true)
                .onLoad(new OnLoadCompleteListener() {
                    @Override
                    public void loadComplete(int nbPages) {
                        pdfView.fitToWidth(0);   // Goto to specific page
                        //pdfView.setMinZoom(60);
                        //pdfView.setMidZoom(80);
                        //pdfView.setMaxZoom(100);
                        pdfView.setVisibility(View.VISIBLE);
                        tvRotation.setVisibility(View.VISIBLE);
                    }
                })
                .scrollHandle(new com.delta.bhansalitechno.scroll.DefaultScrollHandle(this))
                .autoSpacing(true)
                .spacing(10)
                .onPageError(new OnPageErrorListener() {
                    @Override
                    public void onPageError(int page, Throwable t) {

                    }
                })
                //.pageFitPolicy(com.delta.bhansalitechno.util.FitPolicy.BOTH)
                .load();
        pdfView.enableDoubletap(true);
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
                    String totalTime = String.format("%02d", hour) + ":" + String.format("%02d", Minutes) + ":" + String.format("%02d", Seconds);
                    prefManager.setHandlerTime(totalTime);
                    tvStartTime.setText(prefManager.getHandlerTime());
                });
                handler.postDelayed(this, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void rotationPdf() {
        try {
            if (rotation == 90) {
                rotation = 180;
                pdfView.setRotation(90);
            } else if (rotation == 180) {
                rotation = 270;
                pdfView.setRotation(180);
            } else if (rotation == 270) {
                rotation = 0;
                pdfView.setRotation(270);
            } else {
                rotation = 90;
                pdfView.setRotation(0);
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

                prefManager.setOneTimeShiftCheck("True");
                prefManager.setJobListArray("");
                prefManager.setShiftType("");
                prefManager.setShiftIn("");
                prefManager.setShiftOut("");

                startActivity(new Intent(StopJobListActivityNew.this, LoginActivity.class));
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
            File dir = new File(StopJobListActivityNew.this.getExternalFilesDir("BhansaliTechno") + "/BhansaliTechno");
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


    private void showErrorMessage(String message) {
        final MessageFragment m = new MessageFragment(message);
        new Handler(Looper.myLooper()).post(() -> m.show(StopJobListActivityNew.this.getSupportFragmentManager(), KEY_BTM_SHT));
    }

    private void showNoInternet() {
        final NoInternetFragment n = new NoInternetFragment(() -> {

        });
        new Handler(Looper.myLooper()).post(() -> n.show(StopJobListActivityNew.this.getSupportFragmentManager(), KEY_BTM_SHT));
    }

    private void apiCheckStartJob() {
        try {
            if (isInternet()) {
                checkJobNoList.clear();

                ProgressDialog p = new ProgressDialog(StopJobListActivityNew.this);
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

                            pdfView.setVisibility(View.GONE);
                            ltLogout.setVisibility(View.VISIBLE);

                            String urlPdf = checkJobNoList.get(0).getPDFFile();
                            mainUrl = urlPdf.replace(KEY_U0027, KEY_AFOSTROPHE).replace(KEY_U0026, KEY_EMPER);
                            file = mainUrl.substring(mainUrl.lastIndexOf('/') + 1);
                            openPDFView();

                            apiStartJobAPICardShow();
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
                        startActivity(new Intent(StopJobListActivityNew.this, StartJobListActivity.class));
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
                ProgressDialog p = new ProgressDialog(StopJobListActivityNew.this);
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

                                pdfView.setVisibility(View.GONE);
                                ltLogout.setVisibility(View.VISIBLE);

                                String urlPdf = startJobNoList.get(0).getPdfFile();
                                mainUrl = urlPdf.replace(KEY_U0027, KEY_AFOSTROPHE).replace(KEY_U0026, KEY_EMPER);
                                file = mainUrl.substring(mainUrl.lastIndexOf('/') + 1);
                                openPDFView();
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
                    }

                    @Override
                    public void onNullResponse() {
                        if (p.isShowing()) {
                            p.dismiss();
                        }
                        Toast.makeText(StopJobListActivityNew.this, "Null Response", Toast.LENGTH_SHORT).show();
                        //showErrorMessage("Null Response");
                    }

                    @Override
                    public void onExceptionFired(String error) {
                        if (p.isShowing()) {
                            p.dismiss();
                        }
                        Toast.makeText(StopJobListActivityNew.this, error, Toast.LENGTH_SHORT).show();

                        //showErrorMessage(error);
                    }

                    @Override
                    public void on400(int responseCode) {
                        if (p.isShowing()) {
                            p.dismiss();
                        }
                        Toast.makeText(StopJobListActivityNew.this, String.valueOf(responseCode), Toast.LENGTH_SHORT).show();
                        //showErrorMessage(String.valueOf(responseCode));
                    }

                    @Override
                    public void onFailure(@NonNull Throwable t) {
                        if (p.isShowing()) {
                            p.dismiss();
                        }
                        Toast.makeText(StopJobListActivityNew.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
            rvStopJObList.setLayoutManager(new LinearLayoutManager(StopJobListActivityNew.this, LinearLayoutManager.HORIZONTAL, false));
            rvStopJObList.setItemAnimator(new DefaultItemAnimator());
            rvStopJObList.setFocusable(false);
            rvStopJObList.setAdapter(startJobListAdapter);
            startJobListAdapter.setItemClick(this);
            rvStopJObList.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void apiStopJob(String jobListId, String qty, String remarks, String status, String acceptedQty, String holdReason, String locationId) {
        try {
            if (isInternet()) {
                stopJobNoList.clear();

                ProgressDialog p = new ProgressDialog(StopJobListActivityNew.this);
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
                map.put("LocId", NetworkUtils.createPartFromString(locationId));

                MultipartBody.Part file;

                if (!attachmentpath.equalsIgnoreCase("")) {
                    File oldFIle = new File(attachmentpath);
                    ProgressRequestBody fileBody = new ProgressRequestBody(oldFIle, StopJobListActivityNew.this);
                    file = MultipartBody.Part.createFormData("file", oldFIle.getName(), fileBody);
                } else {
                    file = null;
                }

                new ApiUtil(this, API_STOP_JOB, map, file, new OnResponse() {
                    @Override
                    public void onSuccess(JSONArray jsonArray) {
                        try {
                            apiCheckStartJob();
                            alertDialog.dismiss();
                            if (p.isShowing()) {
                                p.dismiss();
                            }

                            checkJobStartOrNot = "False";
                            lylSwipe.setVisibility(View.GONE);
                            pdfView.setVisibility(View.GONE);
                            handler.removeCallbacksAndMessages(null);
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

                            Toast.makeText(StopJobListActivityNew.this, "Job Done Success.", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(StopJobListActivityNew.this, message, Toast.LENGTH_SHORT).show();
                        //showErrorMessage(message);
                    }

                    @Override
                    public void onNullResponse() {
                        if (p.isShowing()) {
                            p.dismiss();
                        }
                        Toast.makeText(StopJobListActivityNew.this, "Null Response", Toast.LENGTH_SHORT).show();
                        //showErrorMessage("Null Response");
                    }

                    @Override
                    public void onExceptionFired(String error) {
                        if (p.isShowing()) {
                            p.dismiss();
                        }
                        //showErrorMessage(error);
                        Toast.makeText(StopJobListActivityNew.this, error, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void on400(int responseCode) {
                        if (p.isShowing()) {
                            p.dismiss();
                        }
                        //showErrorMessage(String.valueOf(responseCode));
                        Toast.makeText(StopJobListActivityNew.this, String.valueOf(responseCode), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(@NonNull Throwable t) {
                        if (p.isShowing()) {
                            p.dismiss();
                        }
                        //showErrorMessage(t.getLocalizedMessage());
                        Toast.makeText(StopJobListActivityNew.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
            View confirmDialog = LayoutInflater.from(StopJobListActivityNew.this).inflate(R.layout.layout_stop_dlg, null);
            AppCompatButton btnYes = confirmDialog.findViewById(R.id.btnYes);
            AppCompatButton btnNo = confirmDialog.findViewById(R.id.btnNo);

            EditText edtProductionQty = confirmDialog.findViewById(R.id.edtProductionQty);
            EditText edtAcceptProductionQty = confirmDialog.findViewById(R.id.edtAcceptProductionQty);
            edtAcceptProductionQty.setText("0");
            EditText edtRemarks = confirmDialog.findViewById(R.id.edtRemarks);
            TextView dropdownValue = confirmDialog.findViewById(R.id.edtlocation);
            dropdownValue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ListPopupWindow listPopupWindow = createListPopupWindowforlocation(dropdownValue, locationList);
                    listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                            locationId = locationList.get(pos).getName();
                            dropdownValue.setText(locationList.get(pos).getName());
                            listPopupWindow.dismiss();
                        }
                    });
                    listPopupWindow.show();
                }
            });
            attachfile = confirmDialog.findViewById(R.id.attachfile);
            attachementfilename = confirmDialog.findViewById(R.id.attachementfilename);
            attachfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectImage();
                }

            });

            final AlertDialog.Builder alert = new AlertDialog.Builder(StopJobListActivityNew.this);
            alert.setView(confirmDialog);
            //alert.setTitle("Password");
            //Creating an alert dialog
            alertDialog = alert.create();
            alertDialog.show();
            alertDialog.setCancelable(true);
            btnYes.setOnClickListener(view -> {
                if (edtProductionQty.getText().toString().isEmpty()) {
                    Toast.makeText(StopJobListActivityNew.this, "Please enter Accepted quantity.", Toast.LENGTH_SHORT).show();
                } else if (edtAcceptProductionQty.getText().toString().isEmpty()) {
                    Toast.makeText(StopJobListActivityNew.this, "Please enter Rejected quantity.", Toast.LENGTH_SHORT).show();
                } else if (Double.parseDouble(edtProductionQty.getText().toString().trim())
                        + Double.parseDouble(edtAcceptProductionQty.getText().toString().trim()) > Double.parseDouble(list.get(pos).getPendingQty())) {
                    Toast.makeText(StopJobListActivityNew.this, "Accepted Quantity and Rejected qty can't be greater than available quantity.", Toast.LENGTH_SHORT).show();
                } else {
                    apiStopJob(list.get(pos).getJobListId(), edtProductionQty.getText().toString().trim(),
                            edtRemarks.getText().toString().trim(), "Stop", edtAcceptProductionQty.getText().toString().trim(), "", locationId );
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

            View confirmDialog = LayoutInflater.from(StopJobListActivityNew.this).inflate(R.layout.layout_hold_dlg, null);
            AppCompatButton btnYes = confirmDialog.findViewById(R.id.btnYes);
            AppCompatButton btnNo = confirmDialog.findViewById(R.id.btnNo);

            TextView dropdownValue = confirmDialog.findViewById(R.id.edtHoldReason1);

            final AlertDialog.Builder alert = new AlertDialog.Builder(StopJobListActivityNew.this);
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
                    Toast.makeText(StopJobListActivityNew.this, "Please select hold reason.", Toast.LENGTH_SHORT).show();
                } else {
                    //apiStopJob(dropdownValue.getText().toString().trim(), "");
                    apiStopJob(list.get(pos).getJobListId(), "",
                            "", "Hold", "", reasonId, "");
                }
            });
            btnNo.setOnClickListener(v1 -> alertDialog.dismiss());
            attachfile = confirmDialog.findViewById(R.id.attachfile);
            attachementfilename = confirmDialog.findViewById(R.id.attachementfilename);
            attachfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectImage();
                }

            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ListPopupWindow createListPopupWindow(View anchor, List<TextListModel> items) {
        final ListPopupWindow popup = new ListPopupWindow(StopJobListActivityNew.this);
        ListAdapter adapter = new ListPopupWindowAdapter(items);
        popup.setAnchorView(anchor);
        popup.setWidth(((View) anchor.getParent()).getWidth() / 2);
        popup.setAdapter(adapter);
        return popup;
    }

    private ListPopupWindow createListPopupWindowforlocation(View anchor, List<LocationModel> items) {
        final ListPopupWindow popup = new ListPopupWindow(StopJobListActivityNew.this);
        ListAdapter adapter = new ListPopupWindowAdapterforlocation(items);
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
                ProgressDialog p = new ProgressDialog(StopJobListActivityNew.this);
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
                                LinearLayoutManager mManager = new LinearLayoutManager(StopJobListActivityNew.this, LinearLayoutManager.HORIZONTAL, false);
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
                        Toast.makeText(StopJobListActivityNew.this, message, Toast.LENGTH_SHORT).show();
                        //showErrorMessage(message);
                    }

                    @Override
                    public void onNullResponse() {
                        if (p.isShowing()) {
                            p.dismiss();
                        }
                        rvRadioBtn.setVisibility(View.GONE);
                        Toast.makeText(StopJobListActivityNew.this, "Null Response", Toast.LENGTH_SHORT).show();
                        //showErrorMessage("Null Response");
                    }

                    @Override
                    public void onExceptionFired(String error) {
                        if (p.isShowing()) {
                            p.dismiss();
                        }
                        rvRadioBtn.setVisibility(View.GONE);
                        Toast.makeText(StopJobListActivityNew.this, error, Toast.LENGTH_SHORT).show();
                        //showErrorMessage(error);
                    }

                    @Override
                    public void on400(int responseCode) {
                        if (p.isShowing()) {
                            p.dismiss();
                        }
                        rvRadioBtn.setVisibility(View.GONE);
                        Toast.makeText(StopJobListActivityNew.this, String.valueOf(responseCode), Toast.LENGTH_SHORT).show();
                        //showErrorMessage(String.valueOf(responseCode));
                    }

                    @Override
                    public void onFailure(@NonNull Throwable t) {
                        if (p.isShowing()) {
                            p.dismiss();
                        }
                        rvRadioBtn.setVisibility(View.GONE);
                        Toast.makeText(StopJobListActivityNew.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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

            new ApiUtil(StopJobListActivityNew.this, API_TEXT_LIST, map, null, new OnResponse() {
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
                    Toast.makeText(StopJobListActivityNew.this, message, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNullResponse() {
                    Toast.makeText(StopJobListActivityNew.this, "Null Response", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onExceptionFired(String error) {
                    Toast.makeText(StopJobListActivityNew.this, error, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void on400(int responseCode) {
                    Toast.makeText(StopJobListActivityNew.this, String.valueOf(responseCode), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(@NonNull Throwable t) {
                    Toast.makeText(StopJobListActivityNew.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }).request();
        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    private void apiLocationAPI() {
        try {
            locationList.clear();

            HashMap<String, RequestBody> map = new HashMap<>();

            new ApiUtil(StopJobListActivityNew.this, API_LOCATION, map, null, new OnResponse() {
                @Override
                public void onSuccess(JSONArray jsonArray) {
                    try {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            LocationModel locationModel = new LocationModel();
                            locationList.add(locationModel);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        FirebaseCrashlytics.getInstance().recordException(e);
                    }
                }

                @Override
                public void on209(String message) {
                    Toast.makeText(StopJobListActivityNew.this, message, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNullResponse() {
                    Toast.makeText(StopJobListActivityNew.this, "Null Response", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onExceptionFired(String error) {
                    Toast.makeText(StopJobListActivityNew.this, error, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void on400(int responseCode) {
                    Toast.makeText(StopJobListActivityNew.this, String.valueOf(responseCode), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(@NonNull Throwable t) {
                    Toast.makeText(StopJobListActivityNew.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }).request();
        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    private void shiftOverAlert() {
        try {
            View confirmDialog = LayoutInflater.from(StopJobListActivityNew.this).inflate(R.layout.layout_shift_over_dlg, null);
            AppCompatButton btnYes = confirmDialog.findViewById(R.id.btnYes);
            AppCompatButton btnNo = confirmDialog.findViewById(R.id.btnNo);

            final AlertDialog.Builder alert = new AlertDialog.Builder(StopJobListActivityNew.this);
            alert.setView(confirmDialog);
            //alert.setTitle("Password");
            //Creating an alert dialog
            alertDialog = alert.create();
            alertDialog.show();
            alertDialog.setCancelable(false);
            btnYes.setOnClickListener(view -> {
                alertDialog.dismiss();
            });
            btnNo.setOnClickListener(v1 -> {
                prefManager.setOneTimeShiftCheck("False");
                alertDialog.dismiss();
                shiftTimeHandler.removeCallbacks(runnable1);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Runnable runnable1 = new Runnable() {
        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        public void run() {
            if (!prefManager.getShiftOut().isEmpty()) {
                try {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        String shiftOutHour = Utils.ConvertDateFormat(prefManager.getShiftOut(), "hh:mm:ss", "hh");
                        String shiftOutMin = Utils.ConvertDateFormat(prefManager.getShiftOut(), "hh:mm:ss", "mm");
                        //String shiftOutSecond = Utils.ConvertDateFormat(prefManager.getShiftOut(),"hh:mm:ss","ss");

                        int actualServerHour = Integer.parseInt(shiftOutHour);
                        int actualServerMin = Integer.parseInt(shiftOutMin) - Integer.parseInt(prefManager.getMinusMin());

                        //String systemCurrentTime = Utils.getCurrentTimestamp("hh:mm:ss");
                        String systemCurrentHour = Utils.getCurrentTimestamp("hh");
                        String systemCurrentMin = Utils.getCurrentTimestamp("mm");
                        //String systemCurrentSecond = Utils.getCurrentTimestamp("ss");

                        int actualSystemHour = Integer.parseInt(Objects.requireNonNull(systemCurrentHour));
                        int actualSystemMin = Integer.parseInt(Objects.requireNonNull(systemCurrentMin));

                        //int currentDayOfMonth = Calendar.getInstance().get(Calendar.HOUR);
                        //int currentMonth = Calendar.getInstance().get(Calendar.MINUTE);

                        boolean checkLogic = actualServerHour == actualSystemHour && actualServerMin == actualSystemMin;

                        if (checkLogic) {
                            //prefManager.setOneTimeShiftCheck("True");
                            if (prefManager.getOneTimeShiftCheck().equalsIgnoreCase("True")) {
                                shiftOverAlert();
                            }
                        }
                    });
                    shiftTimeHandler.postDelayed(this, 30000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                //Toast.makeText(StopJobListActivity.this, "Shift Out Time Is Empty.", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(StopJobListActivityNew.this);
        builder.setTitle("Alert !");
        builder.setMessage("All Start Job First Hold Or Stop After Logout !");
        builder.setCancelable(true);
        builder.setPositiveButton("OK", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });
        /*builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });*/
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(
                StopJobListActivityNew.this);
        builder.setTitle("Add Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    try {
                        String fileName = System.currentTimeMillis() + ".jpg";
                        // create parameters for Intent with filename
                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Images.Media.TITLE, fileName);
                        values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera");
                        imageUriGlobal = StopJobListActivityNew.this.getContentResolver().insert(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                values);
                        // create new Intent
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUriGlobal);
                        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                        startActivityForResult(intent, PICK_REQUEST_CAMERA_IMAGE);
                    } catch (SecurityException e) {
                        FirebaseCrashlytics.getInstance().recordException(e);
                        Toast.makeText(StopJobListActivityNew.this, "You have to give permission for take image. Please Give permission.", Toast.LENGTH_SHORT).show();
                        //CommonUses.showToast(context, "You have to give permission for take image. Please Give permission.");
                    }
//                } else if (items[item].equals("Choose from Gallery")) {
//                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    intent.setType("image/*");
//                    startActivityForResult(intent, PICK_REQUEST_GALLERY_IMAGE);
//                } else if (items[item].equals("Attached Document")) {
//                    Intent intent = new Intent();
//                    intent.setType("*/*");
//                    intent.setAction(Intent.ACTION_GET_CONTENT);
//                    startActivityForResult(Intent.createChooser(intent, "ChooseFile"), PICK_REQUEST_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void askForResolution(final String picturePath) {
        try {
            attachmentpath = picturePath;
            File imgFile = new File(attachmentpath);
            attachementfilename.setText(imgFile.getName());
        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = StopJobListActivityNew.this.managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    public String compressImage(String imagePath) {
        String filepath = "";
        try {

            Bitmap scaledBitmap = null;

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap bmp = BitmapFactory.decodeFile(imagePath, options);

            int actualHeight = options.outHeight;
            int actualWidth = options.outWidth;

            float imgRatio = (float) actualWidth / (float) actualHeight;
            float maxRatio = maxWidth / maxHeight;

            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight;
                    actualWidth = (int) (imgRatio * actualWidth);
                    actualHeight = (int) maxHeight;
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth;
                    actualHeight = (int) (imgRatio * actualHeight);
                    actualWidth = (int) maxWidth;
                } else {
                    actualHeight = (int) maxHeight;
                    actualWidth = (int) maxWidth;
                }
            }

            options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
            options.inJustDecodeBounds = false;
            options.inDither = false;
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inTempStorage = new byte[16 * 1024];

            try {
                bmp = BitmapFactory.decodeFile(imagePath, options);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();
            }

            try {
                scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.RGB_565);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();
            }

            float ratioX = actualWidth / (float) options.outWidth;
            float ratioY = actualHeight / (float) options.outHeight;
            float middleX = actualWidth / 2.0f;
            float middleY = actualHeight / 2.0f;

            Matrix scaleMatrix = new Matrix();
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

            assert scaledBitmap != null;
            Canvas canvas = new Canvas(scaledBitmap);
            canvas.setMatrix(scaleMatrix);
            canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

            if (bmp != null) {
                bmp.recycle();
            }

            ExifInterface exif;
            try {
                exif = new ExifInterface(imagePath);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                } else if (orientation == 3) {
                    matrix.postRotate(180);
                } else if (orientation == 8) {
                    matrix.postRotate(270);
                }
                scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            } catch (IOException e) {
                FirebaseCrashlytics.getInstance().recordException(e);
            }
            FileOutputStream out;

            /*File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                    + "/Android/data/"
                    + context.getApplicationContext().getPackageName()
                    + "/Files/Compressed");*/

            File mediaStorageDir = new File(StopJobListActivityNew.this.getExternalFilesDir(null)
                    + "/UnnatiiAttendance");

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                mediaStorageDir.mkdirs();
            }

            String mImageName = "IMG_" + System.currentTimeMillis() + ".jpg";

            Log.d("tag1", "image name is 0 " + System.currentTimeMillis());

            filepath = (mediaStorageDir.getAbsolutePath() + "/" + mImageName);

            try {

                out = new FileOutputStream(filepath);
                //write the compressed bitmap at the destination specified by filename.
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

            } catch (FileNotFoundException e) {
                FirebaseCrashlytics.getInstance().recordException(e);
            }

            askForResolution(filepath);
        } catch (Exception e) {
            Log.d("tag", "Exeption is " + e.getMessage());
        }
        return filepath;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = Math.min(heightRatio, widthRatio);
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    @Override
    public void onProgressUpdate(int percentage) {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onFinish() {

    }
}
