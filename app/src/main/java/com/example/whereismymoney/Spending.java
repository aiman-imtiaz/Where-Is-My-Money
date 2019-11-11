package com.example.whereismymoney;


import java.util.Date;

/**
 * Spending object.
 *
 * Each spending object has an amount spent, a date, a category, and a description which is optional.
 *
 * @see AddSpending
 * @author Aiman, Casper, Elain and Leyli
 * @version 1.0
 */
public class Spending implements Comparable<Category>{
    float amount;
    Date date;
    public Category category;
    String description;

    /**
     * Method creates a spending object given an amount, a date, and a category object.
     *
     * @param amt This is the amount spent
     * @param dt This is the date of the spending
     * @param cat This is the category in which spending was made
     */
    Spending(float amt, Date dt, Category cat){
        amount = amt;
        date = dt;
        category = cat;
    }

    /**
     * Method creates a spending object given an amount, a date, a category object and a description.
     *
     * @param amt This is the amount spent
     * @param dt This is the date of the spending
     * @param cat This is the category in which spending was made
     * @param desc This is the description of the spending
     */
    Spending(float amt, Date dt, Category cat, String desc){
        amount = amt;
        date = dt;
        category = cat;
        description = desc;
    }

    /**
     * Method gets the amount of the spending object.
     * @return This is the amount spent
     */
    public float getAmount(){
        return this.amount;
    }

    /**
     * Method gets the date of the spending object.
     * @return This is the date of the spending
     */
    public Date getDate(){
        return this.date;
    }

    /**
     * Method compares two categories names, the one of the Spending's Category and the one of the parameter.
     * @param o This is the category object
     * @return 0 if the names are the same, non-0 if the names are different
     */
    @Override
    public int compareTo(Category o) {
        return this.category.name.compareTo(o.name);
    }

}
