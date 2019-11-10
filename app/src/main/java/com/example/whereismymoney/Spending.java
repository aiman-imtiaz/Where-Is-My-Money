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
    Category category;
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
        AssignCategory(cat);
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
        AssignCategory(cat);
        description = desc;
    }

    /**
     * Method gets the amount of the spending object.
     * @return This is the amount spent
     */
    private float getAmount(){
        return this.amount;
    }

    /**
     * Method gets the date of the spending object.
     * @return This is the date of the spending
     */
    private Date getDate(){
        return this.date;
    }

    /**
     * Method gets the category object that the spending belongs to.
     * @return This is the category object to which the spending belongs
     */
    private Category getCategory(){
        return this.category;
    }

    /**
     * Method assigns a category object to a spending.
     */
    public void AssignCategory(Category cat){
        this.category=cat;
    }

    /**
     * Method compares two categories.
     * @param o This is the category object
     * @return boolean on whether two categories are the same
     */
    @Override
    public int compareTo(Category o) {
        return this.category.name.compareTo(o.name);
    }

}
