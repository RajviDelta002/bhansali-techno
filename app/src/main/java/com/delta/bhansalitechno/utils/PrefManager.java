package com.delta.bhansalitechno.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import static com.delta.bhansalitechno.utils.AppConfig.KEY_EMPTY_STRING;
import static com.delta.bhansalitechno.utils.AppConfig.KEY_NULL;
import static com.delta.bhansalitechno.utils.AppConfig.PREF_COMMON_CUSTOMER_CITY;
import static com.delta.bhansalitechno.utils.AppConfig.PREF_COMMON_CUSTOMER_CODE;
import static com.delta.bhansalitechno.utils.AppConfig.PREF_COMMON_CUSTOMER_NAME;
import static com.delta.bhansalitechno.utils.AppConfig.PREF_CUSTOMER_CODE;
import static com.delta.bhansalitechno.utils.AppConfig.PREF_CUSTOMER_NAME;
import static com.delta.bhansalitechno.utils.AppConfig.PREF_DATA_VERSION;
import static com.delta.bhansalitechno.utils.AppConfig.PREF_REQ_IMAGE;
import static com.delta.bhansalitechno.utils.AppConfig.PREF_COMPLAINT_ENTRY_IMG_1;
import static com.delta.bhansalitechno.utils.AppConfig.PREF_COMPLAINT_ENTRY_IMG_2;
import static com.delta.bhansalitechno.utils.AppConfig.PREF_COMPLAINT_ENTRY_IMG_3;
import static com.delta.bhansalitechno.utils.AppConfig.PREF_COMPLAINT_ENTRY_IMG_4;
import static com.delta.bhansalitechno.utils.AppConfig.PREF_COVER_IMAGE;
import static com.delta.bhansalitechno.utils.AppConfig.PREF_DEALER_DISTRIBUTOR_ID;
import static com.delta.bhansalitechno.utils.AppConfig.PREF_EMAIL_ID;
import static com.delta.bhansalitechno.utils.AppConfig.PREF_FIRST_NAME;
import static com.delta.bhansalitechno.utils.AppConfig.PREF_HASH_KEY;
import static com.delta.bhansalitechno.utils.AppConfig.PREF_IMEI_NO;
import static com.delta.bhansalitechno.utils.AppConfig.PREF_INVOICE_IMAGE;
import static com.delta.bhansalitechno.utils.AppConfig.PREF_IS_ADMIN;
import static com.delta.bhansalitechno.utils.AppConfig.PREF_IS_DISABLED;
import static com.delta.bhansalitechno.utils.AppConfig.PREF_IS_FIRST_TIME;
import static com.delta.bhansalitechno.utils.AppConfig.PREF_LAST_NAME;
import static com.delta.bhansalitechno.utils.AppConfig.PREF_LR_IMAGE;
import static com.delta.bhansalitechno.utils.AppConfig.PREF_MOBILE_NO;
import static com.delta.bhansalitechno.utils.AppConfig.PREF_OTP;
import static com.delta.bhansalitechno.utils.AppConfig.PREF_PASSWORD;
import static com.delta.bhansalitechno.utils.AppConfig.PREF_PROFILE_IMAGE;
import static com.delta.bhansalitechno.utils.AppConfig.PREF_STORE_DATA_BANNER_OFFLINE;
import static com.delta.bhansalitechno.utils.AppConfig.PREF_STORE_DATA_ITEM_GROUP_OFFLINE;
import static com.delta.bhansalitechno.utils.AppConfig.PREF_STORE_DATA_TOP_CUSTOMER_PRODUCT;
import static com.delta.bhansalitechno.utils.AppConfig.PREF_STORE_DATA_TOP_KDH_PRODUCT;
import static com.delta.bhansalitechno.utils.AppConfig.PREF_STORE_DATA_TOP_PRODUCT_OFFLINE;
import static com.delta.bhansalitechno.utils.AppConfig.PREF_USER_ID;
import static com.delta.bhansalitechno.utils.AppConfig.PREF_USER_NAME;
import static com.delta.bhansalitechno.utils.AppConfig.PREF_USER_TYPE;
import static com.delta.bhansalitechno.utils.AppConfig.PREF_USER_TYPE_STRING;
import static com.delta.bhansalitechno.utils.AppConfig.PREF_USER_TYPE_TEXT_LIST_ID;

public class PrefManager {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public Context context;

    //Constructor
    public PrefManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
    }

    public void clearPrefByKey(String Key) {
        editor = sharedPreferences.edit();
        editor.remove(Key).apply();
    }

    public void setFirstTime(boolean isFirstTime) {
        editor = sharedPreferences.edit();
        editor.putBoolean(PREF_IS_FIRST_TIME, isFirstTime);
        editor.apply();
    }

    public boolean isFirstTime() {
        return sharedPreferences.getBoolean(PREF_IS_FIRST_TIME, true);
    }

    public void setIMEI(String imeiNo) {
        editor = sharedPreferences.edit();
        editor.putString(PREF_IMEI_NO, imeiNo);
        editor.apply();
    }

    public String getIMEI() {
        return sharedPreferences.getString(PREF_IMEI_NO, KEY_EMPTY_STRING);
    }

    public void setStoreDataBannerOffline(boolean offline) {
        editor = sharedPreferences.edit();
        editor.putBoolean(PREF_STORE_DATA_BANNER_OFFLINE, offline);
        editor.apply();
    }

    public boolean getStoreDataBannerOffline() {
        return sharedPreferences.getBoolean(PREF_STORE_DATA_BANNER_OFFLINE, false);
    }

    public void setStoreDataTopProductOffline(boolean offline) {
        editor = sharedPreferences.edit();
        editor.putBoolean(PREF_STORE_DATA_TOP_PRODUCT_OFFLINE, offline);
        editor.apply();
    }

    public boolean getStoreDataTopProductOffline() {
        return sharedPreferences.getBoolean(PREF_STORE_DATA_TOP_PRODUCT_OFFLINE, false);
    }

    public void setStoreDataItemGroupOffline(boolean offline) {
        editor = sharedPreferences.edit();
        editor.putBoolean(PREF_STORE_DATA_ITEM_GROUP_OFFLINE, offline);
        editor.apply();
    }

    public boolean getStoreDataItemGroupOffline() {
        return sharedPreferences.getBoolean(PREF_STORE_DATA_ITEM_GROUP_OFFLINE, false);
    }

    public void setStoreDataTop10CustomerOffline(boolean offline) {
        editor = sharedPreferences.edit();
        editor.putBoolean(PREF_STORE_DATA_TOP_CUSTOMER_PRODUCT, offline);
        editor.apply();
    }

    public boolean getStoreDataTop10CustomerOffline() {
        return sharedPreferences.getBoolean(PREF_STORE_DATA_TOP_CUSTOMER_PRODUCT, false);
    }

    public void setStoreDataTop10KDHOffline(boolean offline) {
        editor = sharedPreferences.edit();
        editor.putBoolean(PREF_STORE_DATA_TOP_KDH_PRODUCT, offline);
        editor.apply();
    }

    public boolean getStoreDataTop10KDHOffline() {
        return sharedPreferences.getBoolean(PREF_STORE_DATA_TOP_KDH_PRODUCT, false);
    }

    public void setFcmId(String fcmId) {
        editor = sharedPreferences.edit();
        editor.putString("FcmId", fcmId);
        editor.apply();
    }

    public String getFcmId() {
        return sharedPreferences.getString("FcmId", KEY_EMPTY_STRING);
    }

    public void setDataVersion(String dataVersion) {
        editor = sharedPreferences.edit();
        editor.putString(PREF_DATA_VERSION, dataVersion);
        editor.apply();
    }

    public String getDataVersion() {
        return sharedPreferences.getString(PREF_DATA_VERSION, KEY_EMPTY_STRING);
    }

    public void setLoggedIn(String isLogin) {
        editor = sharedPreferences.edit();
        editor.putString("IsLogin", isLogin);
        editor.apply();
    }

    public String isLoggedIn() {
        return sharedPreferences.getString("IsLogin", "False");
    }

    public void setUserName(String userName) {
        editor = sharedPreferences.edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.apply();
    }

    public String getUserName() {
        return sharedPreferences.getString(PREF_USER_NAME, KEY_EMPTY_STRING);
    }

    public void setUserId(String userId) {
        editor = sharedPreferences.edit();
        editor.putString(PREF_USER_ID, userId);
        editor.apply();
    }

    public String getUserId() {
        return sharedPreferences.getString(PREF_USER_ID, KEY_EMPTY_STRING);
    }

    public void setOtp(String otp) {
        editor = sharedPreferences.edit();
        editor.putString(PREF_OTP, otp);
        editor.apply();
    }

    public String getOtp() {
        return sharedPreferences.getString(PREF_OTP, KEY_EMPTY_STRING);
    }

    public void setFirstName(String firstName) {
        editor = sharedPreferences.edit();
        editor.putString(PREF_FIRST_NAME, firstName);
        editor.apply();
    }

    public String getFirstName() {
        return sharedPreferences.getString(PREF_FIRST_NAME, KEY_EMPTY_STRING);
    }

    public void setLastName(String lastName) {
        editor = sharedPreferences.edit();
        editor.putString(PREF_LAST_NAME, lastName);
        editor.apply();
    }

    public void setUserTypeString(String userTypeString) {
        editor = sharedPreferences.edit();
        editor.putString(PREF_USER_TYPE_STRING, userTypeString);
        editor.apply();
    }

    public String getUserTypeString() {
        return sharedPreferences.getString(PREF_USER_TYPE_STRING, KEY_EMPTY_STRING);
    }

    public String getLastName() {
        return sharedPreferences.getString(PREF_LAST_NAME, KEY_EMPTY_STRING);
    }

    public void setPassword(String password) {
        editor = sharedPreferences.edit();
        editor.putString(PREF_PASSWORD, password);
        editor.apply();
    }

    public String getPassword() {
        return sharedPreferences.getString(PREF_PASSWORD, KEY_EMPTY_STRING);
    }

    public void setMobileNo(String mobileNo) {
        editor = sharedPreferences.edit();
        editor.putString(PREF_MOBILE_NO, mobileNo);
        editor.apply();
    }

    public String getMobileNo() {
        return sharedPreferences.getString(PREF_MOBILE_NO, KEY_EMPTY_STRING);
    }

    public void setUserTypeTextListId(String userTypeTextListId) {
        editor = sharedPreferences.edit();
        editor.putString(PREF_USER_TYPE_TEXT_LIST_ID, userTypeTextListId);
        editor.apply();
    }

    public String getUserTypeTextListId() {
        return sharedPreferences.getString(PREF_USER_TYPE_TEXT_LIST_ID, KEY_EMPTY_STRING);
    }

    public void setEmailId(String emailId) {
        editor = sharedPreferences.edit();
        editor.putString(PREF_EMAIL_ID, emailId);
        editor.apply();
    }

    public String getEmailId() {
        return sharedPreferences.getString(PREF_EMAIL_ID, KEY_EMPTY_STRING);
    }

    public void setIsDisabled(String isDisabled) {
        editor = sharedPreferences.edit();
        editor.putString(PREF_IS_DISABLED, isDisabled);
        editor.apply();
    }

    public String getIsDisabled() {
        return sharedPreferences.getString(PREF_IS_DISABLED, KEY_EMPTY_STRING);
    }

    public void setIsAdmin(String isAdmin) {
        editor = sharedPreferences.edit();
        editor.putString(PREF_IS_ADMIN, isAdmin);
        editor.apply();
    }

    public String getIsAdmin() {
        return sharedPreferences.getString(PREF_IS_ADMIN, KEY_EMPTY_STRING);
    }

    public void setUserType(String userType) {
        editor = sharedPreferences.edit();
        editor.putString(PREF_USER_TYPE, userType);
        editor.apply();
    }

    public String getUserType() {
        return sharedPreferences.getString(PREF_USER_TYPE, KEY_EMPTY_STRING);
    }

    public void setComplaintEntry1(String userType) {
        editor = sharedPreferences.edit();
        editor.putString(PREF_COMPLAINT_ENTRY_IMG_1, userType);
        editor.apply();
    }

    public String getComplaintEntry1() {
        return sharedPreferences.getString(PREF_COMPLAINT_ENTRY_IMG_1, KEY_EMPTY_STRING);
    }

    public void setComplaintEntry2(String userType) {
        editor = sharedPreferences.edit();
        editor.putString(PREF_COMPLAINT_ENTRY_IMG_2, userType);
        editor.apply();
    }

    public String getComplaintEntry2() {
        return sharedPreferences.getString(PREF_COMPLAINT_ENTRY_IMG_2, KEY_EMPTY_STRING);
    }

    public void setComplaintEntry3(String userType) {
        editor = sharedPreferences.edit();
        editor.putString(PREF_COMPLAINT_ENTRY_IMG_3, userType);
        editor.apply();
    }

    public String getComplaintEntry3() {
        return sharedPreferences.getString(PREF_COMPLAINT_ENTRY_IMG_3, KEY_EMPTY_STRING);
    }

    public void setComplaintEntry4(String userType) {
        editor = sharedPreferences.edit();
        editor.putString(PREF_COMPLAINT_ENTRY_IMG_4, userType);
        editor.apply();
    }

    public String getComplaintEntry4() {
        return sharedPreferences.getString(PREF_COMPLAINT_ENTRY_IMG_4, KEY_EMPTY_STRING);
    }

    public void setProfileImage(String profileImage) {
        editor = sharedPreferences.edit();
        editor.putString(PREF_PROFILE_IMAGE, profileImage);
        editor.apply();
    }

    public String getProfileImage() {
        return sharedPreferences.getString(PREF_PROFILE_IMAGE, KEY_EMPTY_STRING);
    }

    public void setInvoiceImage(String InvoiceImage) {
        editor = sharedPreferences.edit();
        editor.putString(PREF_INVOICE_IMAGE, InvoiceImage);
        editor.apply();
    }

    public String getInvoiceImage() {
        return sharedPreferences.getString(PREF_INVOICE_IMAGE, KEY_EMPTY_STRING);
    }


    public void setLRImage(String LRImage) {
        editor = sharedPreferences.edit();
        editor.putString(PREF_LR_IMAGE, LRImage);
        editor.apply();
    }

    public String getLRImage() {
        return sharedPreferences.getString(PREF_LR_IMAGE, KEY_EMPTY_STRING);
    }


    public void setCoverImage(String coverImage) {
        editor = sharedPreferences.edit();
        editor.putString(PREF_COVER_IMAGE, coverImage);
        editor.apply();
    }

    public String getCoverImage() {
        return sharedPreferences.getString(PREF_COVER_IMAGE, KEY_EMPTY_STRING);
    }

    public void setDealerDistributorId(String coverImage) {
        editor = sharedPreferences.edit();
        editor.putString(PREF_DEALER_DISTRIBUTOR_ID, coverImage);
        editor.apply();
    }

    public String getDealerDistributorId() {
        return sharedPreferences.getString(PREF_DEALER_DISTRIBUTOR_ID, KEY_EMPTY_STRING);
    }

    public void setNextPage(String pageCnt) {
        editor = sharedPreferences.edit();
        editor.putString("CAT_NEXT_PAGE", pageCnt);
        editor.apply();
    }

    public String getNextPage() {
        return sharedPreferences.getString("CAT_NEXT_PAGE", KEY_EMPTY_STRING);
    }

    public void setPreviousPage(String pageCnt) {
        editor = sharedPreferences.edit();
        editor.putString("CAT_PREVIOUS_PAGE", pageCnt);
        editor.apply();
    }

    public String getPreviousPage() {
        return sharedPreferences.getString("CAT_PREVIOUS_PAGE", KEY_EMPTY_STRING);
    }

    public void setHasKey(String hasKey) {
        editor = sharedPreferences.edit();
        editor.putString(PREF_HASH_KEY, hasKey);
        editor.apply();
    }

    public String getHasKey() {
        return sharedPreferences.getString(PREF_HASH_KEY, KEY_EMPTY_STRING);
    }

    public void setReqImage(String picturePath) {
        editor = sharedPreferences.edit();
        editor.putString(PREF_REQ_IMAGE, picturePath);
        editor.apply();
    }

    public String getReqImage() {
        return sharedPreferences.getString(PREF_REQ_IMAGE, KEY_NULL);
    }

    @SuppressLint("CommitPrefEdits")
    public void setNotiFicationCount(int notiCount) {
        this.editor = this.sharedPreferences.edit();
        this.editor.putString(AppConfig.PREF_NOTIFICATION_COUNT, String.valueOf(notiCount));
        this.editor.apply();
    }

    public String getNotiFicationCount() {
        return this.sharedPreferences.getString(AppConfig.PREF_NOTIFICATION_COUNT, "");
    }

    public void setCustomerName(String customerName) {
        editor = sharedPreferences.edit();
        editor.putString(PREF_CUSTOMER_NAME, customerName);
        editor.apply();
    }

    public String getCustomerName() {
        return sharedPreferences.getString(PREF_CUSTOMER_NAME, KEY_EMPTY_STRING);
    }

    public void setCustomerCode(String customerCode) {
        editor = sharedPreferences.edit();
        editor.putString(PREF_CUSTOMER_CODE, customerCode);
        editor.apply();
    }

    public String getCustomerCode() {
        return sharedPreferences.getString(PREF_CUSTOMER_CODE, KEY_EMPTY_STRING);
    }

    public void setCommonCustomerCode(String commonCustomerCode) {
        editor = sharedPreferences.edit();
        editor.putString(PREF_COMMON_CUSTOMER_CODE, commonCustomerCode);
        editor.apply();
    }

    public String getCommonCustomerCode() {
        return sharedPreferences.getString(PREF_COMMON_CUSTOMER_CODE, KEY_EMPTY_STRING);
    }

    public void setCommonCustomerName(String commonCustomerName) {
        editor = sharedPreferences.edit();
        editor.putString(PREF_COMMON_CUSTOMER_NAME, commonCustomerName);
        editor.apply();
    }

    public String getCommonCustomerName() {
        return sharedPreferences.getString(PREF_COMMON_CUSTOMER_NAME, KEY_EMPTY_STRING);
    }

    public void setCommonCustomerCity(String commonCustomerCode) {
        editor = sharedPreferences.edit();
        editor.putString(PREF_COMMON_CUSTOMER_CITY, commonCustomerCode);
        editor.apply();
    }

    public String getCommonCustomerCity() {
        return sharedPreferences.getString(PREF_COMMON_CUSTOMER_CITY, KEY_EMPTY_STRING);
    }

    public void setPdfCount(String pdfCount) {
        editor = sharedPreferences.edit();
        editor.putString("PdfCount", pdfCount);
        editor.apply();
    }

    public String getPdfCount() {
        return sharedPreferences.getString("PdfCount", "0");
    }

    public void setJobNo(String jobNo) {
        editor = sharedPreferences.edit();
        editor.putString("JobNo", jobNo);
        editor.apply();
    }

    public String getJobNo() {
        return sharedPreferences.getString("JobNo", KEY_EMPTY_STRING);
    }

    public void setJobId(String jobId) {
        editor = sharedPreferences.edit();
        editor.putString("JobId", jobId);
        editor.apply();
    }

    public String getJobId() {
        return sharedPreferences.getString("JobId", KEY_EMPTY_STRING);
    }

    public void setItmName(String itmName) {
        editor = sharedPreferences.edit();
        editor.putString("ItmName", itmName);
        editor.apply();
    }

    public String getItmName() {
        return sharedPreferences.getString("ItmName", KEY_EMPTY_STRING);
    }

    public void setPlanQty(String planQty) {
        editor = sharedPreferences.edit();
        editor.putString("PlanQty", planQty);
        editor.apply();
    }

    public String getPlanQty() {
        return sharedPreferences.getString("PlanQty", KEY_EMPTY_STRING);
    }

    public void setShop(String shop) {
        editor = sharedPreferences.edit();
        editor.putString("Shop", shop);
        editor.apply();
    }

    public String getShop() {
        return sharedPreferences.getString("Shop", KEY_EMPTY_STRING);
    }

    public void setMachine(String machine) {
        editor = sharedPreferences.edit();
        editor.putString("Machine", machine);
        editor.apply();
    }

    public String getMachine() {
        return sharedPreferences.getString("Machine", KEY_EMPTY_STRING);
    }

    public void setMachineNo(String machineNo) {
        editor = sharedPreferences.edit();
        editor.putString("MachineNo", machineNo);
        editor.apply();
    }

    public String getMachineNo() {
        return sharedPreferences.getString("MachineNo", KEY_EMPTY_STRING);
    }

    public void setMachineCode(String machineCode) {
        editor = sharedPreferences.edit();
        editor.putString("MachineCode", machineCode);
        editor.apply();
    }

    public String getMachineCode() {
        return sharedPreferences.getString("MachineCode", KEY_EMPTY_STRING);
    }

    public void setMachineName(String machineName) {
        editor = sharedPreferences.edit();
        editor.putString("MachineName", machineName);
        editor.apply();
    }

    public String getMachineName() {
        return sharedPreferences.getString("MachineName", KEY_EMPTY_STRING);
    }

    public void setMachineId(String machineId) {
        editor = sharedPreferences.edit();
        editor.putString("MachineId", machineId);
        editor.apply();
    }

    public String getMachineId() {
        return sharedPreferences.getString("MachineId", KEY_EMPTY_STRING);
    }

    public void setProcess(String process) {
        editor = sharedPreferences.edit();
        editor.putString("Process", process);
        editor.apply();
    }

    public String getProcess() {
        return sharedPreferences.getString("Process", KEY_EMPTY_STRING);
    }

    public void setHandlerTime(String handlerTime) {
        editor = sharedPreferences.edit();
        editor.putString("HandlerTime", handlerTime);
        editor.apply();
    }

    public String getHandlerTime() {
        return sharedPreferences.getString("HandlerTime", KEY_EMPTY_STRING);
    }

    public void setStartTime(String startTime) {
        editor = sharedPreferences.edit();
        editor.putString("StartTime", startTime);
        editor.apply();
    }

    public String getStartTime() {
        return sharedPreferences.getString("StartTime", KEY_EMPTY_STRING);
    }

    public void setStopTime(String stopTime) {
        editor = sharedPreferences.edit();
        editor.putString("StopTime", stopTime);
        editor.apply();
    }

    public String getStopTime() {
        return sharedPreferences.getString("StopTime", KEY_EMPTY_STRING);
    }

    public void setMLTime(String mlTime) {
        editor = sharedPreferences.edit();
        editor.putString("MLTime", mlTime);
        editor.apply();
    }

    public String getMLTime() {
        return sharedPreferences.getString("MLTime", KEY_EMPTY_STRING);
    }

    public void setHourTime(String hourTime) {
        editor = sharedPreferences.edit();
        editor.putString("HourTime", hourTime);
        editor.apply();
    }

    public String getHourTime() {
        return sharedPreferences.getString("HourTime", KEY_EMPTY_STRING);
    }

    public void setMinutesTime(String minutesTime) {
        editor = sharedPreferences.edit();
        editor.putString("MinutesTime", minutesTime);
        editor.apply();
    }

    public String getMinutesTime() {
        return sharedPreferences.getString("MinutesTime", KEY_EMPTY_STRING);
    }

    public void setSecondTime(String secondTime) {
        editor = sharedPreferences.edit();
        editor.putString("SecondTime", secondTime);
        editor.apply();
    }

    public String getSecondTime() {
        return sharedPreferences.getString("SecondTime", KEY_EMPTY_STRING);
    }

    public void setJobListArray(String jobListArray) {
        editor = sharedPreferences.edit();
        editor.putString("JobListArray", jobListArray);
        editor.apply();
    }

    public String getJobListArray() {
        return sharedPreferences.getString("JobListArray", KEY_EMPTY_STRING);
    }
}
