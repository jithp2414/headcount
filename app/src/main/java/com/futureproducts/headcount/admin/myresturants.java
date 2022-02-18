package com.futureproducts.headcount.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.futureproducts.headcount.R;
import com.futureproducts.headcount.model.resturantsmodel;
import com.futureproducts.headcount.viewmodels.upholder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class myresturants extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseRecyclerOptions<resturantsmodel> option;
    FirebaseRecyclerAdapter<resturantsmodel, upholder> adapter;
    DatabaseReference uoref;
    DatabaseReference dref;
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    String key = "";
    String admintype ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myresturants);

        recyclerView = findViewById(R.id.allrecycler);
        recyclerView.setLayoutManager( new LinearLayoutManager(this));

        dref = db.child("users").child(FirebaseAuth.getInstance().getUid());
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                   admintype  = snapshot.child("admintype").getValue(String.class);
                    if(admintype.matches("admin")){
                        uoref = FirebaseDatabase.getInstance().getReference().child("resturants");

                    }else{
                        uoref = FirebaseDatabase.getInstance().getReference().child("partners").child(FirebaseAuth.getInstance().getUid());
                    }
                    Log.d("TAG", "checkpath"+uoref);
                    option = new FirebaseRecyclerOptions.Builder<resturantsmodel>()
                            .setQuery(uoref, resturantsmodel.class).build();
                    adapter = new FirebaseRecyclerAdapter<resturantsmodel, upholder>(option) {
                        @Override
                        protected void onBindViewHolder(@NonNull upholder holder, int position, @NonNull resturantsmodel model) {
                            holder.rname.setText(model.getResturantname());
                            holder.rname.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent go = new Intent(myresturants.this, viewresturantsactivity.class);
                                    go.putExtra("rid", getRef(holder.getAbsoluteAdapterPosition()).getKey());
                                    startActivity(go);
                                    finish();
                                }
                            });
                        }

                        @NonNull
                        @Override
                        public upholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.allresturants,parent, false);
                            return new upholder(v);
                        }
                    };
                    adapter.startListening();
                    recyclerView.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






    }
}