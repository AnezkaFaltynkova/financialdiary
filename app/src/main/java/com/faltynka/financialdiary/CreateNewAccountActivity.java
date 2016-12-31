package com.faltynka.financialdiary;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.faltynka.financialdiary.sqlite.helper.DatabaseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateNewAccountActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private Button btnCreateAccount;
    private EditText inputEmail;
    private EditText inputPassword;
    private DatabaseHelper mydb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setSubtitle("Create New Account");

        setContentView(R.layout.activity_create_new_account);

        auth = FirebaseAuth.getInstance();
        mydb = DatabaseHelper.getInstance(this);

        btnCreateAccount = (Button) findViewById(R.id.create_account_button);
        inputEmail = (EditText) findViewById(R.id.emailNewEditText);
        inputPassword = (EditText) findViewById(R.id.passwordNewEditText);

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = inputEmail.getText().toString().trim();
                final String password = inputPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(CreateNewAccountActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    mydb.insertUser(email, password);
                                    Toast.makeText(CreateNewAccountActivity.this, "Your account was created.", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(CreateNewAccountActivity.this, MenuActivity.class));
                                    MainActivity.getInstance().finish();
                                    finish();
                                } else {
                                    if(task.getException().getMessage().equals("The email address is already in use by another account.")) {
                                        Toast.makeText(CreateNewAccountActivity.this, "The email address is already in use by another account.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(CreateNewAccountActivity.this, "Authentication failed." + task.getException(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });

            }
        });
    }

}
