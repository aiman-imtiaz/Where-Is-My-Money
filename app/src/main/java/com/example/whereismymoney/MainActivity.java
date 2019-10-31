package com.example.whereismymoney;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity {

    public static Database DB = new Database();
    Button makeCategoryOrSpendingBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        makeCategoryOrSpendingBTN = (Button) findViewById(R.id.makeCategoryOrSpendingBTN);
        makeCategoryOrSpendingBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openMakeCategoryOrSpending();
            }
        });
    }

    public void openMakeCategoryOrSpending(){
        Intent intent = new Intent(this, MakeCategoryOrSpending.class);
        startActivity(intent);
    }
}
