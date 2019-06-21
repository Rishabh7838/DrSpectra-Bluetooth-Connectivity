package com.example.rish.drspectrabt;

import android.app.DatePickerDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class PatientProfileActivity extends AppCompatActivity {

    private FloatingActionButton editFAB,saveFAB;
    private TextInputEditText nameET,emailET,numberET,ageET,deviceidET;
    private TextView nameTV,emailTV,numberTV,ageTV,deviceidTV,genderTV;
    private RadioGroup genderRG;
    private CardView editCV,saveCV;
    private HashMap<String,Object>  detailMap = new HashMap<>();
    private Button tinnStartDate,treatStartDate;
    DatePickerDialog  tinnTimeDialog,treatTimeDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);

        editFAB = findViewById(R.id.editFAB);
        saveFAB = findViewById(R.id.submitFAB);
        nameET  = findViewById(R.id.patientName);
        emailET = findViewById(R.id.patientEmail);
        numberET = findViewById(R.id.patientNumber);
        ageET = findViewById(R.id.patientAge);
        deviceidET = findViewById(R.id.patientID);
        nameTV = findViewById(R.id.nameTV);
        emailTV = findViewById(R.id.email_tv);
        numberTV = findViewById(R.id.numberTV);
        ageTV = findViewById(R.id.age_tv);
        deviceidTV = findViewById(R.id.device_id_tv);
        genderTV = findViewById(R.id.gender_tv);
        genderRG = findViewById(R.id.genderRG);
        editCV = findViewById(R.id.editCV);
        saveCV = findViewById(R.id.viewCV);
        tinnStartDate = findViewById(R.id.tinn_prob_date);
        treatStartDate = findViewById(R.id.treat_start_date);


        editFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editCV.setVisibility(View.VISIBLE);
                saveCV.setVisibility(View.GONE);
            }
        });

        saveFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name,email,age,device,number;
                        if(nameET.getText().toString().isEmpty())
                            nameET.setError("Name can not be empty");
                        else if(emailET.getText().toString().isEmpty())
                            emailET.setError("Email can not be empty");
                        else if(ageET.getText().toString().isEmpty())
                            ageET.setError("Age can not be empty");
                        else if(deviceidET.getText().toString().isEmpty())
                            deviceidET.setError("Device can not be empty");
                        else{
                            name = nameET.getText().toString();
                            email = emailET.getText().toString();
                            age = ageET.getText().toString();
                            device=deviceidET.getText().toString();
                            number = numberET.getText().toString();

                            detailMap.put("Name",name);
                            detailMap.put("Age",age);
                            detailMap.put("Number",number);
                            detailMap.put("DeviceID",device);
                            detailMap.put("Email",email);

                            FirebaseDatabase.getInstance().getReference().child("Patient_Details").child(device).updateChildren(detailMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    editCV.setVisibility(View.GONE);
                                    saveCV.setVisibility(View.VISIBLE);
                                    Toast.makeText(PatientProfileActivity.this, "Data Saved", Toast.LENGTH_SHORT).show();
                                }
                                
                            });
                        }


                
            }
        });

        final DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
        tinnStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tinnTimeDialog.show();
            }
        });

        treatStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                treatTimeDialog.show();
            }
        });


        Calendar newCalendar = Calendar.getInstance();
          tinnTimeDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tinnStartDate.setText(dateFormat.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        treatTimeDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                treatStartDate.setText(dateFormat.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));



    }


}
