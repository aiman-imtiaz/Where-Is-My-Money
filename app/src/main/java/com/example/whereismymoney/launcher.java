package com.example.whereismymoney;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class launcher extends AppCompatActivity {

    private ProgressDialog pDialog;

    JSONParser jsonParser = new com.example.whereismymoney.JSONParser();
    String categoryName;
    String categoryColor;
    private static String url_create_category = "http://192.168.64.2/android_connect/create_category.php";
    private static String TAG_SUCCESS = "success";

    Button button;
    public static Database DB = new Database();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        DB.updateCategories();
        DB.updateSpendings();
        Log.d("BUNCAT", String.valueOf(DB.Categories.size()));
        /*if (DB.Categories.size() == 0){
            categoryName = DB.defaultCategoryName;
            categoryColor = String.valueOf(DB.defaultCategoryColor.toArgb());
            DB.MakeCategory(DB.defaultCategoryName, DB.defaultCategoryColor);
            new CreateNewCategory().execute();
        }else {
            dosomething();
        }*/

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if (DB.Categories.size() == 0){
                    categoryName = DB.defaultCategoryName;
                    categoryColor = String.valueOf(DB.defaultCategoryColor.toArgb());
                    DB.MakeCategory(DB.defaultCategoryName, DB.defaultCategoryColor);
                    new CreateNewCategory().execute();
                }else {
                    dosomething();
                }
            }
        });
        Log.d("AUNCAT", String.valueOf(DB.Categories.size()));
    }


    public void dosomething(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    class CreateNewCategory extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = new ProgressDialog(launcher.this);
            pDialog.setMessage("Creating Category...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {


            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("name", categoryName));
            params.add(new BasicNameValuePair("color", categoryColor));

            Log.d("NEWCAT", params.toString());
            Log.d("NEWCAT", url_create_category);
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
