package com.example.wscubedemo;

import static android.content.Context.ALARM_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

import static com.example.wscubedemo.MainActivity.REQUEST_CODE;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

public class RecyclerContactAdapter extends RecyclerView.Adapter<RecyclerContactAdapter.ViewHolder>{
    Context context;
    ArrayList<Contacts> arrayList;
    DatabaseHelper databaseHelper;
    int hourStart, minStart, hourEnd, minEnd;
    String start = "", end = "";
    //PendingIntent pendingIntent;

    public RecyclerContactAdapter(Context context, ArrayList<Contacts> arrayList){
        this.context = context;
        Log.d("tester", "RecyclerAdapterConstructor");
        this.arrayList = arrayList;
        //this.pendingIntent = pendingIntent;

        //notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.time_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Contacts contacts = arrayList.get(position);
        databaseHelper = DatabaseHelper.getDB(context);
        holder.start.setText(contacts.getStart());
        holder.task.setText(contacts.getTask());
        holder.end.setText(contacts.getEnd());
//        holder.start.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(holder.start.isChecked()){
//                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
//                    Intent intent = new Intent(context, AlarmReciever.class);
//                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intent, 0);
//                    Log.d("tester", "cancel alarm");
//                    alarmManager.cancel(pendingIntent);
//                }
//            }
//
//        });

        //after clicking on each row
        holder.llrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("tester", "onRowClick");

                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.add_update_lay);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                TextView txtStartTime = dialog.findViewById(R.id.txtStartTime);
                EditText edtTask = dialog.findViewById(R.id.edtTask);
                TextView txtEndTime = dialog.findViewById(R.id.txtEndTime);
                Button btnAction = dialog.findViewById(R.id.btnAction);
                btnAction.setText("Update");
                txtStartTime.setText(arrayList.get(position).start);
                edtTask.setText(arrayList.get(position).task);
                txtEndTime.setText(arrayList.get(position).end);

                //AlarmReciever reciever = new AlarmReciever(true);
//                AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
//                Intent intent = new Intent(context, AlarmReciever.class);
//                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intent, 0);
//                Log.d("tester", "cancel alarm");
//                alarmManager.cancel(pendingIntent);

                //After clicking on startTime textview
                txtStartTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
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

                //after clicking on endTime textview
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

                //after clicking on button
                btnAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String task = "";
//                        String start = "", task = "", end = "";
//                        if(!edtStartTime.getText().toString().equals("")){
//                            start = edtStartTime.getText().toString();
//                        }
//                        else{
//                            Toast.makeText(context, "Please Enter Start Name", Toast.LENGTH_SHORT).show();
//                        }

                        if(!edtTask.getText().toString().equals("")){
                            task = edtTask.getText().toString();
                        }
                        else{
                            Toast.makeText(context, "Please Enter Task", Toast.LENGTH_SHORT).show();
                        }

//                        if(!edtEndTime.getText().toString().equals("")){
//                            end = edtEndTime.getText().toString();
//                        }
//                        else{
//                            Toast.makeText(context, "Please Enter End Time", Toast.LENGTH_SHORT).show();
//                        }

                        databaseHelper.contactDAO().updateContact(new Contacts(start, task, end));
                        arrayList.set(position, new Contacts(start, task, end));
                        notifyItemChanged(position);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        //after pressing long on each row
        holder.llrow.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setTitle("Delete Contact")
                        .setMessage("Are you sure you want to delete?")
                        .setIcon(R.drawable.ic_baseline_delete_24)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                databaseHelper.contactDAO().deleteContact(arrayList.get(position));
                                arrayList.remove(position);
                                notifyItemRemoved(position);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                builder.show();
                return true;
            }
        });
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView start, task, end;
        //CheckBox start;
        LinearLayout llrow;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            start = itemView.findViewById(R.id.startTime);
            task = itemView.findViewById(R.id.taskName);
            end = itemView.findViewById(R.id.endTime);
            llrow = itemView.findViewById(R.id.llrow);
        }
    }
}
