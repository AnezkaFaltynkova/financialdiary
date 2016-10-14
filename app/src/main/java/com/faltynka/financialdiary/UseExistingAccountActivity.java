package com.faltynka.financialdiary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class UseExistingAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setSubtitle("Use Existing Login");
        setContentView(R.layout.activity_use_existing_account);

    }
}
