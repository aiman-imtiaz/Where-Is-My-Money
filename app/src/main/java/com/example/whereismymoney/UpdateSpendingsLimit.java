package com.example.whereismymoney;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

@RequiresApi(api = Build.VERSION_CODES.O)
public class UpdateSpendingsLimit extends AppCompatActivity {

    Database DB = MainActivity.DB;
    TextView currentSpendingsLimit;
    EditText editSpendingsLimitAmount;
    Button editSpendingsLimitBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_spendings_limit);
        currentSpendingsLimit = (TextView) findViewById(R.id.currentSpendingsLimit);
        editSpendingsLimitAmount = (EditText) findViewById(R.id.editSpendingsLimitAmount);
        editSpendingsLimitBTN = (Button) findViewById(R.id.editSpendingsLimitBTN);
        currentSpendingsLimit.setText("$" + String.valueOf(DB.spendingsLimit));
        editSpendingsLimitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                editSpendingsLimit();
            }
        });
    }

    public void editSpendingsLimit(){
        DB.spendingsLimit = Float.parseFloat(editSpendingsLimitAmount.getText().toString());
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
