package com.example.whereismymoney;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

@RequiresApi(api = Build.VERSION_CODES.O)
public class launcher extends AppCompatActivity {
    Button button;
    public static Database DB = new Database();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        DB.updateCategories();
        DB.updateSpendings();
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                dosomething();
            }
        });

    }

    public void dosomething(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
