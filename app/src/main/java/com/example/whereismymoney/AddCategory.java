package com.example.whereismymoney;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

@RequiresApi(api = Build.VERSION_CODES.O)
public class AddCategory extends AppCompatActivity {

    Database DB = MainActivity.DB;
    Button addCategoryBTN;
    Spinner chooseCategoryColor;
    EditText addCategoryNameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);/*
        colorMap = new HashMap<String, Color>();
        colorMap.put("Red", Color.valueOf(Color.RED));
        colorMap.put("Blue", Color.valueOf(Color.BLUE));
        colorMap.put("Yellow", Color.valueOf(Color.YELLOW));*/
        chooseCategoryColor = (Spinner) findViewById(R.id.chooseCategoryColor);
        addCategoryNameInput = (EditText) findViewById(R.id.addCategoryNameInput);
        addCategoryBTN = (Button) findViewById(R.id.addCategoryBTN);
        addCategoryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCategory();
            }
        });
    }
    public void addCategory(){
        String categoryName = addCategoryNameInput.getText().toString();
        //Object categoryColor = colorDictionary.get(chooseCategoryColor);
        Color categoryColor = Color.valueOf(Color.parseColor(String.valueOf(chooseCategoryColor.getSelectedItem())));
        DB.MakeCategory(categoryName, categoryColor);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
