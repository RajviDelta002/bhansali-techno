package com.delta.bhansalitechno.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.delta.bhansalitechno.R;
import com.delta.bhansalitechno.api.ApiUtil;
import com.delta.bhansalitechno.bottomsheets.MessageFragment;
import com.delta.bhansalitechno.bottomsheets.NoInternetFragment;
import com.delta.bhansalitechno.interfaces.OnResponse;
import com.delta.bhansalitechno.utils.AppConfig;
import com.delta.bhansalitechno.utils.ConnectionDetector;
import com.delta.bhansalitechno.utils.NetworkUtils;
import com.delta.bhansalitechno.utils.PrefManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.RequestBody;

import static com.delta.bhansalitechno.utils.AppConfig.API_LOGIN;
import static com.delta.bhansalitechno.utils.AppConfig.KEY_BACK;
import static com.delta.bhansalitechno.utils.AppConfig.KEY_BTM_SHT;
import static com.delta.bhansalitechno.utils.AppConfig.KEY_GO_SETTING;
import static com.delta.bhansalitechno.utils.AppConfig.KEY_GRANT_PERMISSION;
import static com.delta.bhansalitechno.utils.AppConfig.KEY_PKG;
import static com.delta.bhansalitechno.utils.AppConfig.NULL;

public class LoginActivity extends AppCompatActivity {

    private PrefManager prefManager;

    private String fcmId = "";
    private int screenOrientation = 1;
    private TextInputEditText txtUsername, txtPassword;
    static final int PERMISSION_REQUEST_CODE_PHONE_STATE = 10;

    AlertDialog aDialog;

    String[] appPermissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_login_new);

            prefManager = new PrefManager(LoginActivity.this);

            requestFirebaseTokenId();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkAndRequestPermission()) {
                    init();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    @SuppressLint("HardwareIds")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_CODE_PHONE_STATE) {

            //Init
            HashMap<String, Integer> permissionResults = new HashMap<>();
            int deniedCount = 0;

            //Gather permission grant Result
            for (int i = 0; i < grantResults.length; i++) {

                //Add Only denied permission
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    permissionResults.put(permissions[i], grantResults[i]);
                    deniedCount++;
                }
            }

            //Check if all permission are granted
            if (deniedCount == 0) {

                //Allow to proceed
                init();

            } else {  //At least one or all permission are are denied

                for (Map.Entry<String, Integer> entry : permissionResults.entrySet()) {

                    String permName = entry.getKey();

                    //Permission is denied and never ask again isn't checked
                    //ask again explaining the importance of permission
                    //shouldShowRequestPermissionRationale will true
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permName)) {
                        showPermissionDialog(AppConfig.KEY_EMPTY_STRING,
                                "App need your permission to process further",
                                KEY_GRANT_PERMISSION,
                                v -> {
                                    aDialog.dismiss();
                                    checkAndRequestPermission();
                                },
                                KEY_BACK,
                                v -> {
                                    aDialog.dismiss();
                                    finish();
                                },
                                false);
                    } else {

                        //permission is denied  and never ask again is checked
                        //shouldShowRequestPermissionRationale will false
                        //Ask user to go to settings and manually allow permission

                        showPermissionDialog(AppConfig.KEY_EMPTY_STRING,
                                "You have denied some permission. Allow all permission at " +
                                        "[Settings] -> [Permission]",
                                KEY_GO_SETTING,
                                v -> {
                                    //Go to settings
                                    Intent intent = new Intent(
                                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                            Uri.fromParts(KEY_PKG, getPackageName(), null));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                },
                                KEY_BACK,
                                v -> {
                                    aDialog.dismiss();
                                    LoginActivity.this.finish();
                                },
                                false);

                    }

                }
            }
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

    private void init() {
        try {
            ImageView imgAppLogo = findViewById(R.id.imgAppLogo);
            Animation mAnimationSlide1 = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.anim_out);
            imgAppLogo.startAnimation(mAnimationSlide1);

            TextView btnLogin = findViewById(R.id.btnLogin);
            Animation mAnimationSlide = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.anim_in);
            btnLogin.startAnimation(mAnimationSlide);

            txtUsername = findViewById(R.id.txtUsername);
            txtPassword = findViewById(R.id.txtPassword);

            btnLogin.setOnClickListener(v -> {
                isValid();
                //startActivity(new Intent(LoginActivity.this, DashboardActivityNew.class));
                //finish();
            });
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean checkAndRequestPermission() {

        List<String> listPermissionNeeded = new ArrayList<>();

        //Check All Permission Are Granted On Not
        for (String perm : appPermissions) {
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                listPermissionNeeded.add(perm);
            }
        }

        //Ask For Non Granted Permission
        if (!listPermissionNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionNeeded.toArray(new String[0]),
                    PERMISSION_REQUEST_CODE_PHONE_STATE);
            return false;
        }
        return true;
    }

    public boolean isInternet() {
        return new ConnectionDetector(LoginActivity.this).isInternetConnected();
    }

    private void isValid() {
        if (TextUtils.isEmpty(Objects.requireNonNull(txtUsername.getText()).toString())) {
            showErrorMessage("Please, Enter Username");
        } else if (TextUtils.isEmpty(Objects.requireNonNull(txtPassword.getText()).toString())) {
            showErrorMessage("Please, Enter password");
        } else {
            apiLogin();
        }
    }

    private void showErrorMessage(String message) {
        final MessageFragment m = new MessageFragment(message);
        new Handler(Looper.myLooper()).post(() -> m.show(LoginActivity.this.getSupportFragmentManager(), KEY_BTM_SHT));
    }

    private void showNoInternet() {
        final NoInternetFragment n = new NoInternetFragment(() -> {

        });
        new Handler(Looper.myLooper()).post(() -> n.show(LoginActivity.this.getSupportFragmentManager(), KEY_BTM_SHT));
    }

    private void apiLogin() {
        try {
            if (isInternet()) {
                ProgressDialog p = new ProgressDialog(LoginActivity.this);
                p.setCancelable(false);
                p.setMessage("Loading...");
                p.show();

                HashMap<String, RequestBody> map = new HashMap<>();
                map.put("UserName", NetworkUtils.createPartFromString(Objects.requireNonNull(txtUsername.getText()).toString().trim()));
                map.put("Password", NetworkUtils.createPartFromString(Objects.requireNonNull(txtPassword.getText()).toString().trim()));
                map.put("FCMId", NetworkUtils.createPartFromString(prefManager.getFcmId()));

                new ApiUtil(LoginActivity.this, API_LOGIN, map, null, new OnResponse() {
                    @Override
                    public void onSuccess(JSONArray jsonArray) {
                        try {
                            if (p.isShowing()) {
                                p.dismiss();
                            }
                            if (jsonArray != null && jsonArray.length() > 0) {
                                JSONObject object = jsonArray.getJSONObject(0);
                                String UserId = object.has("UserId") ? object.getString("UserId") : NULL;
                                String FirstName = object.has("FirstName") ? object.getString("FirstName") : NULL;
                                String LastName = object.has("LastName") ? object.getString("LastName") : NULL;
                                String UserName = object.has("UserName") ? object.getString("UserName") : NULL;
                                String Password = object.has("Password") ? object.getString("Password") : NULL;
                                String IsAdmin = object.has("IsAdmin") ? object.getString("IsAdmin") : NULL;
                                String InsertedOn = object.has("InsertedOn") ? object.getString("InsertedOn") : NULL;
                                String LastUpdatedOn = object.has("LastUpdatedOn") ? object.getString("LastUpdatedOn") : NULL;
                                String LastUpdatedByUserId = object.has("LastUpdatedByUserId") ? object.getString("LastUpdatedByUserId") : NULL;
                                String InsertedByUserId = object.has("InsertedByUserId") ? object.getString("InsertedByUserId") : NULL;
                                String IsHRAdmin = object.has("IsHRAdmin") ? object.getString("IsHRAdmin") : NULL;
                                String Approval1UserId = object.has("Approval1UserId") ? object.getString("Approval1UserId") : NULL;
                                String Approval1UserName = object.has("Approval1UserName") ? object.getString("Approval1UserName") : NULL;
                                String Approval2UserId = object.has("Approval2UserId") ? object.getString("Approval2UserId") : NULL;
                                String Approval2UserName = object.has("Approval2UserName") ? object.getString("Approval2UserName") : NULL;
                                String IsMD = object.has("IsMD") ? object.getString("IsMD") : NULL;
                                String AttendanceReq = object.has("AttendanceReq") ? object.getString("AttendanceReq") : NULL;
                                String IsDisable = object.has("IsDisable") ? object.getString("IsDisable") : NULL;
                                String MediclaimRights = object.has("MediclaimRights") ? object.getString("MediclaimRights") : NULL;
                                String AttendanceExemption = object.has("AttendanceExemption") ? object.getString("AttendanceExemption") : NULL;
                                String IsLDAPAllowed = object.has("IsLDAPAllowed") ? object.getString("IsLDAPAllowed") : NULL;
                                String LDAPUserName = object.has("LDAPUserName") ? object.getString("LDAPUserName") : NULL;
                                String IsAllGradations = object.has("IsAllGradations") ? object.getString("IsAllGradations") : NULL;
                                String FCMId = object.has("FCMId") ? object.getString("FCMId") : NULL;
                                String ProfilePhotoPathShow = object.has("ProfilePhotoPathShow") ? object.getString("ProfilePhotoPathShow") : NULL;

                                prefManager.setUserId(UserId);
                                prefManager.setFirstName(FirstName);
                                prefManager.setLastName(LastName);
                                prefManager.setUserName(UserName);
                                prefManager.setPassword(Password);
                                prefManager.setFcmId(FCMId);
                                prefManager.setIsAdmin(IsAdmin);
                                prefManager.setLoggedIn("True");

                                startActivity(new Intent(LoginActivity.this, DashboardActivityNew.class));
                                finish();
                            } else {
                                if (p.isShowing()) {
                                    p.dismiss();
                                }
                                showErrorMessage("No User Found !!!");
                            }
                        } catch (JSONException e) {
                            if (p.isShowing()) {
                                p.dismiss();
                            }
                            e.printStackTrace();
                            FirebaseCrashlytics.getInstance().recordException(e);
                            showErrorMessage(e.getLocalizedMessage());
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

    public void showPermissionDialog(String title, String msg, String positiveLabel,
                                     View.OnClickListener positiveOnClick,
                                     String negativeLabel,
                                     View.OnClickListener negativeOnClick,
                                     boolean isCancelable) {
        try {
            @SuppressLint("InflateParams") View view = LayoutInflater
                    .from(LoginActivity.this)
                    .inflate(R.layout.dialog_need_permission, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setTitle(title);
            builder.setCancelable(isCancelable);
            builder.setMessage(msg);
            builder.setView(view);

            //Casting
            TextView lblMessage = view.findViewById(R.id.lblMessage);
            TextView lblPos = view.findViewById(R.id.lblPositiveButton);
            TextView lblNeg = view.findViewById(R.id.lblNegativeButton);

            //Setting Up Text
            lblMessage.setVisibility(View.GONE);
            lblPos.setText(positiveLabel);
            lblNeg.setText(negativeLabel);

            //Setting Up Listener
            lblPos.setOnClickListener(positiveOnClick);
            lblNeg.setOnClickListener(negativeOnClick);

            aDialog = builder.create();
            aDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    private void requestFirebaseTokenId() {
        try {
            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Log.d("TAG", "Fetching FCM registration token failed", task.getException());
                    return;
                }

                // Get new FCM registration token
                String token = task.getResult();
                fcmId = token;
                prefManager.setFcmId(fcmId);

                // Log and toast
                String msg = getString(R.string.msg_token_fmt, token);
                Log.d("TAG", msg);
            });
        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
            Toast.makeText(LoginActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
