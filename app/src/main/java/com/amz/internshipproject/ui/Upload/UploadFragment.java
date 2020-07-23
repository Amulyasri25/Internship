package com.amz.internshipproject.ui.Upload;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.amz.internshipproject.Post;
import com.amz.internshipproject.R;
import com.amz.internshipproject.UserUploads;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;

public class UploadFragment extends Fragment  {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String TAG = "UploadFragment";
    private UploadViewModel uploadViewModel;
    private ImageView mPostImage, location;
    private Button post;
    private Uri imageUri;
    EditText Description;
    private ArrayList al;
    private ArrayAdapter ad;
    double latitude,longitude;
    private DatabaseReference refer;
    private StorageReference storageReference;
    private String lat, longitude1, description, selectedType;
    private String[] type = {"--Select--", "Orphan", "Old Age people", "Blood Required","Blood Available", "Food Required", "Food Available","NGO"};
    private Spinner typeSpinner;
    private ArrayAdapter<String> typeAdapter;
    private FirebaseAuth fAth;
    String currentPicPath,name;
    private LocationManager mlocationManager;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        uploadViewModel =
                ViewModelProviders.of(this).get(UploadViewModel.class);

        View root = inflater.inflate(R.layout.fragment_upload, container, false);
        //  final TextView textView = root.findViewById(R.id.text_notifications);
        mPostImage = root.findViewById(R.id.imageView);
        location = root.findViewById(R.id.select_loc);
        // sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mlocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        typeSpinner = root.findViewById(R.id.sp);
        Description = root.findViewById(R.id.description);
        post = root.findViewById(R.id.uploadPost);
        typeAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, type);
        typeSpinner.setAdapter(typeAdapter);
        fAth = FirebaseAuth.getInstance();
        refer = FirebaseDatabase.getInstance().getReference("Posts");
        storageReference = FirebaseStorage.getInstance().getReference("Posts");
        uploadViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //        textView.setText(s);

            }
        });


        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedType = typeSpinner.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                getLocation();
            }
        });


        mPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Select From");
                String[] options = {"Pick from Gallery", "from camera", "Cancel"};
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int option) {
                        switch (option) {
                            case 0:
                                pickImage();
                                Toast.makeText(getContext(), "Pick from Gallery", Toast.LENGTH_LONG).show();
                                break;
                            case 1:
                                dispatchTakePictureIntent();
                                Toast.makeText(getContext(), "from camera", Toast.LENGTH_LONG).show();
                                break;
                            case 2:
                                dialog.cancel();
                                break;
                            default:
                                break;
                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();


            }
        });
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                description = Description.getText().toString();
                Toast.makeText(getContext(), "Posted success", Toast.LENGTH_SHORT).show();
                createPost();

            }
        });

///
        return root;
    }

     private void dispatchTakePictureIntent() {
       Intent pickIntent = new Intent();
     pickIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
     if (pickIntent.resolveActivity(getActivity().getPackageManager()) != null) {
      File photoFile = null;
     try {
       photoFile = createImageFile();
    }
      catch (IOException ex) {
      Toast.makeText(getContext(), " creating file error", Toast.LENGTH_SHORT).show();
    }
    if (photoFile != null){
      Uri photoUri = FileProvider.getUriForFile(getContext(),"com.amz.android.fileprovider",photoFile);
    pickIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
    }
    startActivityForResult(pickIntent, REQUEST_IMAGE_CAPTURE);
    }
    }
      private File createImageFile() throws IOException {
      File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
      File image = File.createTempFile(""+System.currentTimeMillis(),".jpg",storageDir);
      currentPicPath = image.getAbsolutePath();
        return  image;
    }


    private void createPost() {
        Toast.makeText(getContext(), "create", Toast.LENGTH_SHORT).show();
        if (selectedType.equals(type[0])) {
            Toast.makeText(getContext(), "please select the category of post ...", Toast.LENGTH_SHORT).show();
            return;
        }
        storePost();
    }

    private void storePost() {
        // if(imageUri!=null) {
        Toast.makeText(getContext(), "store", Toast.LENGTH_SHORT).show();
        final StorageReference FilRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
        FilRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                FilRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String uid = fAth.getCurrentUser().getUid();
                     FirebaseDatabase.getInstance().getReference("users").child(uid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                name=snapshot.child("name").getValue().toString();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        String key = refer.push().getKey();
                        Post post = new Post(uri.toString(), description, uid,selectedType);
                        refer.child(key).setValue(post);
                        refer.child(key).child("latitude").setValue(latitude);
                        refer.child(key).child("longitude").setValue(longitude);
                        //    Toast.makeText(getContext(), "Posted", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Toast.makeText(getContext(), " "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    //startActivity(new Intent(getContext(), UserUploads.class));


    private String getFileExtension(Uri imgUri) {
        ContentResolver contentResolver=getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
         return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imgUri));
    }




    private void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            mPostImage.setImageURI(imageUri);
            System.out.println("$$$$$$$$$$ "+imageUri);
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            File file = new File(currentPicPath);
            imageUri = Uri.fromFile(file);
            mPostImage.setImageURI(imageUri);
        }


        }




       public void getLocation() {
        Location location = getLastBestLocation();
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
           // location_tv.setText("" + latitude + " , " + longitude);

        }
        else{
             latitude=0;
             longitude=0;
        }
    }
        private Location getLastBestLocation() {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "allow permisions....", Toast.LENGTH_SHORT).show();
                return null;
            }
            Location locationGPS = mlocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location locationNET = mlocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Toast.makeText(getContext(), "gxcvxcvv", Toast.LENGTH_SHORT).show();

            long gpsLocationTime = 0;
            long netLocationTime = 0;

            if(locationGPS!=null){
                gpsLocationTime = locationGPS.getTime();
                //Toast.makeText(get, "gps"+gpsLocationTime, Toast.LENGTH_SHORT).show();
            }
            if(locationNET != null){
                netLocationTime = locationNET.getTime();
               // Toast.makeText(this, "net"+netLocationTime, Toast.LENGTH_SHORT).show();
            }

            if(gpsLocationTime-netLocationTime>0){
                return locationGPS;
            }else{
                return locationNET;
            }
        }
}
