package com.example.whereismymoney;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.whereismymoney.JSONParser;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * AddCategory calls methods from Database to add a category object.
 *
 * AddCategory takes arguments from users by reading the information from user input, supplies the
 * arguments to methods from the Database class and creates a new category on the online database.
 *
 * @see JSONParser
 * @see Database
 *
 * @author Aiman, Casper, Elaine and Leyli
 * @version 1.0
 *
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class AddCategory extends AppCompatActivity {

    private ProgressDialog pDialog;

    JSONParser jsonParser = new com.example.whereismymoney.JSONParser();
    Database DB = launcher.DB;
    Button addCategoryBTN;
    Spinner chooseCategoryColor;
    EditText addCategoryNameInput;

    String categoryName;
    String categoryColor;


    //TODO: Make a new url for this, currently a placeholder
    private static String url_create_category = "http://192.168.64.2/android_connect/create_category.php";

    private static String TAG_SUCCESS = "success";

    /**
     *
     *
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        chooseCategoryColor = (Spinner) findViewById(R.id.chooseCategoryColor);
        addCategoryNameInput = (EditText) findViewById(R.id.addCategoryNameInput);
        addCategoryBTN = (Button) findViewById(R.id.addCategoryBTN);
        addCategoryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryName = addCategoryNameInput.getText().toString();
                categoryColor = String.valueOf(Color.parseColor(String.valueOf(chooseCategoryColor.getSelectedItem())));
                new CreateNewCategory().execute();
                DB.updateCategories();
            }
        });
    }

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


            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("name", categoryName));
            params.add(new BasicNameValuePair("color", categoryColor));

            JSONObject json = jsonParser.makeHttpRequest(url_create_category, "POST", params);

            if (json == null){
                Log.d("NEWCAT", "JSON is null");
            }

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

