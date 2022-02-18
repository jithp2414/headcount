package com.futureproducts.headcount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class loginscreen extends AppCompatActivity {
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mauth = FirebaseAuth.getInstance();
    public static final int RC_SIGN_IN = 20;
    private GoogleSignInClient mGoogleSignInClient;
    String mailcheck;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginscreen);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id1))
                .requestEmail()
                .build();

        //configure sign in client
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    public void login(View view) {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {

                // Google Sign In was successful
                GoogleSignInAccount account = task.getResult(ApiException.class);
                mailcheck = account.getEmail();
                Log.d("TAG", "mailcheck"+mailcheck);

                firebaseAuthWithGoogle(account);

            } catch (ApiException e) {


                // Google Sign In failed
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mauth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {

                    if (task.isSuccessful()) {

                        // Sign in success
                        FirebaseUser user = mauth.getCurrentUser();
//                        Intent gohome = new Intent(MainActivity.this, saveuserdata.class);
//                        startActivity(gohome);
                        checkUserData(user);

                    } else {

                        //stop loading

                        // Google Sign In failed
                        Toast.makeText(loginscreen.this, "Authentication failed", Toast.LENGTH_SHORT).show();

                    }

                });
    }

    private void checkUserData(FirebaseUser user) {


        user = FirebaseAuth.getInstance().getCurrentUser();


        DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        DatabaseReference cref = db.child("users").child(FirebaseAuth.getInstance().getUid());
        cref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Intent gohome = new Intent(loginscreen.this, MainActivity.class);
                    gohome.putExtra("email", mailcheck);
//                    gohome.putExtra("phonenumber", "no");
                    startActivity(gohome);
                    finish();
                }else{
                    DatabaseReference uref = db.child("users").child(FirebaseAuth.getInstance().getUid());
                    uref.child("name").setValue(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                    uref.child("userid").setValue(FirebaseAuth.getInstance().getUid());
                    uref.child("usertype").setValue("user");
                    uref.child("admintype").setValue("partner");


                    Intent gohome = new Intent(loginscreen.this, MainActivity.class);
                    gohome.putExtra("email", mailcheck);
//                    gohome.putExtra("phonenumber", "no");
                    startActivity(gohome);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });








//        FirebaseDatabase.getInstance().getReference().child("users")
//                .orderByChild("email").equalTo(mailcheck)
//                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//
//                DataSnapshot userSnap;
//                if (task.isSuccessful() && (userSnap = task.getResult()) != null && userSnap.exists()) {
//                    Snackbar.make(googleBtn, "You account with this email already exists!" +
//                            "Please login with it", Snackbar.LENGTH_INDEFINITE).show();
//                } else {
//
//                    Intent gohome = new Intent(MainActivity.this, saveuserdata.class);
//                    gohome.putExtra("email", mailcheck);
////                    gohome.putExtra("phonenumber", "no");
//                    startActivity(gohome);
//
//                }
//
//            }
//        });
    }



}