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
        mydb = new DatabaseHelper(this);

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
                                Toast.makeText(CreateNewAccountActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(CreateNewAccountActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    mydb.insertUser(email, password);
                                    startActivity(new Intent(CreateNewAccountActivity.this, MenuActivity.class));
                                    MainActivity.getInstance().finish();
                                    finish();
                                }
                            }
                        });

            }
        });
    }

}
