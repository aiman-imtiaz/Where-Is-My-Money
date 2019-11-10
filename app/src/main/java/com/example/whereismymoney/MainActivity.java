package com.example.whereismymoney;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity {

    public static Database DB = launcher.DB;
    FloatingActionButton makeCategoryOrSpendingBTN;
    ProgressBar spendingGoalProgress;
    TextView progressToSpendingLimitTXT;
    ListView displaySpendings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displaySpendings = (ListView) findViewById(R.id.displaySpendings);
        spendingGoalProgress = (ProgressBar) findViewById(R.id.spendingGoalProgress);
        progressToSpendingLimitTXT = (TextView) findViewById(R.id.progressToSpendingLimitTXT);
        setupProgressBar();
        setupPieChart();
        makeCategoryOrSpendingBTN = (FloatingActionButton) findViewById(R.id.makeCategoryOrSpendingBTN);
        Log.d("MAINACT", DB.Spendings.toString());
        makeCategoryOrSpendingBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openMakeCategoryOrSpending();
            }
        });

        runOnUiThread(new Runnable() {
            public void run() {
                /**
                 * Updating spendings data into ListView
                 * */
                String[] amounts = new String[DB.Spendings.size()];
                for (int i = 0; i<DB.Spendings.size(); i++){
                    String pattern = "yyyy/MM/dd";
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                    String date = simpleDateFormat.format((DB.Spendings.get(i).date));
                    amounts[i] = "$" + DB.Spendings.get(i).amount + " spent on " + date;
                }

                // Reverse the order so that the topmost is the most recent spending
                for(int i=0; i<amounts.length/2; i++){
                    String temp = amounts[i];
                    amounts[i] = amounts[amounts.length -i -1];
                    amounts[amounts.length -i -1] = temp;
                }

                // Set ListView 
                ArrayAdapter dataAdapter = new ArrayAdapter(MainActivity.this,
                        android.R.layout.simple_list_item_1, amounts);
                displaySpendings.setAdapter(dataAdapter);

            }
        });
    }


    private void setupProgressBar() {
        float spendingsLimit = DB.spendingsLimit;
        float totalSpendings = DB.totalSpendings;
        float percentSpent = totalSpendings/spendingsLimit;
        //TODO: Round the percentages so we don't get trailing decimals
        progressToSpendingLimitTXT.setText("Current Progress To SpendingsLimit: " + DB.totalSpendings + "/" + DB.spendingsLimit
        + "\nWhich is " + (percentSpent*100) + "% of your limit");
        spendingGoalProgress.setProgress((int)(percentSpent*100));
    }

    private void setupPieChart() {
        //populating a list of pie entries
        List<PieEntry> pieEntries = new ArrayList<>();
        for (int i = 0; i < DB.Categories.size(); i++){
            Category category = DB.Categories.get(i);
            pieEntries.add(new PieEntry(DB.SumOfSpendingsInCategory(category), category.name));
        }

        PieDataSet dataSet = new PieDataSet(pieEntries, "Spendings in Categories");
        dataSet.setColors(DB.getCategoryColors());
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(25);
        PieData data = new PieData(dataSet);


        // Get the chart
        PieChart chart = (PieChart) findViewById(R.id.pieChart);
        chart.setData(data);
        chart.animateY(1000);
        //TODO: Figure out if we want percentage values or not
        chart.setUsePercentValues(true);
        chart.setHoleRadius(0f);
        chart.setTransparentCircleRadius(0f);
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.invalidate();
    }

    public void openMakeCategoryOrSpending(){
        Intent intent = new Intent(this, MakeCategoryOrSpending.class);
        startActivity(intent);
    }



}
