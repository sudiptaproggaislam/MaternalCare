package com.progga.maternalcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class notificationActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef,ref;
    private ListView listView;
    String currentUserId,msg,fullMsg;

    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_groups=new ArrayList<>();

    SimpleDateFormat dateFormat1 = new SimpleDateFormat("EEE, d MMM yyyy");
    SimpleDateFormat dateFormat2 = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");


    long date1 = 0;
    long date2 = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        mToolbar=(Toolbar)findViewById(R.id.notification_activity_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Maternal Care");

        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();


        rootRef= FirebaseDatabase.getInstance("https://maternal-care-fa0e8-default-rtdb.firebaseio.com/").getReference();
        rootRef.child("User").child(currentUserId).child("rnotification").setValue("0");

        InitializeFields();

        retriveDateView();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                msg=parent.getItemAtPosition(position).toString();
                Toast.makeText(notificationActivity.this,msg,Toast.LENGTH_LONG).show();
            }
        });
    }

    private void retriveDateView() {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Set<String> set=new HashSet<>();

                for (DataSnapshot ds:dataSnapshot.child("notification").child(currentUserId).getChildren()) {
//                    if(ds.child("notification").child(currentUserId).exists()) {
                        String s = ds.child("rtime").getValue().toString();
                        date1 = Long.parseLong(s);
                        Date rDateGet = new Date(date1);
                        if (ds.child("key").getValue().toString().equals("2")) {
                            set.add("Reminder : " + ds.child("msg").getValue().toString() + "\nReminder at : "+ ds.child("ctime").getValue().toString());
                        }
//                    }
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

    private void InitializeFields() {
        listView=(ListView)findViewById(R.id.lvReminderNotification);
        arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list_of_groups);
        listView.setAdapter(arrayAdapter);

        ref= FirebaseDatabase.getInstance("https://maternal-care-fa0e8-default-rtdb.firebaseio.com/").getReference();
    }
}