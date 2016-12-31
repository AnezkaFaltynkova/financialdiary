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
import com.faltynka.financialdiary.sqlite.model.Category;
import com.faltynka.financialdiary.sqlite.model.Record;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UseExistingAccountActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private Button btnLogin;
    private Button btnForgotPassword;
    private EditText inputEmail;
    private EditText inputPassword;
    private DatabaseHelper mydb;
    private DatabaseReference mDatabase;
    private String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setSubtitle("Use Existing Login");

        auth = FirebaseAuth.getInstance();
        mydb = DatabaseHelper.getInstance(this);

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(UseExistingAccountActivity.this, MenuActivity.class));
            finish();
        }

        setContentView(R.layout.activity_use_existing_account);

        inputEmail = (EditText) findViewById(R.id.emailExistingEditText);
        inputPassword = (EditText) findViewById(R.id.passwordExistingEditText);
        btnLogin = (Button) findViewById(R.id.loginButton);
        btnForgotPassword = (Button) findViewById(R.id.btn_reset_password);

        auth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(UseExistingAccountActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        inputPassword.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(UseExistingAccountActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    mydb.insertUser(email, password);

                                    FirebaseUser mFirebaseUser = auth.getCurrentUser();
                                    mDatabase = FirebaseDatabase.getInstance().getReference();
                                    if (mFirebaseUser == null) {
                                        startActivity(new Intent(UseExistingAccountActivity.this, MainActivity.class));
                                        finish();
                                    } else {
                                        mUserId = mFirebaseUser.getUid();
                                        mDatabase.child("users").child(mUserId).child("category").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot snapshot) {
                                                List<Category> categoriesInFirebase = new ArrayList<>();
                                                for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                                                    Category category = categorySnapshot.getValue(Category.class);
                                                    categoriesInFirebase.add(category);
                                                }
                                                for (Category firebaseCategory : categoriesInFirebase) {
                                                    mydb.createCategoryWithId(firebaseCategory);
                                                }
                                                downloadRecords();
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }

                                    Intent intent = new Intent(UseExistingAccountActivity.this, MenuActivity.class);
                                    startActivity(intent);
                                    MainActivity.getInstance().finish();
                                    finish();
                                }
                            }
                        });
            }
        });

        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UseExistingAccountActivity.this, ForgotPasswordActivity.class));
            }
        });

    }

    private void downloadRecords() {
        mDatabase.child("users").child(mUserId).child("record").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Record> recordsInFirebase = new ArrayList<>();
                for (DataSnapshot recordSnapshot : snapshot.getChildren()) {
                    Record record = recordSnapshot.getValue(Record.class);
                    record.setFirebaseId(recordSnapshot.getKey());
                    recordsInFirebase.add(record);
                }
                for (Record firebaseRecord : recordsInFirebase) {
                    mydb.createRecordWithId(firebaseRecord);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
