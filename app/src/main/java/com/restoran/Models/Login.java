package com.restoran.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Login {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("fio")
    @Expose
    private String fio;
    @SerializedName("svoiZakazi")
    @Expose
    private String svoiZakazi;
    @SerializedName("uroven")
    private String uroven;

    @SerializedName("vib")
    private String  vib;

    public String  getVib() {
        return vib;
    }

    public void setVib(String  vib) {
        this.vib = vib;
    }

    public String getUroven() {
        return uroven;
    }

    public void setUroven(String uroven) {
        this.uroven = uroven;
    }

    public String getSvoiZakazi() {
        return svoiZakazi;
    }

    public void setSvoiZakazi(String svoiZakazi) {
        this.svoiZakazi = svoiZakazi;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }
}
