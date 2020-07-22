package com.example.basic;

import android.net.Uri;

import java.net.URI;

public class Information {
    String name,address,problem,phone,report,date;
    String imgUri;

    public Information() {
    }

    public Information(String name, String address, String problem, String phone, String imgUri, String date, String report) {
        this.name = name;
        this.address = address;
        this.problem = problem;
        this.phone = phone;
        this.imgUri = imgUri;
        this.date=date;
        this.report=report;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getProblem() {
        return problem;
    }

    public String getPhone() {
        return phone;
    }

    public String getImgUri() {
        return imgUri;
    }


    public String getReport() { return report; }

    public String getDate() { return date; }
}
