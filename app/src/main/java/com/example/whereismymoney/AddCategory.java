package com.example.whereismymoney;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


import org.apache.http.NameValuePair;
import org.w3c.dom.Text;

@RequiresApi(api = Build.VERSION_CODES.O)
public class AddCategory extends AppCompatActivity {

    private ProgressDialog pDialog;

    JSONParser jsonParser = new com.example.whereismymoney.JSONParser();
    Database DB = MainActivity.DB;
    Button addCategoryBTN;
    Spinner chooseCategoryColor;
    EditText addCategoryNameInput;


    //TODO: Make a new url for this, currently a placeholder
    private static String url_create_category = "http://192.168.64.2/android_connect/create_product.php";

    private static String TAG_SUCCESS = "success";

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
                //addCategory();
                new CreateNewCategory().execute();
            }
        });
    }
    /*public void addCategory(){
        String categoryName = addCategoryNameInput.getText().toString();
        //Object categoryColor = colorDictionary.get(chooseCategoryColor);
        Color categoryColor = Color.valueOf(Color.parseColor(String.valueOf(chooseCategoryColor.getSelectedItem())));
        DB.MakeCategory(categoryName, categoryColor);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }*/

    class CreateNewCategory extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = new ProgressDialog(AddCategory.this);
            pDialog.setMessage("Creating Category...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            String categoryName = addCategoryNameInput.getText().toString();
            String categoryColor = String.valueOf(Color.parseColor(String.valueOf(chooseCategoryColor.getSelectedItem())));


            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("name", categoryName));
            params.add(new BasicNameValuePair("color", categoryColor));

            JSONObject json = jsonParser.makeHttpRequest(url_create_category, "POST", params);

            try{
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);

                    finish();
                }
                else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url){
            pDialog.dismiss();
        }
    }
}

