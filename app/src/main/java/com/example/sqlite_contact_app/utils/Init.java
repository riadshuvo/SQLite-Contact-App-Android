package com.example.sqlite_contact_app.utils;

import android.Manifest;

public class Init {

    public Init() {
    }

    public static final int CAMERA_REQUEST_CODE = 5;

    public static final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    public static final String[] PHONE_PERMISSIONS = {Manifest.permission.CALL_PHONE};
}
