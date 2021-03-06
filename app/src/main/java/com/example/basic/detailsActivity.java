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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.santalu.maskara.Mask;
import com.santalu.maskara.widget.MaskEditText;

import java.util.Date;

public class detailsActivity extends AppCompatActivity {
    final public static int PICK_IMAGE_REQUEST = 1;

    EditText nameE, addressE, problemE, reportE;
    MaskEditText phoneE, dateE;
    String name, address, phone, problem, date, report;
    TextView dateAndTime;
    ImageView imageView;
    Button addImg;
    Uri selectedImg=null;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userRef = db.collection("user");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        dateAndTime = findViewById(R.id.date);
        imageView = findViewById(R.id.imageview);
        addImg = findViewById(R.id.addImg);


        String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
        dateAndTime.setText(currentDateTimeString);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.done:
                done();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void done() {
        nameE = findViewById(R.id.name);
        addressE = findViewById(R.id.address);
        phoneE = findViewById(R.id.phone);
        problemE = findViewById(R.id.purpose);
        dateE = findViewById(R.id.dateFill);
        reportE = findViewById(R.id.reportNumber);

        name = nameE.getText().toString().trim();
        address = addressE.getText().toString().trim();
        problem = problemE.getText().toString().trim();
        phone = phoneE.getText().toString().trim();
        date = dateE.getText().toString().trim();
        report = reportE.getText().toString().trim();
        try {
            if (name.isEmpty()) {
                Toast.makeText(this, "Name is compulsory", Toast.LENGTH_SHORT).show();
            } else if (selectedImg==null) {
                Toast.makeText(this, "Add image to proceed !!", Toast.LENGTH_SHORT).show();
            } else {

                Information information = new Information(name, address, problem, phone, selectedImg.toString(), date, report);

                userRef.add(information).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(detailsActivity.this, "Information Saved !!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(detailsActivity.this, "Error occured :-(", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (Exception e) {
            Log.i("ERROR", e.toString());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }


    public void addImage(View view) {
        getPhoto();
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


        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        // intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        selectedImg = data.getData();

        if (requestCode == PICK_IMAGE_REQUEST && data != null && resultCode == RESULT_OK && data.getData() != null) {

            try {
                Glide.with(getApplicationContext()).asBitmap().load(selectedImg).into(imageView);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

   /* private String getFileExtension(Uri uri){

    }*/
}
