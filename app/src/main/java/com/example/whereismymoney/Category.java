package com.example.whereismymoney;

import android.graphics.Color;

/**
 * Category object.
 *
 * Each category object has a name and a color.
 *
 * @see AddCategory
 * @author Aiman, Casper, Elain and Leyli
 * @version 1.0
 */

public class Category{
    String name;
    public Color colour;

    /**
     * Method takes a string as a name, and color of a category.
     *
     * @param name This is the name of a category
     * @param colour This is the color of a a category
     */
    Category(String name, Color colour){
        this.name = name;
        this.colour = colour;
    }


    /**
     * Method gets a name of a category object.
     *
     * @return This is the name of a category object
     */
    public String getName(){
        return this.name;
    }
}
