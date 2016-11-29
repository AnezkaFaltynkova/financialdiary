package com.faltynka.financialdiary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.faltynka.financialdiary.sqlite.helper.DatabaseHelper;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper mydb;
    static MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = this;
        mydb = new DatabaseHelper(this);
        if (mydb.existUser()) {
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
            finish();
        } else {
            setContentView(R.layout.activity_main);
        }
    }

    public void displayCreateAccountScreen(View view) {
        Intent intent = new Intent(this, CreateNewAccountActivity.class);
        startActivity(intent);
    }

    public void displayUseExistingAccountScreen(View view) {
        Intent intent = new Intent(this, UseExistingAccountActivity.class);
        startActivity(intent);
    }

    public static MainActivity getInstance() {
        return mainActivity;
    }
}
