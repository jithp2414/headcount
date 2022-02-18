package com.futureproducts.headcount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.futureproducts.headcount.model.resturantsmodel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class viewresturantdetailsactivity extends AppCompatActivity {

    String id;
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    ImageView logo;
    TextView name, address, headcount, original, covid, current, seekbarvalue;
    SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewresturantdetailsactivity);

        id = getIntent().getStringExtra("id");
        logo = findViewById(R.id.rlogo);
        name = findViewById(R.id.rname);
        address = findViewById(R.id.raddress);
        headcount = findViewById(R.id.headcount);
        original = findViewById(R.id.vieworiginal);
        covid = findViewById(R.id.covidtotal);
        current = findViewById(R.id.viewcurrent);
        seekBar = findViewById(R.id.seekbar);
        seekbarvalue = findViewById(R.id.seekbarvalue);


        seekBar.setEnabled(false);

        DatabaseReference nref = db.child("resturants").child(id);
        nref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    resturantsmodel data = snapshot.getValue(resturantsmodel.class);
                    Picasso.get().load(data.getLogo()).into(logo);
                    name.setText(data.getResturantname());
                    address.setText(data.getAddress());
                    headcount.setText(data.getHeadcount());
                    original.setText(data.getOriginal());
                    covid.setText(data.getCovid());
                    int a = Integer.parseInt(data.getCovid());
                    int b = Integer.parseInt(data.getHeadcount());
                    int c = b*100;
                    int d = c/a;
                    current.setText(String.valueOf(d)+"%");
                    seekbarvalue.setText(d+"%/100%");
                    if(d<40){
                        seekBar.setProgress(d);
                        seekBar.setProgressDrawable(getDrawable(R.color.bar1on));
                    }else if(d>40 && d<80) {
                        seekBar.setProgress(d);
                        seekBar.setProgressDrawable(getDrawable(R.color.bar4on));
                    }else if(d>80){
                        seekBar.setProgress(d);
                        seekBar.setProgressDrawable(getDrawable(R.color.bar5on));
                    }




                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}