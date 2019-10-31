package com.example.whereismymoney;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

@RequiresApi(api = Build.VERSION_CODES.O)
public class AddCategory extends AppCompatActivity {

    Database DB = MainActivity.DB;
    Button addCategoryBTN;
    Spinner chooseCategoryColor;
    EditText addCategoryNameInput;
    Dictionary colorDictionary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        colorDictionary = new Hashtable();
        colorDictionary.put("Red", Color.valueOf(Color.RED));
        colorDictionary.put("Blue", Color.valueOf(Color.BLUE));
        colorDictionary.put("Yellow", Color.valueOf(Color.YELLOW));
        chooseCategoryColor = (Spinner) findViewById(R.id.chooseCategoryColor);
        addCategoryNameInput = (EditText) findViewById(R.id.addCategoryNameInput);
        addCategoryBTN = (Button) findViewById(R.id.addSpendingBTN);
        addCategoryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCategory();
            }
        });
    }
    public void addCategory(){
        String categoryName = addCategoryNameInput.getText().toString();
        Color categoryColor = colorDictionary.get(chooseCategoryColor);
        DB.MakeCategory(categoryName, categoryColor);
    }
}
