package com.delta.bhansalitechno.interfaces;

import androidx.annotation.NonNull;

import org.json.JSONArray;

public interface OnResponse {

    void onSuccess(JSONArray jsonArray);

    void on209(String message);

    void onNullResponse();

    void onExceptionFired(String error);

    void on400(int responseCode);

    void onFailure(@NonNull Throwable t);
}
