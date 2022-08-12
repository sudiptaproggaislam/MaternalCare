package com.progga.maternalcare;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class showcustomremindermsg extends AppCompatActivity {
    private TextView msg;
    private Toolbar mToolbar;
    String getMsg,currentUserId,getdate;
    long cDate=0;
    SimpleDateFormat formater = new SimpleDateFormat("EEE, d MMM yyyy");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showcustomremindermsg);


        mToolbar=(Toolbar)findViewById(R.id.showcustomReminderMsg_activity_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Reminder");

        msg=  (TextView)findViewById(R.id.tvreminderMsg);
        getdate=getIntent().getExtras().getString("date");
        getMsg=getIntent().getExtras().getString("msg");
        cDate=Long.parseLong(getdate);
        Date m=new Date(cDate);
        currentUserId=getIntent().getExtras().getString("id");
        msg.setText("You Set an Alarm :\n"+"Set message : "+getMsg+"\nDate : "+formater.format(m));

//        int cnt=0;
//        DatabaseReference refUpdate= FirebaseDatabase.getInstance("https://maternal-care-fa0e8-default-rtdb.firebaseio.com/").getReference();
//        refUpdate.child("customreminder").child(currentUserId).child(getMsg).setValue("0");
    }
}