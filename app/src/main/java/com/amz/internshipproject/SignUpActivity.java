package com.amz.internshipproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SignUpActivity extends AppCompatActivity {
    private EditText etName,etEmail,etPassword,etMobile,etAddress,etState;
    private String name,mobile,email,password,address,state;
    private Spinner userTypeSpinner;
    private ImageView doc;
    private FirebaseAuth auth;
    private DatabaseReference userRef,userTypeRef;
    private StorageReference mStorageRef;
    private String[] rolesList={"--Select User Type--","General User","NGO","Orphanage","Old age home","BloodBank","Restaurant"};
    private ArrayAdapter<String> roleAdapter;
    private Uri imageUri;
    private String selectedUserType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        etName=findViewById(R.id.name);
        etEmail=findViewById(R.id.mail);
        etMobile=findViewById(R.id.phone);
        etPassword  = findViewById(R.id.password);
        etAddress=findViewById(R.id.address);
        etState=findViewById(R.id.state);
        userTypeSpinner = findViewById(R.id.sp1);
        roleAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,rolesList);
        userTypeSpinner.setAdapter(roleAdapter);
        doc=findViewById(R.id.proof);
        auth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("users");
        userTypeRef = FirebaseDatabase.getInstance().getReference("roles");


        mStorageRef= FirebaseStorage.getInstance().getReference("Documents");
        StorageReference fileRef = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
        fileRef.putFile(Uri.parse(String.valueOf(imageUri)));
        userTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedUserType = userTypeSpinner.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void navigateToSignIn() {
        startActivity(new Intent(SignUpActivity.this,SignIn.class));
        finish();
    }

    public void OnSignInTapped(View view) {
        navigateToSignIn();
    }

    public void OnSignUpTapped(View view) {
        name        = etName.getText().toString().trim();
        email       = etEmail.getText().toString().trim();
        mobile      = etMobile.getText().toString().trim();
        password    = etPassword.getText().toString();
        address=etAddress.getText().toString().trim();
        state=etState.getText().toString().trim();
        createUser();
    }

    private void createUser() {
        if (selectedUserType.equals(rolesList[0])) {
            Toast.makeText(SignUpActivity.this, "please select user type ...", Toast.LENGTH_SHORT).show();
            return;
        }
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                storeUserDetails();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
    public String getFileExtension(Uri uri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(Uri.parse(String.valueOf(uri))));
    }

    public void FolderSave(View view) {
        pickImage();
    }

    private void pickImage() {
        Intent img=new Intent();
        img.setType("image/*");
        img.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(img,100);
    }
    private void storeUserDetails() {
        String uId = auth.getCurrentUser().getUid();
        User user = new User(name,email,mobile,selectedUserType,address,state);
        userRef.child(uId).setValue(user);
        userTypeRef.child(uId).child("userType").setValue(selectedUserType);
        Toast.makeText(this, "Sign Up Success...", Toast.LENGTH_SHORT).show();
        navigateToSignIn();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100 && resultCode==RESULT_OK && data !=null){
            imageUri=data.getData();
            doc.setImageURI(imageUri);
        }
    }
}

