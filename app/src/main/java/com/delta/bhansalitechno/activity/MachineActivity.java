package com.delta.bhansalitechno.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.airbnb.lottie.LottieAnimationView;
import com.delta.bhansalitechno.R;
import com.delta.bhansalitechno.adapter.MachineAdapter;
import com.delta.bhansalitechno.api.ApiUtil;
import com.delta.bhansalitechno.bottomsheets.LogoutFragment;
import com.delta.bhansalitechno.bottomsheets.MessageFragment;
import com.delta.bhansalitechno.bottomsheets.NoInternetFragment;
import com.delta.bhansalitechno.databinding.ActivityMachineBinding;
import com.delta.bhansalitechno.interfaces.OnResponse;
import com.delta.bhansalitechno.model.MachineModel;
import com.delta.bhansalitechno.utils.ConnectionDetector;
import com.delta.bhansalitechno.utils.NetworkUtils;
import com.delta.bhansalitechno.utils.PrefManager;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.RequestBody;

import static com.delta.bhansalitechno.utils.AppConfig.API_MACHINE;
import static com.delta.bhansalitechno.utils.AppConfig.KEY_AFOSTROPHE;
import static com.delta.bhansalitechno.utils.AppConfig.KEY_BTM_SHT;
import static com.delta.bhansalitechno.utils.AppConfig.KEY_EMPER;
import static com.delta.bhansalitechno.utils.AppConfig.KEY_U0026;
import static com.delta.bhansalitechno.utils.AppConfig.KEY_U0027;
import static com.delta.bhansalitechno.utils.AppConfig.NULL;

public class MachineActivity extends AppCompatActivity {

    private ActivityMachineBinding binding;
    private PrefManager prefManager;

    private int screenOrientation = 1;
    private final ArrayList<MachineModel> machineList = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            binding = DataBindingUtil.setContentView(this, R.layout.activity_machine);
            prefManager = new PrefManager(MachineActivity.this);

            if (getIntent().getBooleanExtra("FROM_NOTIFICATION", false)) {
                // Show the Toast message
                Toast.makeText(this, "Job imported. Please wait a few minutes.", Toast.LENGTH_SHORT).show();
            }


            if (!prefManager.getMachineName().isEmpty()) {
                binding.tvSelectedMachine.setVisibility(View.GONE);
                binding.tvSelectedMachine.setText("Previous Selected Machine : " + prefManager.getMachineName() + " " + "(" + prefManager.getMachineNo() + ")");
            } else {
                binding.tvSelectedMachine.setVisibility(View.GONE);
            }

            binding.rvMachine.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            //binding.rvMachine.setLayoutManager(new GridLayoutManager(this, 2));
            binding.rvMachine.setItemAnimator(new DefaultItemAnimator());
            binding.rvMachine.setFocusable(false);

            binding.btnNext.setOnClickListener(v -> {
                if (!prefManager.getMachineName().isEmpty()) {
                    //startActivity(new Intent(MachineActivity.this, DashboardActivityNew.class));
                    startActivity(new Intent(MachineActivity.this, StartJobListActivity.class));
                    finish();
                } else {
                    showErrorMessage("Please select machine");
                }
            });


            LottieAnimationView ltLogout = findViewById(R.id.ltLogout);
            ltLogout.setOnClickListener(v -> {
                showAppLogoutMessage();
            });

            apiMachineList();
        } catch (Exception e) {
            e.printStackTrace();
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

    private void apiMachineList() {
        try {
            if (isInternet()) {
                ProgressDialog p = new ProgressDialog(MachineActivity.this);
                p.setCancelable(false);
                p.setMessage("Loading...");
                p.show();

                machineList.clear();

                HashMap<String, RequestBody> map = new HashMap<>();
                map.put("EmpId", NetworkUtils.createPartFromString(prefManager.getUserName()));

                new ApiUtil(this, API_MACHINE, map, null, new OnResponse() {
                    @Override
                    public void onSuccess(JSONArray jsonArray) {
                        try {
                            if (p.isShowing()) {
                                p.dismiss();
                            }
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String MachineId = object.has("MachineId") ? object.getString("MachineId") : NULL;
                                    String MachineNo = object.has("MachineNo") ? object.getString("MachineNo").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String MachineCode = object.has("MachineCode") ? object.getString("MachineCode").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;
                                    String MachineName = object.has("MachineName") ? object.getString("MachineName").replace(KEY_U0026, KEY_EMPER).replace(KEY_U0027, KEY_AFOSTROPHE) : NULL;

                                    MachineModel model = new MachineModel(MachineId, MachineNo, MachineCode, MachineName);
                                    machineList.add(model);
                                }

                                MachineAdapter machineAdapter = new MachineAdapter(machineList, "", (id, name, no, code) -> {
                                    prefManager.setMachineId(id);
                                    prefManager.setMachineNo(no);
                                    prefManager.setMachineCode(code);
                                    prefManager.setMachineName(name);
                                });
                                binding.rvMachine.setAdapter(machineAdapter);
                                binding.btnNext.setVisibility(View.VISIBLE);
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

    public boolean isInternet() {
        return new ConnectionDetector(MachineActivity.this).isInternetConnected();
    }

    private void showErrorMessage(String message) {
        final MessageFragment m = new MessageFragment(message);
        new Handler(Looper.myLooper()).post(() -> m.show(MachineActivity.this.getSupportFragmentManager(), KEY_BTM_SHT));
    }

    private void showNoInternet() {
        final NoInternetFragment n = new NoInternetFragment(() -> {

        });
        new Handler(Looper.myLooper()).post(() -> n.show(MachineActivity.this.getSupportFragmentManager(), KEY_BTM_SHT));
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

                startActivity(new Intent(MachineActivity.this, LoginActivity.class));
                finish();
            });
            logoutFragment.show(getSupportFragmentManager(), "bottom_sheet");
        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }
}
