package com.faltynka.financialdiary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.faltynka.financialdiary.sqlite.model.Record;

import java.util.List;

public class Overview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setSubtitle("Overview");

        setContentView(R.layout.activity_overview);

        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_overview);

        List<Record> records = (List<Record>) getIntent().getSerializableExtra("records");
        for (int i = 1; i <= records.size(); i++) {
            Record record = records.get(i-1);
            Button btn = new Button(this);
            btn.setId(View.generateViewId());
            final int id_ = btn.getId();
            btn.setText(record.getFromId() + "\n" +record.getToId());
            btn.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layout.addView(btn);
            Button btn1 = ((Button) findViewById(id_));
            btn1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Toast.makeText(view.getContext(),
                            "Button clicked index = " + id_, Toast.LENGTH_SHORT)
                            .show();
                }
            });
        }
    }
}
