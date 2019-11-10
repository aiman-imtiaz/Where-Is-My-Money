package com.example.whereismymoney;

import android.graphics.Color;



public class Category{
    String name;
    public Color colour;

    Category(String name, Color colour){
        this.name = name;
        this.colour = colour;
    }


    private String getName(){
        return this.name;
    }
}
