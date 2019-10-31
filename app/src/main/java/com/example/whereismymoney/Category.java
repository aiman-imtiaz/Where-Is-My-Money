package com.example.whereismymoney;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
