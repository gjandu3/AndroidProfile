package com.example.profile;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Checked;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Validator.ValidationListener {
    private Button uploadButton;
    private Button registerButton;
    private Validator validator;
    private ImageView previewImage;

    @NotEmpty
    @Length(min = 3, max = 10)
    private EditText editTextName;

    @NotEmpty
    @Email
    private EditText editTextEmail;

    @NotEmpty
    @Password
    @Length(min = 8, max = 20)
    private EditText editTextPassword;

    @NotEmpty
    @ConfirmPassword
    private EditText editTextConfirmPassword;

    @Checked
    private RadioGroup editGender;

    int SELECT_PICTURE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uploadButton = findViewById(R.id.UploadButton);
        previewImage = findViewById(R.id.ImagePreview);
        registerButton = findViewById(R.id.Register);
        validator = new Validator(this);
        validator.setValidationListener((Validator.ValidationListener) this);
        initView();
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch("image/*");
            }
        });
    }

    private void initView() {
        editTextName = findViewById(R.id.NameText);
        editTextEmail = findViewById(R.id.EmailAddressText);
        editTextPassword = findViewById(R.id.PasswordText);
        editTextConfirmPassword = findViewById(R.id.PasswordConfirmText);
        editGender = findViewById(R.id.Gender);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (previewImage.getDrawable() == null) {
                    Toast.makeText(MainActivity.this, "Please upload a photo", Toast.LENGTH_SHORT).show();
                } else {
                    validator.validate();
                }
            }
        });
    }

   ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
           new ActivityResultCallback<Uri>() {
                @Override
               public void onActivityResult(Uri result) {
                    if (result != null) {
                        previewImage.setImageURI(result);
                    }
                }
           });

    @Override
    public void onValidationSucceeded() {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);

        BitmapDrawable drawable = (BitmapDrawable) previewImage.getDrawable();
        Bitmap bmp = drawable.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte [] byteArray = stream.toByteArray();

        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();
        intent.putExtra("name", name);
        intent.putExtra("photo", byteArray);
        intent.putExtra("email", email);
        startActivity(intent);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error: errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}