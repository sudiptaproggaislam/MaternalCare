package com.progga.maternalcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

public class postpartumActivity extends AppCompatActivity {
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postpartum);

        mToolbar=(Toolbar)findViewById(R.id.postpartum_activity_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Optimizing Postpartum Care");
    }
}