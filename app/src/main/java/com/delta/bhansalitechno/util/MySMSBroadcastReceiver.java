package com.delta.bhansalitechno.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


import com.delta.bhansalitechno.interfaces.SmsListener;
import com.delta.bhansalitechno.utils.PrefManager;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

import java.util.Objects;

public class MySMSBroadcastReceiver extends BroadcastReceiver {

    public static SmsListener smsListener;
    private static Context ctx;
    private  static PrefManager prefManager;

    public static void bindListener(SmsListener listener, Context context){
        smsListener = listener;
        ctx = context;
        prefManager = new PrefManager(ctx);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction()))
        {

            Bundle extras = intent.getExtras();

            Status status = (Status) Objects.requireNonNull(extras).get(SmsRetriever.EXTRA_STATUS);

            switch (Objects.requireNonNull(status).getStatusCode())
            {

                case CommonStatusCodes.SUCCESS:

                    String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);

                    //message.replaceAll(prefManager.getHasKey(),"");

                    Log.d("TAG", "onReceive: "+message);

                    String numberOnly = "";
                    if (!message.equals("") && message.contains("Labh Cart")) {
                        numberOnly = message.substring(4, 8);
                    }

                    smsListener.onMessageReceived(numberOnly);

                    break;

                case CommonStatusCodes.TIMEOUT:

                    Log.d("TAG", "onReceive: Fail");

                    break;
            }
        }
    }
}
