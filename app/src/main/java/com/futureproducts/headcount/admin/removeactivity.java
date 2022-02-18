package com.futureproducts.headcount.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.futureproducts.headcount.R;
import com.futureproducts.headcount.model.resturantsmodel;
import com.futureproducts.headcount.viewmodels.upholder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class removeactivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseRecyclerOptions<resturantsmodel> option;
    FirebaseRecyclerAdapter<resturantsmodel, upholder> adapter;
    DatabaseReference uoref;
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_removeactivity);


        recyclerView = findViewById(R.id.allrecycler);
        recyclerView.setLayoutManager( new LinearLayoutManager(this));

        uoref = FirebaseDatabase.getInstance().getReference().child("resturants");
        option = new FirebaseRecyclerOptions.Builder<resturantsmodel>()
                .setQuery(uoref, resturantsmodel.class).build();
        adapter = new FirebaseRecyclerAdapter<resturantsmodel, upholder>(option) {
            @Override
            protected void onBindViewHolder(@NonNull upholder holder, int position, @NonNull resturantsmodel model) {
                holder.rname.setText(model.getResturantname());
                holder.rname.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        String key = getRef(holder.getAbsoluteAdapterPosition()).getKey();
                        FirebaseFirestore.getInstance().collection("Users")
                                .document(key)
                                .delete();


                        DatabaseReference dref = db.child("resturants").child(key);
                        dref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(removeactivity.this, "resturant deleted successfully", Toast.LENGTH_SHORT).show();
                                Intent go = new Intent(removeactivity.this, adminpage.class);
                                startActivity(go);
                                finish();
                            }
                        });
                        return true;
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