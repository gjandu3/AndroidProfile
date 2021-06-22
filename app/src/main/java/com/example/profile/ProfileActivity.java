package com.example.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        byte[] byteArray = extras.getByteArray("photo");
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        ImageView profilePicture = findViewById(R.id.ProfilePic);
        profilePicture.setImageBitmap(bmp);
        TextView nameView = findViewById(R.id.ProfileName);
        nameView.setText(name);
        TextView emailView = findViewById(R.id.ProfileEmail);
        emailView.setText(email);
    }
}
