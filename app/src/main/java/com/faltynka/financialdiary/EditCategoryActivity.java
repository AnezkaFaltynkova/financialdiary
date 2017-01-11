package com.faltynka.financialdiary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.faltynka.financialdiary.sqlite.helper.DatabaseHelper;
import com.faltynka.financialdiary.sqlite.model.Category;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class EditCategoryActivity extends AppCompatActivity {
    private DatabaseHelper mydb;
    private Category category;
    private Spinner typeSpinner;
    private Button  btnSave;
    private String typeString;
    private EditText nameEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        mydb = DatabaseHelper.getInstance(this);

        category = (Category) getIntent().getSerializableExtra("category");

        typeSpinner = (Spinner) findViewById(R.id.edit_category_type_spinner);
        String typeName = mydb.findTypeNameForCategoryWithId(category.getId());
        ArrayAdapter myAdap = (ArrayAdapter) typeSpinner.getAdapter();
        int spinnerPosition = myAdap.getPosition(typeName);
        typeSpinner.setSelection(spinnerPosition);
        typeSpinner.setOnItemSelectedListener(new EditCategoryActivity.ItemSelectedListener());

        nameEditText = (EditText) findViewById(R.id.nameEditCategory);
        nameEditText.setText(category.getName());
        btnSave = (Button) findViewById(R.id.save_edited_category_button);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString().trim();
                if (name.isEmpty()) {
                    Toast.makeText(EditCategoryActivity.this, "Fill a name", Toast.LENGTH_LONG).show();
                } else {
                    int typeId = mydb.findTypeIdByName(typeString);
                    if (mydb.existCategory(name, typeId)) {
                        Toast.makeText(EditCategoryActivity.this, "This category already exists", Toast.LENGTH_LONG).show();
                    } else {
                        Date edited = new Date();
                        category.setEdited(edited.getTime());
                        category.setName(name);
                        category.setType(typeId);
                        mydb.editCategory(category);
                        Toast.makeText(EditCategoryActivity.this,
                                "Category was edited",
                                Toast.LENGTH_LONG).show();
                        OverviewCategories.getInstance().finish();
                        Intent intent = new Intent(EditCategoryActivity.this, OverviewCategories.class);
                        List<Category> asset = mydb.selectAllCategoriesNotDeletedWithType("Asset");
                        List<Category> income = mydb.selectAllCategoriesNotDeletedWithType("Income");
                        List<Category> expense = mydb.selectAllCategoriesNotDeletedWithType("Expense");
                        List<Category> liability = mydb.selectAllCategoriesNotDeletedWithType("Liability");
                        List<Category> other = mydb.selectAllCategoriesNotDeletedWithType("Other");
                        intent.putExtra("asset", (Serializable) asset);
                        intent.putExtra("income", (Serializable) income);
                        intent.putExtra("expense", (Serializable) expense);
                        intent.putExtra("liability", (Serializable) liability);
                        intent.putExtra("other", (Serializable) other);
                        startActivity(intent);
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
