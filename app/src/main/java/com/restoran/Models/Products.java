package com.restoran.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Products {

    @SerializedName("products")
    @Expose
    private List<Product> products = null;

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public static class Product {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;

        @SerializedName("price")
        @Expose
        private String price;


        @SerializedName("type")
        @Expose
        private String type;

        @SerializedName("id_cat")
        @Expose
        private String idCat;
        @SerializedName("chast")
        @Expose
        private List<Chast> chast = null;
        @SerializedName("vesovoy")
        @Expose
        private int vesovoy;

        public int getVesovoy() {
            return vesovoy;
        }

        public void setVesovoy(int vesovoy) {
            this.vesovoy = vesovoy;
        }

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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getIdCat() {
            return idCat;
        }

        public void setIdCat(String idCat) {
            this.idCat = idCat;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String prise) {
            this.price = prise;
        }

        public List<Chast> getChast() {
            return chast;
        }

        public void setChast(List<Chast> chast) {
            this.chast = chast;
        }

        public class Chast {

            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("name")
            @Expose
            private String name;
            @SerializedName("price")
            @Expose
            private String price;

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

            public String getPrice() {
                return price;
            }

            public void setPrice(String prise) {
                this.price = prise;
            }

        }

    }
}