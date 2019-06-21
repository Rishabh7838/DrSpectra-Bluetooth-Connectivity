package com.example.rish.drspectrabt.Model;

public class Model_Detail {
    public String mode_name;
    public String mode_query;

    public Model_Detail() {
    }

    public Model_Detail(String mode_name, String mode_query) {
        this.mode_name = mode_name;
        this.mode_query = mode_query;
    }

    public String getMode_name() {
        return mode_name;
    }

    public void setMode_name(String mode_name) {
        this.mode_name = mode_name;
    }

    public String getMode_query() {
        return mode_query;
    }

    public void setMode_query(String mode_query) {
        this.mode_query = mode_query;
    }
}
