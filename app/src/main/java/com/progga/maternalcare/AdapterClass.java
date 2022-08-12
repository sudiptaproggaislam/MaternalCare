package com.progga.maternalcare;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterClass extends  RecyclerView.Adapter<AdapterClass.MyViewHolder>{

    ArrayList<deal> list;
    private Context context;
    private String booksCategory;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    String currentUserId;

    public AdapterClass(Context context) {
        this.context = context;
    }

    public AdapterClass(ArrayList<deal> list) {
        this.list=list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_holder,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.ques.setText(list.get(position).getQuestion());
        if(list.get(position).getQuerymark().equals("yes")){
            holder.av.setVisibility(View.VISIBLE);
        }else{
            holder.av.setVisibility(View.INVISIBLE);
        }

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView ques;
        Button button;
        CircleImageView av;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ques=itemView.findViewById(R.id.nameDoc);
            button=itemView.findViewById(R.id.btnrv);
            av=itemView.findViewById(R.id.imgBtnAvailablity);
            button.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            mAuth=FirebaseAuth.getInstance();
            currentUserId=mAuth.getCurrentUser().getUid();
            reference= FirebaseDatabase.getInstance("https://maternal-care-fa0e8-default-rtdb.firebaseio.com/").getReference().child("query").child(currentUserId).child("question").child(list.get(getAdapterPosition()).getKey()).child("querymark");

            reference.setValue("no");
            Intent intent = new Intent(v.getContext(),answerActivity.class);
            intent.putExtra("key",list.get(getAdapterPosition()).getKey());
            v.getContext().startActivity(intent);
        }
    }

}


