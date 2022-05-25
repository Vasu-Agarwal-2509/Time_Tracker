package com.example.wscubedemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 0;
    ArrayList<Contacts> arrayList = new ArrayList<>();
    FloatingActionButton btnOpenDialog;
    RecyclerContactAdapter adapter;
    int hourStart, minStart, hourEnd, minEnd;
    String start = "", end = "";
    AlarmManager alarmManager;
    Intent intent;
    PendingIntent pi;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerContact);
        btnOpenDialog = findViewById(R.id.btnOpenDialog);

        DatabaseHelper databaseHelper = DatabaseHelper.getDB(this);
        arrayList = (ArrayList<Contacts>) databaseHelper.contactDAO().getAllContact();

        //for alarm manager service
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        intent = new Intent(this, AlarmReciever.class);
        pi = PendingIntent.getBroadcast(this, REQUEST_CODE, intent, 0);

        //After clicking on floating button
        btnOpenDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Dialog will appear which contains 2 textview, 1 edittext and 1 button
                Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.add_update_lay);
//                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
//                    dialog.getWindow().setBackgroundDrawable(R.drawable.ic_baseline_add_24);
//                }
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                TextView txtStartTime = dialog.findViewById(R.id.txtStartTime);
                EditText edtTask = dialog.findViewById(R.id.edtTask);
                TextView txtEndTime = dialog.findViewById(R.id.txtEndTime);
                Button btnAction = dialog.findViewById(R.id.btnAction);

                //After clicking on start time textView
                txtStartTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                calendar = Calendar.getInstance();
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);
                                calendar.set(Calendar.SECOND, 0);
                                //startTime = findViewById(R.id.startTime);
                                //alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
//                                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//                                alarmManager.set(AlarmManager.RTC_WAKEUP, , );
                                hourStart = hourOfDay;
                                minStart = minute;
                                start = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                                Log.d("tester", "setText1");
                                txtStartTime.setText(start);
                            }
                        };
                        TimePickerDialog timePickerDialog = new TimePickerDialog(dialog.getContext(), onTimeSetListener, hourStart, minStart, true);
                        timePickerDialog.show();
                    }
                });

                //After clicking on End Time textView
                txtEndTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog.OnTimeSetListener onTimeSetListener1 = new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                hourEnd = hourOfDay;
                                minEnd = minute;
                                end = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                                txtEndTime.setText(end);
                            }
                        };
                        TimePickerDialog timePickerDialog1 = new TimePickerDialog(dialog.getContext(), onTimeSetListener1, hourEnd, minEnd, true);
                        timePickerDialog1.show();
                    }
                });

                //After clicking on Add button
                btnAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String task = "";
                        if (!edtTask.getText().toString().equals("")) {
                            task = edtTask.getText().toString();
                        } else {
                            Toast.makeText(MainActivity.this, "Please Enter Task", Toast.LENGTH_SHORT).show();
                        }

                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
                        //if(start != "" && task != "" && end != ""){
                        databaseHelper.contactDAO().addContact(new Contacts(start, task, end));
                        Log.d("tester", "Completed1");
                        arrayList.clear();
                        arrayList.addAll(databaseHelper.contactDAO().getAllContact());
                        adapter.notifyItemInserted(arrayList.size() - 1);

                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Log.d("tester", "Completed2");
        adapter = new RecyclerContactAdapter(this, arrayList);
        recyclerView.setAdapter(adapter);


    }

//    public void makeAlarm(Calendar calendar){
//        //AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        //Intent intent = new Intent(this, AlarmReciever.class);
//        //PendingIntent pi = PendingIntent.getBroadcast(this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
////        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
////        startTime.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                if(startTime.isChecked()){
////                    alarmManager.cancel(pi);
////                }
////            }
//        //});
//
//    }

}