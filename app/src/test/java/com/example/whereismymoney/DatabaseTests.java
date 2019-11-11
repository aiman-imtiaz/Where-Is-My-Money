package com.example.whereismymoney;

import android.graphics.Color;

import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.Date;
import static org.junit.Assert.*;

public class DatabaseTests {

    private static final Database ins = new Database();

    public DatabaseTests(){
    }

    @Before
    public void setUp(){
        Database ins = new Database();
    }


}
