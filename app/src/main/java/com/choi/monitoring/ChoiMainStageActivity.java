package com.choi.monitoring;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.choi.monitoring.callapi.CallAPIServise;
import com.choi.monitoring.chart.BarChartImple;
import com.choi.monitoring.chart.LineChartImple;
import com.choi.monitoring.chart.PieChartImple;
import com.choi.monitoring.chart.PieChartImpleSec;
import com.github.mikephil.charting.charts.BarChart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChoiMainStageActivity extends AppCompatActivity {
    public CallAPIServise callAPIServise;
    public TextView graph_two_number, graph_two_number_time ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_shap_five_main);

        startActivity(new Intent(this, ChoiIntroActivity.class));
        //initSetting();
        initView();
        callApiFirst();
    }

    public void initView() {
        graph_two_number = findViewById(R.id.graph_two_number);
        graph_two_number_time = findViewById(R.id.graph_two_number_time);
    }

    private void callApiFirst() {
        CallAPIServise callAPIServise = new CallAPIServise();
        callAPIServise.apiCreate(getApplicationContext(), this, true);
    }

    public void ResponseGetData(String chartLocation, JSONArray dataArray, JSONArray dataSubArray)
    {
        if (!chartLocation.isEmpty() && !dataArray.isNull(0))
        {
            if (chartLocation.equals("FIRST"))
            {
                BarChartImple barChartImple
                        = new BarChartImple(ChoiMainStageActivity.this, dataArray);
            }
            else if (chartLocation.equals("SECOND"))
            {
                LineChartImple lineChartImple
                        = new LineChartImple();
                lineChartImple.setInfo(this, this.getApplicationContext(), dataArray);
            }
            else if (chartLocation.equals("THIRD"))
            {
                PieChartImple pieChartImple = new PieChartImple();
                pieChartImple.setInfo(this, this.getApplicationContext(), dataArray);

            }
            else if (chartLocation.equals("FOURTH"))
            {
                PieChartImpleSec pieChartImpleSec = new PieChartImpleSec();
                pieChartImpleSec.setInfo(this, this.getApplicationContext(), dataArray, dataSubArray);
            }
//            else if (chartLocation.equals("FIFTH"))
//            {
//                BarChartImple barChartImple
//                        = new BarChartImple(ChoiMainStageActivity.this, dataArray);
//            }
        }

    }

    public void updateUI(String s, String t)
    {
        graph_two_number.setText("7");
        graph_two_number_time.setText("t");
    }


    /*

    public void go2Main(String chartLocation, JSONArray dataArray) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() { go2MainPage(chartLocation, dataArray); }
        },2000);
    }

    private void go2MainPage(String chartLocation, JSONArray dataArray)
    {
        Intent i = new Intent(this, ChoiMainStageActivity.class);
        i.putExtra("chartLocation",chartLocation);
        i.putExtra("dataArray",dataArray.toString());
        startActivity(i);
    }

    private void initSetting()
    {
        try {
            String chartLocation = this.getIntent().getStringExtra("chartLocation");
            String dataArray = this.getIntent().getStringExtra("dataArray");
            Log.e("YYYM", "onCreate:"+chartLocation+" ,dataArray:"+dataArray);
            JSONArray jsonArrayData = new JSONArray(dataArray);
            if (!chartLocation.isEmpty() && !dataArray.isEmpty())
            {
                if (chartLocation.equals("FIRST"))
                {
                    BarChartImple barChartImple
                            = new BarChartImple(ChoiMainStageActivity.this, jsonArrayData);
                }
*/
/*                else if (chartLocation.equals("SECOND"))
                {
                    LineChartImple lineChartImple
                            = new LineChartImple(ChoiMainStageActivity.this, jsonArrayData);
                }
                else if (chartLocation.equals("THIRD"))
                {
                    BarChartImple barChartImple
                            = new BarChartImple(ChoiMainStageActivity.this, jsonArrayData);
                }
                else if (chartLocation.equals("FOURTH"))
                {
                    BarChartImple barChartImple
                            = new BarChartImple(ChoiMainStageActivity.this, jsonArrayData);
                }
                else if (chartLocation.equals("FIFTH"))
                {
                    BarChartImple barChartImple
                            = new BarChartImple(ChoiMainStageActivity.this, jsonArrayData);
                }*//*


            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

    }
*/

}