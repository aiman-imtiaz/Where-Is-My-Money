package com.example.whereismymoney;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class AddSpending extends AppCompatActivity {

    private ProgressDialog pDialog;

    JSONParser jsonParser = new com.example.whereismymoney.JSONParser();
    Button addSpendingBTN;
    EditText spendingAmountInput;
    Spinner chooseSpendingCategory;
    Database DB = MainActivity.DB;

    private static String url_all_categories = "http://192.168.64.2/android_connect/get_all_categories.php";
    private static String url_create_spending = "http://192.168.64.2/android_connect/create_spending.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_CATEGORIES = "categories";
    private static final String TAG_CID = "cid";
    private static final String TAG_NAME = "name";
    String spendingAmount;
    String spendingCategory;

    JSONArray categories = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_spending);


        new GetCategories().execute();

        spendingAmountInput = (EditText) findViewById(R.id.spendingAmountInput);
        chooseSpendingCategory = (Spinner) findViewById(R.id.chooseSpendingCategory);
        ArrayAdapter<Object> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,  DB.categoryMap.keySet().toArray());
        chooseSpendingCategory.setAdapter(dataAdapter);
        addSpendingBTN = (Button) findViewById(R.id.addSpendingBTN);
        addSpendingBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spendingAmount = spendingAmountInput.getText().toString();
                spendingCategory = String.valueOf(chooseSpendingCategory.getSelectedItem());
                new CreateNewSpending().execute();
                DB.updateSpendings();
            }
        });
    }

    class CreateNewSpending extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = new ProgressDialog(AddSpending.this);
            pDialog.setMessage("Creating Spending...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {


            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("amount", spendingAmount));
            params.add(new BasicNameValuePair("category", spendingCategory));

            JSONObject json = jsonParser.makeHttpRequest(url_create_spending, "POST", params);

            if (json == null){
                Log.d("NEWSPEND", "JSON is null");
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

    class GetCategories extends AsyncTask<String, String, String> {


        protected String doInBackground(String... args) {
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    String[] names = new String[DB.Categories.size()];
                    for (int i = 0; i<DB.Categories.size(); i++){
                        names[i] = DB.Categories.get(i).name;
                    }


                    ArrayAdapter dataAdapter = new ArrayAdapter(AddSpending.this,
                            android.R.layout.simple_spinner_dropdown_item, names);
                    chooseSpendingCategory.setAdapter(dataAdapter);

                }
            });

        }




    }

}
