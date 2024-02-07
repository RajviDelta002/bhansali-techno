package com.delta.bhansalitechno.api;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.delta.bhansalitechno.interfaces.ApiInterface;
import com.delta.bhansalitechno.interfaces.OnResponse;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.delta.bhansalitechno.utils.AppConfig.BASE_URL;
import static com.delta.bhansalitechno.utils.AppConfig.KEY_200;
import static com.delta.bhansalitechno.utils.AppConfig.KEY_209;
import static com.delta.bhansalitechno.utils.AppConfig.KEY_LOADING;
import static com.delta.bhansalitechno.utils.AppConfig.KEY_MSG;
import static com.delta.bhansalitechno.utils.AppConfig.KEY_RESULT;
import static com.delta.bhansalitechno.utils.AppConfig.KEY_STATUS;


public class ApiUtil {

    private static final String TAG = "ApiUtil";

    private Context context;
    private ProgressDialog p = null;
    private OnResponse onResponse;
    private String url;
    private HashMap<String, RequestBody> hashMap;
    private MultipartBody.Part file;

    public ApiUtil(Context context, String url, HashMap<String, RequestBody> hashMap,
                   MultipartBody.Part file, OnResponse onResponse) {

        try {

            this.context = context;
            this.url = url;
            this.hashMap = hashMap;
            this.file = file;
            this.onResponse = onResponse;

            p = new ProgressDialog(context);
            p.setCancelable(false);
            p.setMessage(KEY_LOADING);

        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }

    }

    public void request() {

        try {
            if (context != null) {

                if (p != null && !p.isShowing()) {
                    p.show();
                }

                final ApiInterface apiInterface = ApiClient.getClient(BASE_URL).create(ApiInterface.class);
                Call<ResponseBody> call = null;
                if (hashMap == null) {
                    call = apiInterface.getUrlMap(url);
                } else {
                    call = apiInterface.getUrlMap(url, hashMap, file);
                }

                if (call != null) {

                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                            Log.d(TAG, "request Response Code --> " + response.code());

                            switch (response.code()) {

                                case 200:

                                    try {

                                        if (response.body() != null && response.isSuccessful()) {

                                            String strResBody = response.body().string();

                                            JSONObject jsonObject = new JSONObject(strResBody);

                                            switch (jsonObject.getString(KEY_STATUS)) {

                                                case KEY_200:

                                                    try {
                                                        JSONArray array = jsonObject.getJSONArray(KEY_RESULT);
                                                        onResponse.onSuccess(array);
                                                    } catch (Exception e) {
                                                        onResponse.onSuccess(null);
                                                    }

                                                    if (p != null && p.isShowing()) {
                                                        p.dismiss();
                                                    }

                                                    break;

                                                case KEY_209:
                                                default:

                                                    if (p != null && p.isShowing()) {
                                                        p.dismiss();
                                                    }

                                                    onResponse.on209(jsonObject.getString(KEY_MSG));

                                                    break;
                                            }

                                        } else {

                                            if (p != null && p.isShowing()) {
                                                p.dismiss();
                                            }

                                            onResponse.onNullResponse();

                                        }

                                    } catch (IOException | JSONException e) {

                                        e.printStackTrace();

                                        if (p != null && p.isShowing()) {
                                            p.dismiss();
                                        }

                                        onResponse.onExceptionFired(e.getLocalizedMessage());
                                    }

                                    break;

                                default:

                                    if (p != null && p.isShowing()) {
                                        p.dismiss();
                                    }

                                    onResponse.on400(response.code());

                                    break;
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

                            if (p != null && p.isShowing()) {
                                p.dismiss();
                            }
                            onResponse.onFailure(t);
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }
}
