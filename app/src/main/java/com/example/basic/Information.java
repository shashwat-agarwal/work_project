package com.example.basic;

import android.net.Uri;

import java.net.URI;

public class Information {
    String name,address,problem,phone;
    String imgUri;

    public Information() {
    }


    public Information(String name, String address, String problem, String phone, String imgUri) {
        this.name = name;
        this.address = address;
        this.problem = problem;
        this.phone = phone;
        this.imgUri = imgUri;
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
}
