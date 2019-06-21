package com.example.rish.drspectrabt.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.rish.drspectrabt.Interfaces.ItemClickListener;
import com.example.rish.drspectrabt.R;

public class ModeAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {
    public Button modeNameTV;
    public ItemClickListener itemClickListener;


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public ModeAdapter(View itemView) {
        super(itemView);
        modeNameTV =  itemView.findViewById(R.id.modeNameTV);

        itemView.setOnClickListener(this);

    }

    public void onClick(View v) {
        this.itemClickListener.Onclick(v, getAdapterPosition(), false);
    }
}


