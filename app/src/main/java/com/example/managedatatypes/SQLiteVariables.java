package com.example.managedatatypes;

public class SQLiteVariables {

    private String dateusers;
    private String nameusers;
    private String emaiusers;
    private String passusers;

    public SQLiteVariables() {
    }

    public SQLiteVariables(String dateusers, String nameusers, String emaiusers, String passusers) {
        this.dateusers = dateusers;
        this.nameusers = nameusers;
        this.emaiusers = emaiusers;
        this.passusers = passusers;
    }

    public String getDateusers() {
        return dateusers;
    }
    public void setDateusers(String dateusers) {
        this.dateusers = dateusers;
    }
    public String getNameusers() {
        return nameusers;
    }
    public void setNameusers(String nameusers) {
        this.nameusers = nameusers;
    }
    public String getEmaiusers() {
        return emaiusers;
    }
    public void setEmaiusers(String emaiusers) {
        this.emaiusers = emaiusers;
    }
    public String getPassusers() {
        return passusers;
    }
    public void setPassusers(String passusers) {
        this.passusers = passusers;
    }
}


