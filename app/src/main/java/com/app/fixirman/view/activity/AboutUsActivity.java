package com.app.fixirman.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.app.fixirman.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        FloatingActionButton btn = findViewById(R.id.btn_back);
        btn.setOnClickListener(v-> onBackPressed());
    }
}