package com.example.whereismymoney;


import java.util.Date;
import java.util.List;


public class Spending implements Comparable<Category>{
    float amount;
    Date date;
    Category category;
    String description;

    Spending(float amt, Date dt, Category cat){
        amount = amt;
        date = dt;
        AssignCategory(cat);
    }

    Spending(float amt, Date dt, Category cat, String desc){
        amount = amt;
        date = dt;
        AssignCategory(cat);
        description = desc;
    }

    private float getAmount(){
        return this.amount;
    }

    private Date getDate(){
        return this.date;
    }

    private Category getCategory(){
        return this.category;
    }

    public void AssignCategory(Category cat){
        this.category=cat;
    }

    @Override
    public int compareTo(Category o) {
        return this.category.name.compareTo(o.name);
    }

}
