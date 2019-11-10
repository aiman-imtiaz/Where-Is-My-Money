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
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.O)
public class AddSpending extends AppCompatActivity {

    private ProgressDialog pDialog;

    JSONParser jsonParser = new com.example.whereismymoney.JSONParser();
    TextView output;
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

    //ArrayList<HashMap<String, String>> categoriesList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_spending);

        //categoriesList = new ArrayList<HashMap<String, String>>();


        new GetCategories().execute();

        output = (TextView) findViewById(R.id.output);
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

    /*@RequiresApi(api = Build.VERSION_CODES.O)
    public void addSpending(){
        output.setText(spendingAmountInput.getText().toString());
        float spendingAmount = Float.parseFloat(spendingAmountInput.getText().toString());

        Date newDate = Date.from(Instant.now());
        DB.MakeSpending(spendingAmount, newDate, (Category) DB.categoryMap.get(String.valueOf(chooseSpendingCategory.getSelectedItem())));
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    */

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

            Log.d("ADDSPEND", spendingCategory);

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

        /*
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            Log.d("ADDSPENDING", "Params ok");
            JSONObject json = jsonParser.makeHttpRequest(url_all_categories, "GET", params);
            Log.d("ALLPRODUCTS", "Made HTTP request");

            // Check your log cat for JSON reponse

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    categories = json.getJSONArray(TAG_CATEGORIES);

                    // looping through All Products
                    for (int i = 0; i < categories.length(); i++) {
                        JSONObject c = categories.getJSONObject(i);

                        // Storing each json item in variable
                        String id = c.getString(TAG_CID);
                        String name = c.getString(TAG_NAME);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_CID, id);
                        map.put(TAG_NAME, name);

                        // adding HashList to ArrayList
                        categoriesList.add(map);
                        Log.d("DISPLAYCAT", categoriesList.toString());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }




            return null;
        }*/

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

                    //Log.d("ADDSPEND", names[0].toString());

                    ArrayAdapter dataAdapter = new ArrayAdapter(AddSpending.this,
                            android.R.layout.simple_spinner_dropdown_item, names);/*new ArrayAdapter<Object>(this,
                            android.R.layout.simple_spinner_dropdown_item, categoriesList.);*/
                    //dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


                    // SIMPLE ADAPTER VERSION
                    /*SimpleAdapter dataAdapter = new SimpleAdapter(AddSpending.this,
                            DB.categoriesList, R.layout.support_simple_spinner_dropdown_item, new String[]{DB.getTagName(), DB.getTagCid()},
                            new int[]{android.R.id.text1});/*new ArrayAdapter<Object>(this,
                            android.R.layout.simple_spinner_dropdown_item, categoriesList.);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    */



                    /*SimpleAdapter.ViewBinder viewBinder = new SimpleAdapter.ViewBinder() {
                        @Override
                        public boolean setViewValue(View view, Object data, String textRepresentation) {
                            TextView textView = (TextView) view;
                            textView.setText(textRepresentation);
                            return true;
                        }
                    };
                    dataAdapter.setViewBinder(viewBinder);*/
                    chooseSpendingCategory.setAdapter(dataAdapter);

                    /*ListAdapter adapter = new SimpleAdapter(
                            AllProductsActivity.this, productsList,
                            R.layout.list_item, new String[] { TAG_PID,
                            TAG_NAME},
                            new int[] { R.id.pid, R.id.name });
                    // updating listview
                    setListAdapter(adapter);
                }*/
                }
            });

        }




    }

}
