package com.restoran.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Orders {
    @SerializedName("id_zakaz")
    @Expose
    String id_zakaz;

    @SerializedName("id_stol")
    @Expose
    private String idStol;
    @SerializedName("kol_gost")
    @Expose
    private String kol_gost;
    @SerializedName("id_afitsant")
    @Expose
    private String id_afisant;
    @SerializedName("id_zal")
    @Expose
    private String id_zal;


    public String getId_zakaz() {
        return id_zakaz;
    }

    public void setId_zakaz(String id_zakaz) {
        this.id_zakaz = id_zakaz;
    }

    public String getKol_gost() {
        return kol_gost;
    }

    public void setKol_gost(String kol_gost) {
        this.kol_gost = kol_gost;
    }

    public String getId_afisant() {
        return id_afisant;
    }

    public void setId_afisant(String id_afisant) {
        this.id_afisant = id_afisant;
    }

    public String getId_zal() {
        return id_zal;
    }

    public void setId_zal(String id_zal) {
        this.id_zal = id_zal;
    }

    public String getIdStol() {
        return idStol;
    }

    public void setIdStol(String idStol) {
        this.idStol = idStol;
    }

    @SerializedName("orders")
    @Expose
    List<Order> orderList = new ArrayList<>();

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    public static class Order {
        private String id;
        private String idProduct;
        private String name;
        private String price;
        private String type;
        private int kol;
        private String idChast;
        private String narh;
        private int gramm;
        private int print;
        private String variant;

        @SerializedName("kombo")
        @Expose
        private List<Order> kombo = new ArrayList<>();
        @SerializedName("status")
        @Expose
        private String status = "";

        public String getVariant() {
            return variant;
        }

        public void setVariant(String variant) {
            this.variant = variant;
        }

        public int getPrint() {
            return print;
        }

        public void setPrint(int print) {
            this.print = print;
        }


        public String getNarh() {
            return narh;
        }

        public void setNarh(String narh) {
            this.narh = narh;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getGramm() {
            return gramm;
        }

        public void setGramm(int gramm) {
            this.gramm = gramm;
        }

        public String getIdChast() {
            return idChast;
        }

        public void setIdChast(String idChast) {
            this.idChast = idChast;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIdProduct() {
            return idProduct;
        }

        public void setIdProduct(String idProduct) {
            this.idProduct = idProduct;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public int getKol() {
            return kol;
        }

        public void setKol(int kol) {
            this.kol = kol;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<Order> getKombo() {
            return kombo;
        }


        public void setKombo(List<Order> kobo) {
            this.kombo = kobo;
        }
    }


}

