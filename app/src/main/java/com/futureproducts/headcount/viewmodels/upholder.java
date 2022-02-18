package com.futureproducts.headcount.viewmodels;

import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.futureproducts.headcount.R;

public class upholder extends RecyclerView.ViewHolder{
    public Button rname;

    public upholder(@NonNull View itemView) {
        super(itemView);

        rname = itemView.findViewById(R.id.recyclebtn);

    }
}
