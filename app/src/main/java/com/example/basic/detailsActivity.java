package com.example.basic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Date;

public class detailsActivity extends AppCompatActivity {

    EditText nameE,addressE,phoneE;
    String name,address,phone;
    TextView dateAndTime;
    ImageView imageView;
    Button addImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        dateAndTime = (TextView) findViewById(R.id.date);
        imageView = (ImageView) findViewById(R.id.imageview);
        nameE=(EditText)findViewById(R.id.name);
        addressE=findViewById(R.id.address);
        phoneE=findViewById(R.id.phone);
        addImg=(Button)findViewById(R.id.addImg);


        String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());

        // textView is the TextView view that should display it
        dateAndTime.setText(currentDateTimeString);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void addImage(View view)
    {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        else {
            getPhoto();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getPhoto();
            }
        }
    }


    public void getPhoto() {


        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        // intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri selectedImg = data.getData();

        if (requestCode == 1 && data != null && resultCode == RESULT_OK) {

            try {
                Glide.with(getApplicationContext()).asBitmap().load(selectedImg).into(imageView);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
