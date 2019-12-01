package com.ytempest.chartrepository.chart1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ytempest.chart.SimpleBarGraph;
import com.ytempest.chartrepository.R;

public class Chart1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart1);

        SimpleBarGraph graph = findViewById(R.id.simpleBarGraph);
        graph.setAdapter(new GraphAdapter());
    }
}
