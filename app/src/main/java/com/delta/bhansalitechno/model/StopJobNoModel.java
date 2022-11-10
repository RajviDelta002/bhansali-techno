package com.delta.bhansalitechno.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StopJobNoModel {

    @SerializedName("JobListStartStopId")
    @Expose
    private String jobListStartStopId;
    @SerializedName("JobListId")
    @Expose
    private String jobListId;
    @SerializedName("ActivityByEmployeeId")
    @Expose
    private String activityByEmployeeId;
    @SerializedName("ProcessId")
    @Expose
    private String processId;
    @SerializedName("ItmId")
    @Expose
    private String itmId;
    @SerializedName("StartDateTime")
    @Expose
    private String startDateTime;
    @SerializedName("EndDateTime")
    @Expose
    private String endDateTime;
    @SerializedName("Qty")
    @Expose
    private String qty;
    @SerializedName("Remarks")
    @Expose
    private String remarks;
    @SerializedName("InsertedOn")
    @Expose
    private String insertedOn;
    @SerializedName("LastUpdatedOn")
    @Expose
    private String lastUpdatedOn;
    @SerializedName("InsertedByUserId")
    @Expose
    private String insertedByUserId;
    @SerializedName("LastUpdatedByUserId")
    @Expose
    private String lastUpdatedByUserId;
    @SerializedName("PDFFile")
    @Expose
    private String PDFFile;
    @SerializedName("JobNo")
    @Expose
    private String JobNo;
    @SerializedName("EmployeeName")
    @Expose
    private String EmployeeName;
    @SerializedName("ProcessName")
    @Expose
    private String ProcessName;
    @SerializedName("ItmName")
    @Expose
    private String ItmName;
    @SerializedName("ShopName")
    @Expose
    private String ShopName;
    @SerializedName("MachineName")
    @Expose
    private String MachineName;
    @SerializedName("PlanQty")
    @Expose
    private String PlanQty;

    public StopJobNoModel(String jobListStartStopId, String jobListId, String activityByEmployeeId, String processId, String itmId,
                          String startDateTime, String endDateTime, String qty, String remarks, String insertedOn, String lastUpdatedOn,
                          String insertedByUserId, String lastUpdatedByUserId, String pDFFile, String jobNo, String employeeName,
                          String processName, String itmName, String shopName, String machineName, String PlanQty) {
        this.jobListStartStopId = jobListStartStopId;
        this.jobListId = jobListId;
        this.activityByEmployeeId = activityByEmployeeId;
        this.processId = processId;
        this.itmId = itmId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.qty = qty;
        this.remarks = remarks;
        this.insertedOn = insertedOn;
        this.lastUpdatedOn = lastUpdatedOn;
        this.insertedByUserId = insertedByUserId;
        this.lastUpdatedByUserId = lastUpdatedByUserId;
        this.PDFFile = pDFFile;
        JobNo = jobNo;
        EmployeeName = employeeName;
        ProcessName = processName;
        ItmName = itmName;
        ShopName = shopName;
        MachineName = machineName;
        this.PlanQty = PlanQty;
    }

    public String getJobListStartStopId() {
        return jobListStartStopId;
    }

    public void setJobListStartStopId(String jobListStartStopId) {
        this.jobListStartStopId = jobListStartStopId;
    }

    public String getJobListId() {
        return jobListId;
    }

    public void setJobListId(String jobListId) {
        this.jobListId = jobListId;
    }

    public String getActivityByEmployeeId() {
        return activityByEmployeeId;
    }

    public void setActivityByEmployeeId(String activityByEmployeeId) {
        this.activityByEmployeeId = activityByEmployeeId;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getItmId() {
        return itmId;
    }

    public void setItmId(String itmId) {
        this.itmId = itmId;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getInsertedOn() {
        return insertedOn;
    }

    public void setInsertedOn(String insertedOn) {
        this.insertedOn = insertedOn;
    }

    public String getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    public void setLastUpdatedOn(String lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
    }

    public String getInsertedByUserId() {
        return insertedByUserId;
    }

    public void setInsertedByUserId(String insertedByUserId) {
        this.insertedByUserId = insertedByUserId;
    }

    public String getLastUpdatedByUserId() {
        return lastUpdatedByUserId;
    }

    public void setLastUpdatedByUserId(String lastUpdatedByUserId) {
        this.lastUpdatedByUserId = lastUpdatedByUserId;
    }

    public String getPDFFile() {
        return PDFFile;
    }

    public void setPDFFile(String PDFFile) {
        this.PDFFile = PDFFile;
    }

    public String getJobNo() {
        return JobNo;
    }

    public void setJobNo(String jobNo) {
        JobNo = jobNo;
    }

    public String getEmployeeName() {
        return EmployeeName;
    }

    public void setEmployeeName(String employeeName) {
        EmployeeName = employeeName;
    }

    public String getProcessName() {
        return ProcessName;
    }

    public void setProcessName(String processName) {
        ProcessName = processName;
    }

    public String getItmName() {
        return ItmName;
    }

    public void setItmName(String itmName) {
        ItmName = itmName;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String shopName) {
        ShopName = shopName;
    }

    public String getMachineName() {
        return MachineName;
    }

    public void setMachineName(String machineName) {
        MachineName = machineName;
    }

    public String getPlanQty() {
        return PlanQty;
    }

    public void setPlanQty(String planQty) {
        PlanQty = planQty;
    }
}
