package com.delta.bhansalitechno.utils;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.delta.bhansalitechno.R;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.delta.bhansalitechno.utils.AppConfig.KEY_2A;
import static com.delta.bhansalitechno.utils.AppConfig.KEY_AFOSTROPHE;
import static com.delta.bhansalitechno.utils.AppConfig.KEY_ASTERISK;
import static com.delta.bhansalitechno.utils.AppConfig.KEY_CB;
import static com.delta.bhansalitechno.utils.AppConfig.KEY_EMPER;
import static com.delta.bhansalitechno.utils.AppConfig.KEY_OB;
import static com.delta.bhansalitechno.utils.AppConfig.KEY_SDF;
import static com.delta.bhansalitechno.utils.AppConfig.KEY_U0026;
import static com.delta.bhansalitechno.utils.AppConfig.KEY_U0027;
import static com.delta.bhansalitechno.utils.AppConfig.KEY_U005B;
import static com.delta.bhansalitechno.utils.AppConfig.KEY_U005D;

public class Utils {

    //Method : Hide Status Bar For Activity
    public static void hideStatusBar(Activity a) {
        View decorView = a.getWindow().getDecorView();

        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
        ActionBar actionBar = a.getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    public static void changeStatusBar(Activity activity) {

        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ///Change Status Bar Color
                Window window = activity.getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ContextCompat.getColor(activity, R.color.white));

                //Status Bar Icon
                int flags = activity.getWindow().getDecorView().getSystemUiVisibility();
                flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                activity.getWindow().getDecorView().setSystemUiVisibility(flags);
                activity.getWindow().setStatusBarColor(Color.WHITE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @NonNull
    public static String getReplacedString(@NonNull String convertString) {
        if (convertString != null) {
            convertString = convertString.replace(KEY_U0027, KEY_AFOSTROPHE).replace(KEY_U0026, KEY_EMPER).replace(KEY_U005B, KEY_OB).replace(KEY_U005D, KEY_CB);
        } else {
            convertString = "";
        }
        return convertString;
    }

    @NonNull
    public static String getReplacedStringNew(@NonNull String convertString) {
        if (convertString != null) {
            convertString = convertString.replace(KEY_U0027, KEY_AFOSTROPHE).replace(KEY_U0026, KEY_EMPER).replace(KEY_U005B, KEY_OB).replace(KEY_U005D, KEY_CB)
                    .replace(KEY_2A, KEY_ASTERISK);
        } else {
            convertString = "";
        }
        return convertString;
    }

    public static void showKeyboard(Context context, EditText txt) {
        InputMethodManager mgr = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (mgr != null) {
            mgr.showSoftInput(txt, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public static void initStatusBar(Activity activity) {
        try {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dismissDialog(ProgressDialog progressDialog) {
        if ((progressDialog != null) && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    //Get MonthName
    public static String getMonthName(String s) {
        switch (s) {
            case "1":
                s = "Jan";
                break;
            case "2":
                s = "Feb";
                break;
            case "3":
                s = "Mar";
                break;
            case "4":
                s = "Apr";
                break;
            case "5":
                s = "May";
                break;
            case "6":
                s = "Jun";
                break;
            case "7":
                s = "Jul";
                break;
            case "8":
                s = "Aug";
                break;
            case "9":
                s = "Sep";
                break;
            case "10":
                s = "Oct";
                break;
            case "11":
                s = "Nov";
                break;
            case "12":
                s = "Dec";
                break;
        }
        return s;
    }

    //Method --> Hide Keyboard
    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            View view = activity.getCurrentFocus();
            if (view == null) {
                view = new View(activity);
            }
            if (imm != null)
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isEmpty(String string) {
        if (TextUtils.isEmpty(string) || string.equals("") || string.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public static String convertFirstLetterCap(String str) {

        // Create a char array of given String
        char ch[] = str.toCharArray();
        for (int i = 0; i < str.length(); i++) {

            // If first character of a word is found
            if (i == 0 && ch[i] != ' ' ||
                    ch[i] != ' ' && ch[i - 1] == ' ') {

                // If it is in lower-case
                if (ch[i] >= 'a' && ch[i] <= 'z') {

                    // Convert into Upper-case
                    ch[i] = (char) (ch[i] - 'a' + 'A');
                }
            }

            // If apart from first character
            // Any one is in Upper-case
            else if (ch[i] >= 'A' && ch[i] <= 'Z')

                // Convert into Lower-Case
                ch[i] = (char) (ch[i] + 'a' - 'A');
        }

        // Convert the char array to equivalent String
        String st = new String(ch);
        return st;
    }


    public static void blink(final TextView txt) {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int timeToBlink = 50;    //in milissegunds
                try {
                    Thread.sleep(timeToBlink);
                } catch (Exception e) {
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (txt.getVisibility() == View.VISIBLE) {
                            txt.setVisibility(View.INVISIBLE);
                        } else {
                            txt.setVisibility(View.VISIBLE);
                        }
                        blink(txt);
                    }
                });
            }
        }).start();
    }

    public static String getNAString(String inputStr) {
        String retnstr = "";

        if (inputStr.isEmpty()) {
            retnstr = "N/A";
        } else {
            retnstr = inputStr;
        }

        return retnstr;
    }

    public static String getDate(String format) {
        return new SimpleDateFormat(format, Locale.ENGLISH).format(new Date());
    }

    public static String getDateofCurrentMonth() {

        String currentmonthdate = null;

        try {

            Calendar c = Calendar.getInstance();   // this takes current date
            c.set(Calendar.DAY_OF_MONTH, 1);
            System.out.println(c.getTime());
            Date date = c.getTime();
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            currentmonthdate = dateFormat.format(date);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return currentmonthdate;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getConvertedDate(String date) {
        SimpleDateFormat format = null;
        if (date.contains("/")) {
            format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        } else {
            format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        }
        Date dates = null;
        try {
            dates = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        format = new SimpleDateFormat("dd-MMM-yyyy");
        String output = null;
        if (dates != null) {
            output = format.format(dates);
            return output;
        } else {
            return "";
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static String getConvertedDateForOrder(String date) {
        SimpleDateFormat format = null;
        if (date.contains("/")) {
            format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        } else {
            format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        }
        Date dates = null;
        try {
            dates = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        format = new SimpleDateFormat("dd-MMM-yyyy");
        String output = null;
        if (dates != null) {
            output = format.format(dates);
            return output;
        } else {
            return "";
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static String getConvertedNewDateForOrder(String date) {
        SimpleDateFormat format = null;
        format = new SimpleDateFormat("dd-MM-yy");
        Date dates = null;
        try {
            dates = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        format = new SimpleDateFormat("dd-MMM-yyyy");
        String output = null;
        if (dates != null) {
            output = format.format(dates);
            return output;
        } else {
            return "";
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static String getNotificationDate(String date) {
        SimpleDateFormat format = null;
        if (date.contains("/")) {
            format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        } else {
            format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        }
        Date dates = null;
        try {
            dates = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        format = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        String output = null;
        if (dates != null) {
            output = format.format(dates);
            return output;
        } else {
            return "";
        }
    }

    public static void hideKeyboardInActivity(Activity activity, EditText txt) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(txt.getWindowToken(), 0);
        }
    }

    public static void goBack(Activity a) {
        if (a != null && !a.isFinishing()) {
            a.finish();
        }
    }

    public static void hideKeyboardInFragment(Context context, View view) {
        if (context != null) {
            final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static String convertDate(String date) {

        try {
            String formattedDate = "";

            DateFormat originalFormat = null;

            if (date.contains("/")) {
                originalFormat = new SimpleDateFormat("M/dd/yyyy hh:mm:ss a", Locale.ENGLISH);
            } else {
                originalFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.ENGLISH);
            }

            Date d = null;

            d = originalFormat.parse(date);
            DateFormat targetFormat = new SimpleDateFormat(KEY_SDF);

            if (d != null) {
                formattedDate = targetFormat.format(d);
            }

            return formattedDate;

        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static String ConvertDateFormat(String inputDate, String inputDateFormat, String outputDateFormat) {
        String outPutData = null;
        try {
            SimpleDateFormat inputParser = new SimpleDateFormat(inputDateFormat,Locale.ENGLISH);
            SimpleDateFormat outputParser = new SimpleDateFormat(outputDateFormat,Locale.ENGLISH);

            Date date;
            date = inputParser.parse(inputDate);
            assert date != null;
            outPutData = outputParser.format(date);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return outPutData;
    }

    public static String getCurrentTimestamp(String dateTimeFormat) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(dateTimeFormat, Locale.ENGLISH);
            return dateFormat.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
            return null;
        }
    }
}
