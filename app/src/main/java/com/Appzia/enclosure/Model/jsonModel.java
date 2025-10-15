package com.Appzia.enclosure.Model;

public class jsonModel {
    String prod_id;
    String sub_prod_id;
    String quantity;

    public jsonModel(String prod_id, String sub_prod_id, String quantity) {
        this.prod_id = prod_id;
        this.sub_prod_id = sub_prod_id;
        this.quantity = quantity;
    }

    public String getProd_id() {
        return prod_id;
    }

    public void setProd_id(String prod_id) {
        this.prod_id = prod_id;
    }

    public String getSub_prod_id() {
        return sub_prod_id;
    }

    public void setSub_prod_id(String sub_prod_id) {
        this.sub_prod_id = sub_prod_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
