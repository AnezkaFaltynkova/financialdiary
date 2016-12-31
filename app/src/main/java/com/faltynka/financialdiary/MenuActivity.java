package com.faltynka.financialdiary;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.faltynka.financialdiary.sqlite.helper.DatabaseHelper;
import com.faltynka.financialdiary.sqlite.model.Category;
import com.faltynka.financialdiary.sqlite.model.Record;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {
    private Button btnLogout;
    private Button btnNewCategory;
    private Button btnNewRecord;
    private Button btnOverview;
    private Button btnReports;
    private Button btnSynchronize;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private DatabaseHelper mydb;
    private DatabaseReference mDatabase;
    private String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        auth = FirebaseAuth.getInstance();
        mydb = DatabaseHelper.getInstance(this);

        btnLogout = (Button) findViewById(R.id.logout_button);
        btnNewCategory = (Button) findViewById(R.id.new_category_button);
        btnNewRecord = (Button) findViewById(R.id.new_record_button);
        btnOverview = (Button) findViewById(R.id.overview_button);
        btnReports = (Button) findViewById(R.id.reports_button);
        btnSynchronize = (Button) findViewById(R.id.synchronize_button);

        // this listener will be called when there is change in firebase user session
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MenuActivity.this, MainActivity.class));
                    finish();
                }
            }
        };

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                synchronizeAllData(true);

            }
        });

        btnNewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, NewCategory.class));
            }
        });

        btnNewRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, NewRecord.class));
            }
        });

        btnOverview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, SelectionOverview.class));
            }
        });

        btnReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, SelectionReportsActivity.class));
            }
        });

        btnSynchronize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                synchronizeAllData(false);
            }
        });
    }

    private void synchronizeRecords(final boolean logout) {
        mDatabase.child("users").child(mUserId).child("record").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Record> recordsInFirebase = new ArrayList<>();
                for (DataSnapshot recordSnapshot : snapshot.getChildren()) {
                    Record record = recordSnapshot.getValue(Record.class);
                    record.setFirebaseId(recordSnapshot.getKey());
                    recordsInFirebase.add(record);
                }
                List<Record> recordsInSQLite = mydb.selectAllRecords();
                for (Record firebaseRecord : recordsInFirebase) {
                    if (!recordsInSQLite.contains(firebaseRecord)) {
                        mydb.createRecordWithId(firebaseRecord);
                    } else {
                        Record recordInSQL = recordsInSQLite.get(recordsInSQLite.indexOf(firebaseRecord));
                        if (recordInSQL.getEdited() < firebaseRecord.getEdited()) {
                            mydb.editRecord(firebaseRecord);
                        } else if (recordInSQL.getEdited() > firebaseRecord.getEdited()){
                            mDatabase.child("users").child(mUserId).child("record").child(firebaseRecord.getFirebaseId()).setValue(recordInSQL);
                        }
                    }
                }
                for (Record sqliteRecord : recordsInSQLite) {
                    if (!recordsInFirebase.contains(sqliteRecord)) {
                        mDatabase.child("users").child(mUserId).child("record").push().setValue(sqliteRecord);
                    }
                }
                if (logout) {
                    mydb.deleteData();
                    auth.signOut();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void synchronizeAllData(final boolean logout) {
        FirebaseUser mFirebaseUser = auth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if (mFirebaseUser == null) {
            startActivity(new Intent(MenuActivity.this, MainActivity.class));
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
                    List<Category> categoriesInSQLite = mydb.selectAllCategories();
                    for (Category firebaseCategory : categoriesInFirebase) {
                        if (!categoriesInSQLite.contains(firebaseCategory)) {
                            mydb.createCategoryWithId(firebaseCategory);
                        }
                    }
                    for(Category sqliteCategory : categoriesInSQLite) {
                        if (!categoriesInFirebase.contains(sqliteCategory)) {
                            mDatabase.child("users").child(mUserId).child("category").push().setValue(sqliteCategory);
                        }
                    }
                    if (logout) {
                        synchronizeRecords(true);
                    } else {
                        synchronizeRecords(false);
                    }
                    Toast.makeText(MenuActivity.this, "Synchronized", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        auth.removeAuthStateListener(authListener);
    }
}
