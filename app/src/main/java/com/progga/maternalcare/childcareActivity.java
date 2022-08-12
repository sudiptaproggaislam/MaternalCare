package com.progga.maternalcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

public class childcareActivity extends AppCompatActivity {
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_childcare);

        mToolbar=(Toolbar)findViewById(R.id.childcare_activity_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Care of the Newborn");
    }
}