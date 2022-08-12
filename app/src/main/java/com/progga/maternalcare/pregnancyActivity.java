package com.progga.maternalcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

public class pregnancyActivity extends AppCompatActivity {
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pregnancy);

        mToolbar=(Toolbar)findViewById(R.id.prenancy_activity_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Pregnancy Week by Week");
    }
}