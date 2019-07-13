package com.restoran.Models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ZalStol {

    @SerializedName("zals")
    @Expose
    private List<Zal> zals = null;

    public List<Zal> getZals() {
        return zals;
    }

    public void setZals(List<Zal> zals) {
        this.zals = zals;
    }
    public class Zal {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("stol")
        @Expose
        private List<Stol> stol = null;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Stol> getStol() {
            return stol;
        }

        public void setStol(List<Stol> stol) {
            this.stol = stol;
        }

    }

    public class Stol {
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("status")
        @Expose
        private  boolean status;

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public String getId() {
            return id;
        }

        public void setId(String  id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}


