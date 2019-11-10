package com.example.whereismymoney;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 *
 * MakeCategoryOrSpending contains frontend methods that take user input.
 *
 * @see AddSpending
 * @see AddCategory
 * @author Aiman, Casper, Elaine and Leyli
 * @version 1.0
 *
 */
public class MakeCategoryOrSpending extends AppCompatActivity {

    Button makeSpendingBTN;
    Button makeCategoryBTN;
    Button updateSpendingsLimitBTN;

    /**
     * Method takes user input whether they want to create a category or add a spending.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_category_or_spending);

        makeSpendingBTN = (Button) findViewById(R.id.makeSpendingBTN);
        makeSpendingBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openAddSpending();
            }
        });
        makeCategoryBTN = (Button) findViewById(R.id.makeCategoryBTN);
        makeCategoryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openAddCategory();
            }
        });
        updateSpendingsLimitBTN = (Button) findViewById(R.id.updateSpendingsLimitBTN);
        updateSpendingsLimitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUpdateSpendingsLimit();
            }
        });
    }

    /**
     * Method redirects the user to the AddSpending activity.
     */
    public void openAddSpending(){
        Intent intent = new Intent(this, AddSpending.class);
        startActivity(intent);
    }

    /**
     * Method redirects the user to the AddCategory activity.
     */
    public void openAddCategory(){
        Intent intent = new Intent(this, AddCategory.class);
        startActivity(intent);
    }

    /**
     * Method redirects the user to the UpdateSpendingsLimit activity.
     */
    public void openUpdateSpendingsLimit(){
        Intent intent = new Intent(this, UpdateSpendingsLimit.class);
        startActivity(intent);
    }
}
