package com.example.whereismymoney;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.ColorInt;
import androidx.annotation.RequiresApi;

import java.util.Date;
import java.util.Map;


public class Database {
    private String defaultCategoryName = "Uncategorized";
    float spendingsLimit;
    float totalSpendings;
    public List<Spending> Spendings = new ArrayList<Spending>();
    public List<Category> Categories = new ArrayList<Category>();
    public Map categoryMap = new Hashtable<String, Category>();



    @RequiresApi(api = Build.VERSION_CODES.O)
    public Database(){
        spendingsLimit = 1;
        Category defaultCat = new Category(defaultCategoryName, Color.valueOf(Color.GRAY));
        Categories.add(defaultCat);
        categoryMap.put(defaultCategoryName, defaultCat);
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
}
