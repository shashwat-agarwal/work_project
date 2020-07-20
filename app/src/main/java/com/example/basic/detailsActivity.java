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

import java.util.Date;

public class detailsActivity extends AppCompatActivity {

    EditText nameE, addressE, phoneE, problemE;
    String name, address, phone, problem;
    TextView dateAndTime;
    ImageView imageView;
    Button addImg;
    Uri selectedImg;

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
        problemE = findViewById(R.id.problem);

        name = nameE.getText().toString().trim();
        address = addressE.getText().toString().trim();
        problem = problemE.getText().toString().trim();
        phone = phoneE.getText().toString().trim();
        Toast.makeText(this, "" + selectedImg, Toast.LENGTH_SHORT).show();
        try {
            Information information = new Information(name, address, problem, phone, selectedImg.toString());

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
        } catch (Exception e) {
            Log.i("ERROR", e.toString());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void addImage(View view) {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
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
        selectedImg = data.getData();

        if (requestCode == 1 && data != null && resultCode == RESULT_OK) {

            try {
                Glide.with(getApplicationContext()).asBitmap().load(selectedImg).into(imageView);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
