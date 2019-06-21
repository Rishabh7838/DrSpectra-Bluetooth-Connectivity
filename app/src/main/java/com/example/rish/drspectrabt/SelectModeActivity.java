package com.example.rish.drspectrabt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.rish.drspectrabt.Interfaces.ItemClickListener;
import com.example.rish.drspectrabt.Model.Model_Detail;
import com.example.rish.drspectrabt.ViewHolders.ModeAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.example.rish.drspectrabt.DeviceListActivity.EXTRA_ADDRESS;

public class SelectModeActivity extends AppCompatActivity {
    private FirebaseRecyclerAdapter<Model_Detail, ModeAdapter> adapter;
    private RecyclerView  modeDetailRV;
    private DatabaseReference deviceList;
    private ProgressDialog loadDetailPB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_mode);
          modeDetailRV = findViewById(R.id. modeDetailRV);
          modeDetailRV.setLayoutManager(new GridLayoutManager(SelectModeActivity.this, 2));
          modeDetailRV.setHasFixedSize(true);
         loadDetailPB = new ProgressDialog(this);
         loadDetailPB.setMessage("Loading modes...");
         loadDetailPB.show();
         deviceList = FirebaseDatabase.getInstance().getReference().child("Tinnitus_Mode");
        loadModeDetails();
    }

    private void loadModeDetails() {
        this.adapter = new FirebaseRecyclerAdapter<Model_Detail, ModeAdapter>( Model_Detail.class, R.layout.mode_layout,  ModeAdapter.class, this.deviceList) {

            /* renamed from: com.hello.one.dr_admin.ConnectActivity$1$2 */

            protected void populateViewHolder( ModeAdapter viewHolder, final  Model_Detail model, int position) {
                SelectModeActivity.this.loadDetailPB.dismiss();
                viewHolder.modeNameTV.setText(model.getMode_name());
                viewHolder.modeNameTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new SweetAlertDialog(SelectModeActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Select this mode?")
                                .setContentText("You are selecting "+model.getMode_name()+" mode")
                                .setCancelText("No")
                                .setConfirmText("Yes")
                                .showCancelButton(true)
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog
                                                .setTitleText(model.getMode_name()+" Mode Set")
                                                .setContentText("Start this mode")
                                                .showCancelButton(false)
                                                .setConfirmText("OK")
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                        FirebaseDatabase.getInstance().getReference().child("Device_Details").child("19863").child("Query")
                                                                .setValue(model.getMode_query()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                msg("Settings updated");

                                                                FirebaseDatabase.getInstance().getReference().child("Device_Details").child("19863")
                                                                        .child("updation_time")
                                                                        .setValue(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));

                                                                onBackPressed();
                                                            }
                                                        })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        msg("Update failed. Check Network connection");
                                                                    }
                                                                });
                                                        //this will be received at ledControl (class) Activity

                                                    }
                                                })
                                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);



                                    }
                                })
                                .show();

                    }
                });
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void Onclick(View view, int position, boolean z) {
                        Log.e("Click","Item: "+position);
                    }
                });
            }
        };
        this.modeDetailRV.setAdapter(this.adapter);
    }

    private void msg(String settings_updated) {
        Toast.makeText(this, settings_updated, Toast.LENGTH_SHORT).show();
    }
}
