package com.delta.bhansalitechno.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LocationModel {

    @SerializedName("LocId")
    @Expose
    private String locId;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("ParentLocId")
    @Expose
    private String parentLocId;
    @SerializedName("QtyMax")
    @Expose
    private String qtyMax;
    @SerializedName("IsDefault")
    @Expose
    private String isDefault;
    @SerializedName("ViewFor")
    @Expose
    private String viewFor;
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

    public String getLocId() {
        return locId;
    }

    public void setLocId(String locId) {
        this.locId = locId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentLocId() {
        return parentLocId;
    }

    public void setParentLocId(String parentLocId) {
        this.parentLocId = parentLocId;
    }

    public String getQtyMax() {
        return qtyMax;
    }

    public void setQtyMax(String qtyMax) {
        this.qtyMax = qtyMax;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getViewFor() {
        return viewFor;
    }

    public void setViewFor(String viewFor) {
        this.viewFor = viewFor;
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
}
