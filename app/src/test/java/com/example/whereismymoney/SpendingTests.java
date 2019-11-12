package com.example.whereismymoney;

import android.graphics.Color;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.Date;

import static org.junit.Assert.*;


import static org.hamcrest.CoreMatchers.*;


public class SpendingTests {


    private static final String wrongCategoryName = "WRONG";
    private static final String categoryName = "Test";
    private static final Color categoryColor = Color.valueOf(Color.GRAY);
    private static final float spendingAmount = 10f;
    private static final Date spendingDate = Date.from(Instant.parse("2007-12-03T10:15:30.00Z"));
    private static final Category spendingCategory = new Category(categoryName, categoryColor);
    private static final Category wrongCategory = new Category(wrongCategoryName, categoryColor);
    private static final Spending ins = new Spending(spendingAmount, spendingDate, spendingCategory);
    public SpendingTests(){
    }


    @Before
    public void setUp(){
        Spending ins = new Spending(spendingAmount, spendingDate, spendingCategory);
    }

    @Test
    public void testGetAmount() {
        float resAmount = ins.getAmount();
        float realAmount = spendingAmount;
        assertEquals(resAmount, realAmount, 0.0002);
    }

    @Test
    public void testGetDate() {
        Date resDate = ins.getDate();
        Date realDate = spendingDate;
        assertEquals(resDate, realDate);
    }

    @Test
    public void testCompareToTrue() {
        int res = ins.compareTo(spendingCategory);
        assertEquals(res, 0);
    }

    @Test
    public void testCompareToFalse() {
        int res = ins.compareTo(wrongCategory);
        assertThat(res, not(equalTo(0)));
    }
}
