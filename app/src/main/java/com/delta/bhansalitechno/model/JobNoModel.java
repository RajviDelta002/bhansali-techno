package com.delta.bhansalitechno.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JobNoModel {

    @SerializedName("JobListId")
    @Expose
    private String jobListId;
    @SerializedName("JobNo")
    @Expose
    private String jobNo;
    @SerializedName("Dt")
    @Expose
    private String dt;
    @SerializedName("EmployeeId")
    @Expose
    private String employeeId;
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
    @SerializedName("CycleTime")
    @Expose
    private String cycleTime;
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
    @SerializedName("EmployeeName")
    @Expose
    private String employeeName;
    @SerializedName("ProcessName")
    @Expose
    private String processName;
    @SerializedName("ItmName")
    @Expose
    private String itmName;
    @SerializedName("ShopName")
    @Expose
    private String shopName;
    @SerializedName("MachineName")
    @Expose
    private String machineName;
    @SerializedName("PDFFile")
    @Expose
    private String pdfFile;
    @SerializedName("DoneQty")
    @Expose
    private String doneQty;
    @SerializedName("PendingQty")
    @Expose
    private String pendingQty;

    public JobNoModel(String jobListId, String jobNo, String dt, String employeeId, String processId, String itmId, String startDateTime,
                      String endDateTime, String cycleTime, String qty, String remarks, String insertedOn, String lastUpdatedOn,
                      String insertedByUserId, String lastUpdatedByUserId, String employeeName, String processName, String itmName,
                      String shopName, String machineName, String pdfFile, String doneQty, String pendingQty) {
        this.jobListId = jobListId;
        this.jobNo = jobNo;
        this.dt = dt;
        this.employeeId = employeeId;
        this.processId = processId;
        this.itmId = itmId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.cycleTime = cycleTime;
        this.qty = qty;
        this.remarks = remarks;
        this.insertedOn = insertedOn;
        this.lastUpdatedOn = lastUpdatedOn;
        this.insertedByUserId = insertedByUserId;
        this.lastUpdatedByUserId = lastUpdatedByUserId;
        this.employeeName = employeeName;
        this.processName = processName;
        this.itmName = itmName;
        this.shopName = shopName;
        this.machineName = machineName;
        this.pdfFile = pdfFile;
        this.doneQty = doneQty;
        this.pendingQty = pendingQty;
    }

    public String getJobListId() {
        return jobListId;
    }

    public void setJobListId(String jobListId) {
        this.jobListId = jobListId;
    }

    public String getJobNo() {
        return jobNo;
    }

    public void setJobNo(String jobNo) {
        this.jobNo = jobNo;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
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

    public String getCycleTime() {
        return cycleTime;
    }

    public void setCycleTime(String cycleTime) {
        this.cycleTime = cycleTime;
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

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getItmName() {
        return itmName;
    }

    public void setItmName(String itmName) {
        this.itmName = itmName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getPdfFile() {
        return pdfFile;
    }

    public void setPdfFile(String pdfFile) {
        this.pdfFile = pdfFile;
    }

    public String getDoneQty() {
        return doneQty;
    }

    public void setDoneQty(String doneQty) {
        this.doneQty = doneQty;
    }

    public String getPendingQty() {
        return pendingQty;
    }

    public void setPendingQty(String pendingQty) {
        this.pendingQty = pendingQty;
    }
}
