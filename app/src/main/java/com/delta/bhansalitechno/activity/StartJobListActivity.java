package com.delta.bhansalitechno.activity;

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

import com.delta.bhansalitechno.R;
import com.delta.bhansalitechno.adapter.StartJobListAdapter;
import com.delta.bhansalitechno.api.ApiUtil;
import com.delta.bhansalitechno.bottomsheets.MessageFragment;
import com.delta.bhansalitechno.bottomsheets.NoInternetFragment;
import com.delta.bhansalitechno.databinding.ActivityStartJobListBinding;
import com.delta.bhansalitechno.interfaces.OnResponse;
import com.delta.bhansalitechno.model.JobNoModel;
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

import static com.delta.bhansalitechno.utils.AppConfig.API_JOB_NO;
import static com.delta.bhansalitechno.utils.AppConfig.API_START_JOB;
import static com.delta.bhansalitechno.utils.AppConfig.KEY_AFOSTROPHE;
import static com.delta.bhansalitechno.utils.AppConfig.KEY_BTM_SHT;
import static com.delta.bhansalitechno.utils.AppConfig.KEY_EMPER;
import static com.delta.bhansalitechno.utils.AppConfig.KEY_U0026;
import static com.delta.bhansalitechno.utils.AppConfig.KEY_U0027;
import static com.delta.bhansalitechno.utils.AppConfig.NULL;

public class StartJobListActivity extends AppCompatActivity {

    private ActivityStartJobListBinding binding;
    private PrefManager prefManager;
    private final ArrayList<JobNoModel> multipleJobNoSelectList = new ArrayList<>();

    private int screenOrientation = 1;
    private StartJobListAdapter startJobListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            binding = DataBindingUtil.setContentView(this, R.layout.activity_start_job_list);
            prefManager = new PrefManager(StartJobListActivity.this);

            binding.rvJob.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            binding.rvJob.setItemAnimator(new DefaultItemAnimator());
            binding.rvJob.setFocusable(false);

            apiJobNoList();

            binding.btnStart.setOnClickListener(v -> {
                if (startJobListAdapter != null) {
                    if (startJobListAdapter.getSelected().size() > 0) {
                        if (startJobListAdapter.getSelected().size() > 1) {
                            String lastPartNo = "";
                            String lastProcessName = "";
                            for (int i = 0; i < startJobListAdapter.getSelected().size(); i++) {
                                if (lastPartNo.isEmpty() && lastProcessName.isEmpty()) {
                                    lastPartNo = startJobListAdapter.getSelected().get(i).getPartNo();
                                    lastProcessName = startJobListAdapter.getSelected().get(i).getProcessName();
                                } else {
                                    /*if (!lastPartNo.equalsIgnoreCase(startJobListAdapter.getSelected().get(i).getRefNo())) {
                                        Toast.makeText(this, "Selected Same Part No In Job List.", Toast.LENGTH_SHORT).show();
                                        break;
                                    }

                                    if (!lastProcessName.equalsIgnoreCase(startJobListAdapter.getSelected().get(i).getProcessName())) {
                                        Toast.makeText(this, "Selected Same Process In Job List.", Toast.LENGTH_SHORT).show();
                                        break;
                                    }*/

                                    if (!lastPartNo.equalsIgnoreCase(startJobListAdapter.getSelected().get(i).getPartNo()) ||
                                            !lastProcessName.equalsIgnoreCase(startJobListAdapter.getSelected().get(i).getProcessName())) {
                                        Toast.makeText(this, "Selected Same Part No & Process In Job List.", Toast.LENGTH_SHORT).show();
                                        break;
                                    }

                                    if (i == startJobListAdapter.getSelected().size() - 1) {
                                        //changeScreen();
                                        apiStartJob();
                                    }
                                }
                            }
                        } else {
                            //changeScreen();
                            apiStartJob();
                        }
                    } else {
                        Toast.makeText(this, "Please Select Job List", Toast.LENGTH_SHORT).show();
                    }
                }
            });
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

    private void changeScreen() {
        try {
            String itemArray;
            ArrayList<String> data = new ArrayList<>();
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < startJobListAdapter.getSelected().size(); i++) {
                //data.add("JobListId:" + startJobListAdapter.getSelected().get(i).getJobListId());
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("JobListId", startJobListAdapter.getSelected().get(i).getJobListId());
                jsonArray.put(jsonObject1);
            }
            //JSONArray jsonArrayHistory = new JSONArray(data);
            //itemArray = String.valueOf(jsonArrayHistory);

            itemArray = jsonArray.toString();
            prefManager.setJobListArray(itemArray);
            startActivity(new Intent(StartJobListActivity.this, StopJobListActivity.class));
            finish();
            Toast.makeText(this, "Wait some time redirect screen....", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void apiJobNoList() {
        try {
            if (isInternet()) {
                ProgressDialog p = new ProgressDialog(StartJobListActivity.this);
                p.setCancelable(false);
                p.setMessage("Loading...");
                p.show();

                multipleJobNoSelectList.clear();

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
                                            ItmName, ShopName, MachineName, "", DoneQty, PendingQty, RefNo, PCNo, PartNo,HubPartNo,Size,Desc,Status,HoldReasonTextListId,HoldReason,JobStartStopRemarks,false);
                                    multipleJobNoSelectList.add(noModel);
                                }

                                startJobListAdapter = new StartJobListAdapter(multipleJobNoSelectList);
                                binding.rvJob.setAdapter(startJobListAdapter);
                                binding.btnStart.setVisibility(View.VISIBLE);
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
        return new ConnectionDetector(StartJobListActivity.this).isInternetConnected();
    }

    private void showErrorMessage(String message) {
        final MessageFragment m = new MessageFragment(message);
        new Handler(Looper.myLooper()).post(() -> m.show(StartJobListActivity.this.getSupportFragmentManager(), KEY_BTM_SHT));
    }

    private void showNoInternet() {
        final NoInternetFragment n = new NoInternetFragment(() -> {

        });
        new Handler(Looper.myLooper()).post(() -> n.show(StartJobListActivity.this.getSupportFragmentManager(), KEY_BTM_SHT));
    }

    private void apiStartJob() {
        try {
            if (isInternet()) {
                ProgressDialog p = new ProgressDialog(StartJobListActivity.this);
                p.setCancelable(false);
                p.setMessage("Loading...");
                p.show();

                String itemArray;
                ArrayList<String> data = new ArrayList<>();
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < startJobListAdapter.getSelected().size(); i++) {
                    //data.add("JobListId:" + startJobListAdapter.getSelected().get(i).getJobListId());
                    JSONObject jsonObject1 = new JSONObject();
                    jsonObject1.put("JobListId", startJobListAdapter.getSelected().get(i).getJobListId());
                    jsonArray.put(jsonObject1);
                }
                //JSONArray jsonArrayHistory = new JSONArray(data);
                //itemArray = String.valueOf(jsonArrayHistory);

                itemArray = jsonArray.toString();
                prefManager.setJobListArray(itemArray);

                HashMap<String, RequestBody> map = new HashMap<>();
                map.put("data", NetworkUtils.createPartFromString(prefManager.getJobListArray()));
                //map.put("JobListId", NetworkUtils.createPartFromString(prefManager.getJobId()));
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
                                startActivity(new Intent(StartJobListActivity.this, StopJobListActivity.class));
                                finish();
                                Toast.makeText(StartJobListActivity.this, "Wait some seconds redirect screen....", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
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

}
