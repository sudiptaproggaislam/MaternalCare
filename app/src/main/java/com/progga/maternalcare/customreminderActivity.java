package com.progga.maternalcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class customreminderActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Toolbar mToolbar;
    private ListView customreminderlist;
    private TextView calender;
    private Button upload;
    private EditText remindermsg;

    private DatabaseReference refUp,ref,refUp2;
    private FirebaseAuth dAuth;

    String currentuserid,date1,msg;

    long getdate=0,getMilliSec=24*3600*1000,periodlength=0;
    SimpleDateFormat formater = new SimpleDateFormat("EEE, d MMM yyyy");

    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_groups=new ArrayList<>();

    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customreminder);

        mToolbar=(Toolbar)findViewById(R.id.customreminder_activity_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Custom Reminder");


        initializations();
        createNotificationChannel();
        RetriveAndDisplay();


        calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                msg=remindermsg.getText().toString();
                if(getdate==0 || TextUtils.isEmpty(msg)){
                    Toast.makeText(customreminderActivity.this,"Please select a date and enter message",Toast.LENGTH_LONG).show();
                }else {
                    uploadreminder();
                }
            }
        });
    }



    private void initializations() {
        upload=(Button)findViewById(R.id.btnSendCustomReminder);
        calender=(TextView)findViewById(R.id.tvCalerderCustomReminder);
        customreminderlist=(ListView)findViewById(R.id.lvCustomReminder);
        remindermsg=(EditText)findViewById(R.id.etCustomMsg);

        dAuth=FirebaseAuth.getInstance();
        ref= FirebaseDatabase.getInstance("https://maternal-care-fa0e8-default-rtdb.firebaseio.com/").getReference();

        currentuserid=dAuth.getCurrentUser().getUid();

        arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list_of_groups);
        customreminderlist.setAdapter(arrayAdapter);

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "customreminder";
            String description = "Custom Reminder";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("customreminder",name,importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c=Calendar.getInstance();

        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);

        Date d= c.getTime();
        date1 = formater.format(d);
        Date inActiveDate = null;
        try {
            inActiveDate = formater.parse(date1);
            System.out.println("test date : "+ inActiveDate);
        } catch (ParseException e1) {
            // TODO Auto-generated catch block
            System.out.println("test date : error");
        }
//                dDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar2.getTime());
        getdate=inActiveDate.getTime();
        calender.setText(date1);//+" "+getdate
    }


    private void uploadreminder() {
        refUp= FirebaseDatabase.getInstance("https://maternal-care-fa0e8-default-rtdb.firebaseio.com/").getReference().child("customreminder").child(currentuserid);
        refUp2= FirebaseDatabase.getInstance("https://maternal-care-fa0e8-default-rtdb.firebaseio.com/").getReference().child("customreminderkey");

        String messageKey=refUp.push().getKey();
        final HashMap<String, String> remindermsgmap = new HashMap<>();
        remindermsgmap.put("date", Long.toString(getdate));
        remindermsgmap.put("msg", msg);
        refUp.child(messageKey).setValue(remindermsgmap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(customreminderActivity.this,"Alarm Set",Toast.LENGTH_LONG).show();

                    Calendar c2 = Calendar.getInstance();
                    c2.set(Calendar.HOUR_OF_DAY,15);
                    c2.set(Calendar.MINUTE,59);
                    c2.set(Calendar.SECOND,0);
                    c2.set(Calendar.MILLISECOND,0);


                    alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(customreminderActivity.this,customalarmReceiver.class);
                    pendingIntent = PendingIntent.getBroadcast(customreminderActivity.this,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
//                    long triggerAfter = 2 * 60 * 1000;
//                    long triggerEvery = 200 * 60 * 1000;
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c2.getTimeInMillis(),
                            AlarmManager.INTERVAL_DAY, pendingIntent);

//                    final HashMap<String, String> remindermsgkeymap = new HashMap<>();
//                    remindermsgkeymap.put("cnt", "1");
//                    remindermsgkeymap.put("msg", msg);
//                    refUp2.child(messageKey).setValue(remindermsgkeymap);
                    calender.setText("Tap here to pick date");
                    getdate=0;
                    remindermsg.setText(null);
                }
                else
                {
                    System.out.println(task.getException().toString());
                }
            }
        });

    }


    private void RetriveAndDisplay() {
        ref.child("customreminder").child(currentuserid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Set<String> set=new HashSet<>();
                Iterator iterator=dataSnapshot.getChildren().iterator();
//                for(DataSnapshot ds:dataSnapshot.getChildren()){
//                    String key=ds.getKey().toString();
//                    String datestring= ds.child("date").toString();
//                    Date convertDate=new Date(Long.parseLong(datestring));
//                    String message= ds.child("msg").toString();
//                    set.add("Reminder message : "+message+"\nDate : "+formater.format(convertDate));
//
//                }
                while (iterator.hasNext()){
                    String key=((DataSnapshot)iterator.next()).getKey();
                    String datestring=dataSnapshot.child(key).child("date").getValue().toString();
                    String message=dataSnapshot.child(key).child("msg").getValue().toString();
                    Date convertDate=new Date(Long.parseLong(datestring));
                    set.add("Reminder message : "+message+"\nDate : "+formater.format(convertDate));
                }

                list_of_groups.clear();
                list_of_groups.addAll(set);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}