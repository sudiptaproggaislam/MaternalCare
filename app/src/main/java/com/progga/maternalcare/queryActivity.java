package com.progga.maternalcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class queryActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private FirebaseAuth mAuth;
    private DatabaseReference queryref,ref;
    private RecyclerView listView;
    private EditText etQueryMsg;
    private Button sendQuery;
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
        setContentView(R.layout.activity_query);



        mToolbar=(Toolbar)findViewById(R.id.query_activity_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Maternal Care");

        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();


        queryref= FirebaseDatabase.getInstance("https://maternal-care-fa0e8-default-rtdb.firebaseio.com/").getReference().child("query").child(currentUserId).child("question");


        InitializeFields();
        retriveDateView();

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                msg=parent.getItemAtPosition(position).toString();
//                Toast.makeText(queryActivity.this,msg,Toast.LENGTH_LONG).show();
//            }
//        });

        sendQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String question = etQueryMsg.getText().toString();
                if (!TextUtils.isEmpty(question)){
                    String messageKey=queryref.push().getKey();
                    HashMap<String, Object> messageInfoMap = new HashMap<>();
                    messageInfoMap.put("question", question);
                    messageInfoMap.put("key", messageKey);
                    messageInfoMap.put("answers", "new");
                    messageInfoMap.put("querymark", "new");
                    messageInfoMap.put("uid", currentUserId);
                    queryref.child(messageKey).updateChildren(messageInfoMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(queryActivity.this, "Query has been sent", Toast.LENGTH_SHORT).show();
                                etQueryMsg.setText(null);
                            } else {
                                String message = task.getException().toString();
                                Toast.makeText(queryActivity.this, "Error : " + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(queryActivity.this, "Enter Query", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


    private void InitializeFields() {
        listView=(RecyclerView)findViewById(R.id.lvQuery);
        sendQuery=(Button)findViewById(R.id.btnSendQuery);
        etQueryMsg=(EditText)findViewById(R.id.etQuery);
        arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list_of_groups);
//        listView.setAdapter(arrayAdapter);

        ref= FirebaseDatabase.getInstance("https://maternal-care-fa0e8-default-rtdb.firebaseio.com/").getReference().child("query").child(currentUserId);
    }

    private void retriveDateView() {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

//                Set<String> set=new HashSet<>();
                ArrayList<deal> list=new ArrayList<>();


                if(dataSnapshot.child("question").exists()) {
                    Iterator iterator=dataSnapshot.child("question").getChildren().iterator();
                    while (iterator.hasNext()) {
                        list.add(dataSnapshot.child("question").child(((DataSnapshot) iterator.next()).getKey()).getValue(deal.class));
                    }
                }


                AdapterClass adapterClass=new AdapterClass(list);
                listView.setAdapter(adapterClass);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
