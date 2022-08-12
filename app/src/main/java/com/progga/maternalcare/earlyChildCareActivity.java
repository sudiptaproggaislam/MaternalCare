package com.progga.maternalcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

public class earlyChildCareActivity extends AppCompatActivity {
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_early_child_care);

        mToolbar=(Toolbar)findViewById(R.id.earlychildcare_activity_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Early Child Care");
    }
}