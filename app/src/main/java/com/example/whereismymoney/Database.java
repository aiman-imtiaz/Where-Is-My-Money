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

/**
 *
 * Database of the application that tracks personal finances.
 *
 * WhereIsMyMoney takes input from users such as monthly spending limit, spendings,
 * newly created categories, and any edits they make; this class contains all methods
 * than handle those inputs.
 *
 * @see AddCategory
 * @see AddSpending
 * @see MakeCategoryOrSpending
 *
 * @author Aiman, Casper, Elaine and Leyli
 * @version 1.0
 *
 */


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

    /**
     * This is a default category if the user chooses not to create any categories
    **/
     private String defaultCategoryName = "Uncategorized";
    float spendingsLimit;
    float totalSpendings;
    /**
     * This is a list of spending objects.
     * Each spending object has a category attached to it.
     */
    public List<Spending> Spendings = new ArrayList<Spending>();
    /**
     * This is a list of category objects.
     * Each category has a color attached to it.
     */
    public List<Category> Categories = new ArrayList<Category>();
    /**
     * This is a map of strings of category names to respective category objects
     */
    public Map categoryMap = new Hashtable<String, Category>();



    @RequiresApi(api = Build.VERSION_CODES.O)
    public Database(){
        spendingsLimit = 1000;
        Category defaultCat = new Category(defaultCategoryName, Color.valueOf(Color.GRAY));
        Categories.add(defaultCat);
        categoryMap.put(defaultCategoryName, defaultCat);

        categoriesList = new ArrayList<HashMap<String, String>>();
        spendingsList = new ArrayList<HashMap<String, String>>();
        /*if (Date.from(Instant.now()).getDay() == 1) {
            totalSpendings = 0f;
        }*/
        new GetCategories().execute();
        new GetSpendings().execute();
    }


    public void updateCategories() {
        System.out.println("Hello, Im in updatecategories!");
        Categories.clear();
        new GetCategories().execute();
    }

    public void updateSpendings() {
        System.out.println("Hello, Im in updatespendings!");
        new GetSpendings().execute();

    }

    /**
     *
     * Method updates the spending category.
     *
     * Given a new category, and a spending that has already been created,
     * this method changes the old category to the new category.
     *
     * @param spnd The spending object that needs to be altered
     * @param cat The category object to which the user wants to add the spending
     */
    private void UpdateSpendingCategory(Spending spnd, Category cat){
        spnd.AssignCategory(cat);
        Spendings.add(spnd);
    }

    /**
     *
     * Method updates the spending amount.
     *
     * Given a new amount, and an existing spending object, this method changes
     * the amount spent.
     *
     * @param spnd The spending object that needs to be changed
     * @param amt A number that will be the new amount in the spending object
     */
    private void UpdateSpendingAmount(Spending spnd, float amt){
        spnd.amount = amt;
        totalSpendings += amt;
    }

    /**
     *
     * Method updates the spending description.
     *
     * Given a spending object, and a description, this method changes the description
     * in the spending object.
     *
     * @param spnd The spending object that needs to be changed
     * @param desc A description that will be added to the spending object
     */
    private void UpdateSpendingDescription(Spending spnd, String desc){
        spnd.description = desc;
    }

    /**
     *
     * Method makes new spending object.
     *
     * Given a float that represents the amount spent, a date, and a category object,
     * this method creates a new spending object.
     *
     * @param amt This is the amount spent
     * @param dt This is the date on which the spending was made
     * @param cat This is the category to which the spending belongs
     */
    public void MakeSpending(float amt, Date dt, Category cat){
        Spending newSpnd = new Spending(amt, dt, cat);
        System.out.println(newSpnd.date.toString());
        Spendings.add(newSpnd);
        totalSpendings += amt;
        Log.d("MKSPD", Spendings.toString());
    }

    /**
     *
     * Method makes new spending object with a description.
     *
     * Given a float that represents the amount spent, a date, a category object,
     * and a description, this method creates a new spending object.
     *
     * @param amt This is the amount spent
     * @param dt This is the date on which the spending was made
     * @param cat This is the category to which the spending belongs
     */
    public void MakeSpending(float amt, Date dt, Category cat, String desc){
        Spending newSpnd = new Spending(amt, dt, cat, desc);
        Spendings.add(newSpnd);
        totalSpendings += amt;
    }

    /**
     *
     * Method makes a new category object.
     *
     * Given a name and a color, this method creates a new category.
     *
     * @param name This is the name of the category
     * @param col This is the color of the category
     */
    public void MakeCategory(String name, Color col){
        // If the category name is already in the Categories List, give an error
        Category category = new Category(name, col);
        Categories.add(category);
        categoryMap.put(name, category);
    }

    /**
     *
     * Method updates the spending limit.
     *
     * Given an amount, this method changes the spending limit.
     *
     * @param amt This is the amount that is the spending limit.
     */
    private void UpdateSpendingsLimit(float amt){
        spendingsLimit = amt;
    }

    /**
     *
     * Method returns spendings in a category.
     *
     * Given a category object, this method returns the list of spending objects
     * that are in the category.
     *
     * @param cat This is the category object from which we want to see all spendings
     * @return
     */
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
            categoriesList.clear();
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
                        Log.d("DBSPDS", MainActivity.DB.Spendings.toString());

                        // adding HashList to ArrayList
                        spendingsList.add(map);
                        Log.d("DISPLAYSPD", spendingsList.toString());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            spendingsList.clear();
            return null;
        }

    }



}
