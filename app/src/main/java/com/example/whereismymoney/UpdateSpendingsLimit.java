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

/**
 *
 * UpdateSpendingLimit class contains frontend methods to take user input on spending limit.
 *
 * @author Aiman, Casper, Elaine and Leyli
 * @version 1.0
 *
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class UpdateSpendingsLimit extends AppCompatActivity {

    Database DB = launcher.DB;
    TextView currentSpendingsLimit;
    EditText editSpendingsLimitAmount;
    Button editSpendingsLimitBTN;

    /**
     * Method takes user input for spending limit and supplies it to the method that edits the
     * spending limit.
     */
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

    /**
     * Method edits the spending limit.
     */
    public void editSpendingsLimit(){
        DB.spendingsLimit = Float.parseFloat(editSpendingsLimitAmount.getText().toString());
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
