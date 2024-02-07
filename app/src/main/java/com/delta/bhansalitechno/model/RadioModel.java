package com.delta.bhansalitechno.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RadioModel {

    @SerializedName("FUId")
    @Expose
    private String fUId;
    @SerializedName("LnNo")
    @Expose
    private String lnNo;
    @SerializedName("RecordId")
    @Expose
    private String recordId;
    @SerializedName("FormName")
    @Expose
    private String formName;
    @SerializedName("FilePath")
    @Expose
    private String filePath;
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
    @SerializedName("FileName")
    @Expose
    private String fileName;
    @SerializedName("imageType")
    @Expose
    private String imageType;
    @SerializedName("FileTypeTextListId")
    @Expose
    private String fileTypeTextListId;
    @SerializedName("PDFFile")
    @Expose
    private String pDFFile;
    @SerializedName("FileType")
    @Expose
    private String fileType;

    public RadioModel(String fUId, String lnNo, String recordId, String formName, String filePath, String insertedOn,
                      String lastUpdatedOn, String insertedByUserId, String lastUpdatedByUserId, String fileName,
                      String imageType, String fileTypeTextListId, String pDFFile, String fileType) {
        this.fUId = fUId;
        this.lnNo = lnNo;
        this.recordId = recordId;
        this.formName = formName;
        this.filePath = filePath;
        this.insertedOn = insertedOn;
        this.lastUpdatedOn = lastUpdatedOn;
        this.insertedByUserId = insertedByUserId;
        this.lastUpdatedByUserId = lastUpdatedByUserId;
        this.fileName = fileName;
        this.imageType = imageType;
        this.fileTypeTextListId = fileTypeTextListId;
        this.pDFFile = pDFFile;
        this.fileType = fileType;
    }

    public String getFUId() {
        return fUId;
    }

    public void setFUId(String fUId) {
        this.fUId = fUId;
    }

    public String getLnNo() {
        return lnNo;
    }

    public void setLnNo(String lnNo) {
        this.lnNo = lnNo;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getFileTypeTextListId() {
        return fileTypeTextListId;
    }

    public void setFileTypeTextListId(String fileTypeTextListId) {
        this.fileTypeTextListId = fileTypeTextListId;
    }

    public String getPDFFile() {
        return pDFFile;
    }

    public void setPDFFile(String pDFFile) {
        this.pDFFile = pDFFile;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
