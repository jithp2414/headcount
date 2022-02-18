package com.futureproducts.headcount.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.futureproducts.headcount.BuildConfig;
import com.futureproducts.headcount.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class addactivity extends AppCompatActivity implements
        OnMapReadyCallback, GoogleMap.OnMapClickListener {

    RelativeLayout main, mapholder;
    Button map, submit;
    EditText name, ocapacity, ccapacity, address;
    ImageView logo;
    TextView addlogo, lat, lng;
    private GoogleMap mMap;

    private static final int CAMERA_REQUEST_CODE = 656;
    private static final int VERIFY_PERMISSIONS_REQUEST = 757;
    private static final int GALLERY_REQUEST_CODE = 665;
    private Uri imageUri;
    private String currentPhotoPath;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference imgref;
    private UploadTask img1ut;
    private String imguri = "";
    private String sname, saddress, socapicity, sccapicity, rid, hashgeo;
    double shopLt, shopLn;
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    ProgressDialog progressDialog;
    private byte[] thumb_byte_data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addactivity);

        main = findViewById(R.id.mainlayout);
        map = findViewById(R.id.map);
        mapholder = findViewById(R.id.mapholder);
        name = findViewById(R.id.addrname);
        ocapacity = findViewById(R.id.addoriginal);
        ccapacity = findViewById(R.id.covidcapacity);
        address = findViewById(R.id.address);
        logo = findViewById(R.id.rlogo);
        addlogo = findViewById(R.id.addlogo);
        lat = findViewById(R.id.lat);
        lng = findViewById(R.id.lng);
        progressDialog = new ProgressDialog(this);
        submit = findViewById(R.id.submit);

        rid = UUID.randomUUID().toString();


        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.setVisibility(View.INVISIBLE);
                mapholder.setVisibility(View.VISIBLE);
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.mainmap);
                mapFragment.getMapAsync(addactivity.this);
            }
        });

        addlogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(addactivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(addactivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(addactivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    showchoice();
//                    changeprofilepic();
                } else {
                    ActivityCompat.requestPermissions(addactivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, VERIFY_PERMISSIONS_REQUEST);
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sname = name.getText().toString();
                socapicity = ocapacity.getText().toString();
                sccapicity = ccapacity.getText().toString();
                saddress = address.getText().toString();
                String slat = String.valueOf(shopLt);
                String slng = String.valueOf(shopLn);


                if(sname.matches("")){
                    Toast.makeText(addactivity.this, "please enter restuarent name", Toast.LENGTH_SHORT).show();
                }else if(socapicity.matches("")){
                    Toast.makeText(addactivity.this, "Please enter original capicaty", Toast.LENGTH_SHORT).show();
                }else if(sccapicity.matches("")){
                    Toast.makeText(addactivity.this, "Please enter covid capicity", Toast.LENGTH_SHORT).show();
                }else if(saddress.matches("")){
                    Toast.makeText(addactivity.this, "Please enter address", Toast.LENGTH_SHORT).show();
                }else if(imguri == null){
                    Toast.makeText(addactivity.this, "Please upload logo", Toast.LENGTH_SHORT).show();
                }else if(slat == null){
                    Toast.makeText(addactivity.this, "Please choose location on maps", Toast.LENGTH_SHORT).show();
                }else if(slng == null){
                    Toast.makeText(addactivity.this, "Please choose location on maps", Toast.LENGTH_SHORT).show();
                }else{

                    HashMap<String, Object> data = new HashMap<>();
                    data.put("resturantname", sname);
                    data.put("original", socapicity);
                    data.put("covid", sccapicity);
                    data.put("address", saddress);
                    data.put("ID", rid);
                    data.put("logo", imguri);
                    data.put("lat", shopLt);
                    data.put("lng", shopLn);
                    data.put("geohash", hashgeo);
                    data.put("headcount", "0");

                    Log.d("TAG", "checkgeo"+hashgeo);

                    FirebaseFirestore.getInstance().collection("Users")
                    .document(rid)
                    .set(data)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        DatabaseReference rref = db.child("resturants").child(rid);
                                        rref.setValue(data);

                                        DatabaseReference pref = db.child("partners").child(FirebaseAuth.getInstance().getUid()).child(rid);
                                        pref.setValue(data);

                                        Intent go = new Intent(addactivity.this, adminpage.class);
                                        startActivity(go);
                                        finish();

                                    }
                                }
                            });

                }

            }
        });


    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mMap.setOnMapClickListener(this);
        mMap.setMyLocationEnabled(true);
        Log.d("Tag", "checkloc"+ "shopLn"+" "+"shopLt");
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
         shopLt = latLng.latitude;
         shopLn = latLng.longitude;

        mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));

        main.setVisibility(View.VISIBLE);
        mapholder.setVisibility(View.GONE);

        lat.setText(String.valueOf(shopLt));
        lng.setText(String.valueOf(shopLn));

        Geocoder geocoder = new Geocoder(addactivity.this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(shopLt, shopLn, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }


        HashMap<String,Object> locationUpdateMap = new HashMap<>();
        hashgeo = GeoFireUtils.getGeoHashForLocation(
                new GeoLocation(latLng.latitude, latLng.longitude));


        Log.d("Tag", "checkloc"+ shopLn+" "+shopLt);
    }



    private void showchoice() {
        final AlertDialog alertDialog = new AlertDialog.Builder(addactivity.this, R.style.DialogTheme).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View viewOptions = inflater.inflate(R.layout.image_source_choice,null);

        final LinearLayout camera = viewOptions.findViewById(R.id.camera);
        final LinearLayout gallery = viewOptions.findViewById(R.id.gallery);

        alertDialog.setView(viewOptions);
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.SlideDialogAnimation;
        alertDialog.getWindow().setGravity(Gravity.BOTTOM);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = alertDialog.getWindow().getAttributes();
        alertDialog.getWindow().setAttributes(layoutParams);


        //open camera
        camera.setOnClickListener(v -> {
            dispatchTakePictureIntent();
            alertDialog.dismiss();
        });

        //open gallery
        gallery.setOnClickListener(v -> {
            openGallery();
            alertDialog.dismiss();
        });

        //show dialog
        alertDialog.show();
    }

    private void openGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , GALLERY_REQUEST_CODE);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            photoFile = createImageFile();
            // Continue only if the File was successfully created
            if (photoFile != null) {
                imageUri = FileProvider.getUriForFile(this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK){

            uploadfire(imageUri);

        }
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {

            if (data.getData() != null) {
                imageUri = data.getData();
                uploadfire(imageUri);

            }
        }
    }
    private void uploadfire(Uri imageUri) {
//        main.setVisibility(View.INVISIBLE);
        progressDialog.setTitle("Please Wait Profile pic uploading");
        progressDialog.show();

        try {
            Bitmap o = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            o.compress(Bitmap.CompressFormat.JPEG, 70, stream);

            thumb_byte_data = stream.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
        }



        String currentUid = FirebaseAuth.getInstance().getUid();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imgref = storage.getReference().child("partnerprofilepic").child(FirebaseAuth.getInstance().getUid()).child(timeStamp);
        final StorageReference imageThumbRef1 = imgref.child("img.jpg");
        Log.d("TAG", "check user" + currentUid + "  " + imageThumbRef1);
//        img1ut = imageThumbRef1.putFile(imageUri);
        img1ut = imageThumbRef1.putBytes(thumb_byte_data);
        Task<Uri> urlTask = img1ut.continueWithTask(task -> {

            if (!task.isSuccessful()) {
                Toast.makeText(addactivity.this, "error" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                throw task.getException();
            }

            // Continue with the task to get the download URL
            return imageThumbRef1.getDownloadUrl();

        }).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {

                progressDialog.dismiss();


                Uri downloadUri = task.getResult();

                //link
                imguri = downloadUri.toString();


                Picasso.get()
                        .load(imguri)
                        .fit()
                        .centerCrop()
                        .into(logo);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        progressDialog.dismiss();
                    }
                }, 2000);
//                    bar.setVisibility(View.GONE);

            } else {

                Toast.makeText(addactivity.this, "FAILED", Toast.LENGTH_SHORT).show();

            }
        });

    }
}