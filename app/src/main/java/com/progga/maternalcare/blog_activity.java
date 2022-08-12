package com.progga.maternalcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class blog_activity extends AppCompatActivity {
    private Toolbar mToolbar;
    private LinearLayout pregnancy,postpartum,childcare,earlychildcare;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);


        mToolbar=(Toolbar)findViewById(R.id.blog_activity_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Blogs");

        pregnancy=(LinearLayout)findViewById(R.id.pregnancy);
        postpartum=(LinearLayout)findViewById(R.id.postpartum);
        childcare=(LinearLayout)findViewById(R.id.childcare);
        earlychildcare=(LinearLayout)findViewById(R.id.earlychildcare);

        pregnancy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent= new Intent(blog_activity.this,pregnancyActivity.class);
                startActivity(loginIntent);
            }
        });

        postpartum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent= new Intent(blog_activity.this,postpartumActivity.class);
                startActivity(loginIntent);
            }
        });

        childcare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent= new Intent(blog_activity.this,childcareActivity.class);
                startActivity(loginIntent);
            }
        });

        earlychildcare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent= new Intent(blog_activity.this,earlyChildCareActivity.class);
                startActivity(loginIntent);
            }
        });
    }
}