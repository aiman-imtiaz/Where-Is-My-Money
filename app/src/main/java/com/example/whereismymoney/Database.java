package com.example.whereismymoney;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.RequiresApi;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Map;


public class Database {

    private ProgressDialog pDialog;

    JSONParser jsonParser = new com.example.whereismymoney.JSONParser();
    TextView output;
    Button addSpendingBTN;
    EditText spendingAmountInput;
    Spinner chooseSpendingCategory;

    private static String url_all_categories = "http://192.168.64.2/android_connect/get_all_categories.php";
    private static String url_all_spendings = "http://192.168.64.2/android_connect/get_all_spendings.php";


    public static String getTagSuccess() {
        return TAG_SUCCESS;
    }

    public static String getTagCategories() {
        return TAG_CATEGORIES;
    }

    public static String getTagCid() {
        return TAG_CID;
    }

    public static String getTagName() {
        return TAG_NAME;
    }

    public static String getTagColor() {
        return TAG_COLOR;
    }

    private static final String TAG_SUCCESS = "success";

    // JSON Node names: CATEGORIES
    private static final String TAG_CATEGORIES = "categories";
    private static final String TAG_CID = "cid";
    private static final String TAG_NAME = "name";
    private static final String TAG_COLOR = "color";


    // JSON Node names: SPENDINGS
    private static final String TAG_SPENDINGS = "spendings";
    private static final String TAG_SID = "sid";
    private static final String TAG_AMOUNT = "amount";

    public static String getTagSpendings() {
        return TAG_SPENDINGS;
    }

    public static String getTagSid() {
        return TAG_SID;
    }

    public static String getTagAmount() {
        return TAG_AMOUNT;
    }

    public static String getTagCategory() {
        return TAG_CATEGORY;
    }

    private static final String TAG_CATEGORY = "category";

    JSONArray categories = null;
    JSONArray spendings = null;

    ArrayList<HashMap<String, String>> categoriesList;
    ArrayList<HashMap<String, String>> spendingsList;

    private String defaultCategoryName = "Uncategorized";
    float spendingsLimit;
    float totalSpendings;
    public List<Spending> Spendings = new ArrayList<Spending>();
    public List<Category> Categories = new ArrayList<Category>();
    public Map categoryMap = new Hashtable<String, Category>();



    @RequiresApi(api = Build.VERSION_CODES.O)
    public Database(){
        Category defaultCat = new Category(defaultCategoryName, Color.valueOf(Color.GRAY));
        Categories.add(defaultCat);
        categoryMap.put(defaultCategoryName, defaultCat);

        categoriesList = new ArrayList<HashMap<String, String>>();
        spendingsList = new ArrayList<HashMap<String, String>>();
        new GetCategories().execute();
        new GetSpendings().execute();
    }

    public void updateCategories() {
        new GetCategories().execute();
    }

    public void updateSpendings() {
        new GetSpendings().execute();
    }

    private void UpdateSpendingCategory(Spending spnd, Category cat){
        spnd.AssignCategory(cat);
        Spendings.add(spnd);
    }

    private void UpdateSpendingAmount(Spending spnd, float amt){
        spnd.amount = amt;
        totalSpendings += amt;
    }

    private void UpdateSpendingDescription(Spending spnd, String desc){
        spnd.description = desc;
    }

    public void MakeSpending(float amt, Date dt, Category cat){
        Spending newSpnd = new Spending(amt, dt, cat);
        Spendings.add(newSpnd);
        totalSpendings += amt;
    }

    public void MakeSpending(float amt, Date dt, Category cat, String desc){
        Spending newSpnd = new Spending(amt, dt, cat, desc);
        Spendings.add(newSpnd);
        totalSpendings += amt;
    }

    public void MakeCategory(String name, Color col){
        // If the category name is already in the Categories List, give an error
        Category category = new Category(name, col);
        Categories.add(category);
        categoryMap.put(name, category);
    }

    private void UpdateSpendingsLimit(float amt){
        spendingsLimit = amt;
    }

    private List<Spending> ReturnSpendingsInCategory(Category cat){
        List<Spending> returnList = new ArrayList<Spending>();
        for (Spending spd : Spendings){
            if (spd.compareTo(cat) == 0){
                returnList.add(spd);
            }
        }
        return returnList;
    }

    public float SumOfSpendingsInCategory(Category cat){
        float retval = 0;
        for(Spending spd : Spendings){
            if (spd.compareTo(cat) == 0){
                retval += spd.amount;
            }
        }
        return retval;
    }

    public float[] getSpendingProportions(){
        float[] proportions = new float[Categories.size()];
        List<Spending> spdList;
        float spendingAmount = 0f;
        for (Category cat : Categories){
            spdList = ReturnSpendingsInCategory(cat);
            for (Spending spd : spdList){
                spendingAmount += spd.amount;
            }
            spendingAmount /= totalSpendings;
            proportions[Categories.indexOf(cat)] = spendingAmount;
            spendingAmount = 0f;
        }

        return proportions;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int[] getCategoryColors(){
        int[] retArr = new int[Categories.size()];
        for (int i = 0; i < Categories.size(); i++){
            retArr[i] = Categories.get(i).colour.toArgb();
        }

        return retArr;
    }

    void deleteSpending(Spending spnd){
        totalSpendings -= spnd.amount;
        Spendings.remove(spnd);
    }

    void deleteCategory(Category cat){
        if((cat.name.compareTo(defaultCategoryName)!=0)){
            for (Spending spnd : Spendings){
                if (spnd.compareTo(cat) == 0){
                    UpdateSpendingCategory(spnd, Categories.get(0));
                }
            }
            Categories.remove(cat);
        }
    }

    class GetCategories extends AsyncTask<String, String, String> {


        /**
         * getting All products from url
         * */
        @RequiresApi(api = Build.VERSION_CODES.O)
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            Log.d("ADDSPENDING", "Params ok");
            JSONObject json = jsonParser.makeHttpRequest(url_all_categories, "GET", params);
            Log.d("ALLPRODUCTS", "Made HTTP request");

            // Check your log cat for JSON reponse

            //// MMMMMAAAAAAAAP
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
                        String color = c.getString(TAG_COLOR);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_CID, id);
                        map.put(TAG_NAME, name);
                        map.put(TAG_COLOR, color);

                        MakeCategory(name, Color.valueOf(Integer.parseInt(color)));
                        Log.d("DBCATS", Categories.toString());

                        // adding HashList to ArrayList
                        categoriesList.add(map);
                        Log.d("DISPLAYCAT", categoriesList.toString());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

    }

    class GetSpendings extends AsyncTask<String, String, String> {

        /**
         * getting All products from url
         * */
        @RequiresApi(api = Build.VERSION_CODES.O)
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jsonParser.makeHttpRequest(url_all_spendings, "GET", params);

            // Check your log cat for JSON reponse

            //// MMMMMAAAAAAAAP
            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    spendings = json.getJSONArray(TAG_SPENDINGS);

                    // looping through All Products
                    for (int i = 0; i < spendings.length(); i++) {
                        JSONObject s = spendings.getJSONObject(i);

                        // Storing each json item in variable
                        String id = s.getString(TAG_SID);
                        String amount = s.getString(TAG_AMOUNT);
                        String category = s.getString(TAG_CATEGORY);
                        String date = s.getString("created_at");

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_SID, id);
                        map.put(TAG_AMOUNT, amount);
                        map.put(TAG_CATEGORY, category);
                        map.put("created_at", date);


                        //Date newdate = new Date(date);
                        Date newDate = Date.from(Instant.now());
                        MakeSpending(Float.parseFloat(amount), newDate, (Category) categoryMap.get(category));
                        Log.d("DBSPDS", Spendings.toString());

                        // adding HashList to ArrayList
                        spendingsList.add(map);
                        Log.d("DISPLAYSPD", spendingsList.toString());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

    }



}
