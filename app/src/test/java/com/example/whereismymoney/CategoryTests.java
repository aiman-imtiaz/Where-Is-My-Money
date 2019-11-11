package com.example.whereismymoney;

import android.graphics.Color;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CategoryTests {

    private static final String categoryName = "Test";
    private static final Color categoryColor = Color.valueOf(Color.GRAY);
    private static final Category ins = new Category(categoryName, categoryColor);


    public CategoryTests(){
    }

    @Before
    public void setUp(){
        Category ins = new Category(categoryName, categoryColor);
    }

    @Test
    public void testGetName() {
        String resName = ins.getName();
        String realName = categoryName;
        assertEquals(resName, realName);
    }
}
