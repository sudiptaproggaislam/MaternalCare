package com.progga.maternalcare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class answerActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private Button sendMessageButton;
    private EditText etAnswer;
    private ScrollView mScrollview;
    private TextView displaytextMessage;
    private LinearLayout answerSection;

    private FirebaseAuth mAuth;
    private DatabaseReference answerref,ref,upanswerref,changeanswervalue;

    private String currentUserId,currentUserName,currentDate,currentTime,currentKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);


        initialization();
        getUserInfo();


        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveAnswerInfoToDatabase();
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();

        currentKey=getIntent().getExtras().getString("key");
        answerref= FirebaseDatabase.getInstance("https://maternal-care-fa0e8-default-rtdb.firebaseio.com/").getReference().child("query").child(currentUserId).child("answers").child(currentKey);

        answerref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()){
                    DisplayMessage(dataSnapshot);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()){
                    DisplayMessage(dataSnapshot);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void DisplayMessage(DataSnapshot dataSnapshot) {
        Iterator iterator=dataSnapshot.getChildren().iterator();

        while (iterator.hasNext()){
            String ans=(String)((DataSnapshot)iterator.next()).getValue();
            String ansdate=(String)((DataSnapshot)iterator.next()).getValue();
            String ansname=(String)((DataSnapshot)iterator.next()).getValue();
            String anstime=(String)((DataSnapshot)iterator.next()).getValue();

            displaytextMessage.append(ansname+":\n"+ans+"\n"+anstime+"      "+ansdate+"\n\n\n");
            mScrollview.fullScroll(ScrollView.FOCUS_DOWN);
        }

    }
    private void SaveAnswerInfoToDatabase() {
        String message=etAnswer.getText().toString();

        if (TextUtils.isEmpty(message)){
            Toast.makeText(answerActivity.this,"Please Write Answer First",Toast.LENGTH_SHORT).show();
        }else{
            Calendar calfordate=Calendar.getInstance();
            SimpleDateFormat currentDateFormat=new SimpleDateFormat("MMM dd,yyyy");
            currentDate=currentDateFormat.format(calfordate.getTime());

            Calendar calforTime=Calendar.getInstance();
            SimpleDateFormat currentTimeFormat=new SimpleDateFormat("hh:mm a");
            currentTime=currentTimeFormat.format(calforTime.getTime());


            HashMap<String,Object> ansInfoMap= new HashMap<>();
            ansInfoMap.put("name",currentUserName);
            ansInfoMap.put("answer",message);
            ansInfoMap.put("date",currentDate);
            ansInfoMap.put("time",currentTime);
            String newanswerKey=upanswerref.push().getKey();
            upanswerref.child(newanswerKey).updateChildren(ansInfoMap);
            changeanswervalue.setValue("yes");
            etAnswer.setText(null);
        }
    }

    private void getUserInfo() {

        ref.child("User").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    currentUserName=dataSnapshot.child("name").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initialization() {
//        Toast.makeText(answerActivity.this,getIntent().getExtras().getString("key"),Toast.LENGTH_LONG).show();
        currentKey=getIntent().getExtras().getString("key");
        mToolbar=(Toolbar)findViewById(R.id.answer_activity_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Maternal Care");

        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();

        ref=FirebaseDatabase.getInstance("https://maternal-care-fa0e8-default-rtdb.firebaseio.com/").getReference();
        upanswerref=FirebaseDatabase.getInstance("https://maternal-care-fa0e8-default-rtdb.firebaseio.com/").getReference().child("query").child(currentUserId).child("answers").child(currentKey);
        changeanswervalue=FirebaseDatabase.getInstance("https://maternal-care-fa0e8-default-rtdb.firebaseio.com/").getReference().child("query").child(currentUserId).child("question").child(currentKey).child("answers");

        mScrollview=(ScrollView)findViewById(R.id.svAnswers);
        displaytextMessage=(TextView)findViewById(R.id.tvAnswer);
        etAnswer=(EditText)findViewById(R.id.etAnswer);
        sendMessageButton=(Button)findViewById(R.id.btnSendAnswer);

        ///only admins can answer
        answerSection=(LinearLayout)findViewById(R.id.llanswer);
//        answerSection.setVisibility(View.INVISIBLE);
//        answerSection.removeAllViews();

    }
}