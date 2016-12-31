package com.faltynka.financialdiary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.faltynka.financialdiary.sqlite.helper.DatabaseHelper;

public class NewCategory extends AppCompatActivity {
    private DatabaseHelper mydb;
    private Spinner typeSpinner;
    private Button btnCreate;
    private EditText nameEditText;
    private String typeString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setSubtitle("New Category");

        setContentView(R.layout.activity_new_category);

        mydb = DatabaseHelper.getInstance(this);

        btnCreate = (Button) findViewById(R.id.create_new_category_button);
        nameEditText = (EditText) findViewById(R.id.nameNewCategory);
        typeSpinner = (Spinner) findViewById(R.id.new_category_type_spinner);
        typeSpinner.setOnItemSelectedListener(new ItemSelectedListener());

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString().trim();
                if (name.isEmpty()) {
                    Toast.makeText(NewCategory.this, "Fill a name", Toast.LENGTH_LONG).show();
                } else {
                    int typeId = mydb.findTypeIdByName(typeString);
                    if (mydb.existCategory(name, typeId)) {
                        Toast.makeText(NewCategory.this, "This category already exists", Toast.LENGTH_LONG).show();
                    } else {
                        mydb.createCategory(name, typeId);
                        Toast.makeText(NewCategory.this,
                                "New category was created",
                                Toast.LENGTH_LONG).show();
                        startActivity(new Intent(NewCategory.this, NewCategory.class));
                        finish();
                    }
                }
            }
        });
    }

    public class ItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            typeString = String.valueOf(typeSpinner.getSelectedItem());
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg) {

        }
    }


}
