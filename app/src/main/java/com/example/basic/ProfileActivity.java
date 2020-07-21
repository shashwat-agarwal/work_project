package com.example.basic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    TextView displayDetails;
    ImageView displayImage;
    String id;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference userRef=db.collection("user");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        displayDetails=findViewById(R.id.showText);
        displayImage=findViewById(R.id.showImg);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        load();
    }

    public void load() {
        userRef.document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Information userInformation = documentSnapshot.toObject(Information.class);
                            String name = userInformation.getName();
                            String address = userInformation.getAddress();
                            String problem = userInformation.getProblem();
                            String number = userInformation.getPhone();
                            String uri=userInformation.getImgUri();
                            displayDetails.setText("Name:  " + name + "\n" + "Problem:  " + problem + "\n" + "Address: " + address + "\n" + "Phone: " + number);
                            Picasso.get().load(uri).placeholder(R.drawable.ic_image).into(displayImage);
                        } else {
                            Toast.makeText(ProfileActivity.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        Log.d("TAG", e.toString());
                    }
                });
    }
}
