package com.delta.bhansalitechno.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MachineModel {

    @SerializedName("MachineId")
    @Expose
    private String machineId;
    @SerializedName("MachineNo")
    @Expose
    private String machineNo;
    @SerializedName("MachineCode")
    @Expose
    private String machineCode;
    @SerializedName("MachineName")
    @Expose
    private String machineName;

    public MachineModel(String machineId, String machineNo, String machineCode, String machineName) {
        this.machineId = machineId;
        this.machineNo = machineNo;
        this.machineCode = machineCode;
        this.machineName = machineName;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getMachineNo() {
        return machineNo;
    }

    public void setMachineNo(String machineNo) {
        this.machineNo = machineNo;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

}
