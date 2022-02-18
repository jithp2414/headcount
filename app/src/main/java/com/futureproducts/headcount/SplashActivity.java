package com.futureproducts.headcount;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.futureproducts.headcount.util.LocationRequester;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SplashActivity extends AppCompatActivity implements LocationRequester.LocationRequestAction{


    private LocationRequester locationRequester;
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    LocationRequester locationRequester = new LocationRequester(this, this);
                    locationRequester.getCurrentLocation();

                } else {
                    Toast.makeText(this,
                            "You need to grant location access permission in order user the application"
                            , Toast.LENGTH_SHORT).show();
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);


        requestLocationPermission();

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent go = new Intent(SplashActivity.this, loginscreen.class);
//                startActivity(go);
//                finish();
//            }
//        }, 2000);
//

    }


    public void locationFetched(double lat, double lng) {

        Log.d("ttt", "lat: " + lat + " - lng: " + lng);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {


                                    startActivity(new Intent(SplashActivity.this, loginscreen.class)
                                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                            .putExtra("lat", lat)
                                            .putExtra("lng", lng));
                                    finish();

        }else{
//            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//            HashMap<String, Object> userMap = new HashMap<>();
//            userMap.put("ID", user.getUid());
//            userMap.put("username", "test user "+user.getUid().substring(0,3));
//            Log.d("ttt", "splash  - lat: " + lat + " - lng: " + lng+ "  "+user.getUid().substring(0,3)+"  "+ user.getUid() );
//
//
//            FirebaseFirestore.getInstance().collection("Users")
//                    .document(user.getUid())
//                    .set(userMap)
//                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(@NonNull Void unused) {

                            startActivity(new Intent(SplashActivity.this, MainActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    .putExtra("lat", lat)
                                    .putExtra("lng", lng));
                            finish();
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    finish();
//                }
//            });


        }


    }

    @Override
    public void locationFetchFailed() {

    }

    @Override
    protected void onResume() {
        if (locationRequester != null) {
            locationRequester.resumeLocationUpdates();
        }
        super.onResume();
    }


    @Override
    protected void onPause() {
        if (locationRequester != null) {
            locationRequester.stopLocationUpdates();
        }
        super.onPause();
    }


    private void requestLocationPermission() {

        final String locationPermission = Manifest.permission.ACCESS_FINE_LOCATION;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(this, locationPermission)
                    == PackageManager.PERMISSION_GRANTED) {

                if (locationRequester == null) {
                    locationRequester = new LocationRequester(SplashActivity.this,
                            SplashActivity.this);
                }
                locationRequester.getCurrentLocation();
            } else {

                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            }
        } else {

            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);

        }

    }
}