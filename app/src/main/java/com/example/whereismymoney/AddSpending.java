package com.example.whereismymoney;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.time.Instant;
import java.util.Date;

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
        DB.MakeSpending(spendingAmount, newDate, DB.Categories.get(0));
    }
}
