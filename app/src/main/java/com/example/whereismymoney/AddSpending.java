package com.example.whereismymoney;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.time.Instant;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.O)
public class AddSpending extends AppCompatActivity {
    TextView output;
    Button addSpendingBTN;
    EditText spendingAmountInput;
    Spinner chooseSpendingCategory;
    Database DB = MainActivity.DB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_spending);
        output = (TextView) findViewById(R.id.output);
        spendingAmountInput = (EditText) findViewById(R.id.spendingAmountInput);
        chooseSpendingCategory = (Spinner) findViewById(R.id.chooseSpendingCategory);
        ArrayAdapter<Object> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,  DB.categoryMap.keySet().toArray());
        chooseSpendingCategory.setAdapter(dataAdapter);
        addSpendingBTN = (Button) findViewById(R.id.addSpendingBTN);
        addSpendingBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSpending();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addSpending(){
        output.setText(spendingAmountInput.getText().toString());
        float spendingAmount = Float.parseFloat(spendingAmountInput.getText().toString());

        Date newDate = Date.from(Instant.now());
        //TODO: Change category to spinner element
        DB.MakeSpending(spendingAmount, newDate, (Category) DB.categoryMap.get(String.valueOf(chooseSpendingCategory.getSelectedItem())/*DB.Categories.get(0)*/));
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
