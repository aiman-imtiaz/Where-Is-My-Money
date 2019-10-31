package com.example.whereismymoney;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MakeCategoryOrSpending extends AppCompatActivity {

    Button makeSpendingBTN;
    Button makeCategoryBTN;

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
    }
    public void openAddSpending(){
        Intent intent = new Intent(this, AddSpending.class);
        startActivity(intent);
    }

    public void openAddCategory(){
        Intent intent = new Intent(this, AddCategory.class);
        startActivity(intent);
    }
}
