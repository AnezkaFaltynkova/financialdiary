package com.faltynka.financialdiary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // if (user in database) than skip two button window
        setContentView(R.layout.activity_main);
    }

    public void displayCreateAccountScreen(View view) {
        Intent intent = new Intent(this, CreateNewAccountActivity.class);
        startActivity(intent);
    }

    public void displayUseExistingAccountScreen(View view) {
        Intent intent = new Intent(this, UseExistingAccountActivity.class);
        startActivity(intent);
    }
}
