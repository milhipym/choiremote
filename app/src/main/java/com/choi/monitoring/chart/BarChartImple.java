package com.choi.monitoring.chart;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;

import com.choi.monitoring.R;
import com.choi.monitoring.ChoiMainStageActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BarChartImple extends ChoiMainStageActivity {
    public Activity mActivity;
    public JSONArray mJsonArrayData;
    public BarChart barChart;
    public BarDataSet barDataSet;
    public XAxis xAxis;
    public YAxis yAxis;

    public BarChartImple(Activity activity, JSONArray jsonArrayData) {
        this.mActivity = activity;
        this.mJsonArrayData = jsonArrayData;
        setWholeGraphStyle();
    }
    /*
    * #1. 그래프 전반적인 스타일
    * */
    public void setWholeGraphStyle()
    {
        barChart = mActivity.findViewById(R.id.graph_one_ly);
        barChart.setBackgroundColor(Color.parseColor("#F2E6E6FA"));
        barChart.getDescription().setEnabled(false); // description 표시하지 않기
        barChart.setTouchEnabled(true);// 그래프 터치 가능
        barChart.setDragXEnabled(true);// X축으로 드래그 가능
        barChart.setDragYEnabled(false);// Y축으로 드래그 불가능
        barChart.setScaleEnabled(false);// 확대 불가능
        barChart.setPinchZoom(true);// pinch zoom 가능 (손가락으로 확대축소하는거)
        barChart.setVisibleXRange(5, 5);// 최대 x좌표 기준으로 몇개를 보여줄지 (최소값, 최대값)
        barChart.animateX(1000); //X축 애니메이션
        barChart.animateY(1500); //Y축 애니메이션
        //barChart.moveViewToX(dataSet.getEntryCount());// 가장 최근에 추가한 데이터의 위치로 이동처리

        setXYgraphStyle();
    }

    /*
    * #2. X축, Y축 스타일
    * XAxis, YAxis
    * */
    public void setXYgraphStyle()
    {
        xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // X축을 그래프 아래로 위치하기
        xAxis.setTextSize(10);// 레이블 텍스트 사이즈
        xAxis.setTextColor(Color.BLACK);// 레이블 텍스트 색
        xAxis.setAxisLineColor(Color.BLACK);// 축 색
        xAxis.setDrawAxisLine(false);// 그래프 뒷 배경의 그리드 표시하지 않기
        xAxis.setDrawGridLines(false);

                barChart.getAxisRight().setEnabled(false); // Y축 오른쪽 보이지않게
                barChart.getAxisLeft().setEnabled(false);  // X축 오른쪽 보이지않게
        yAxis = barChart.getAxisLeft(); // Y축 왼쪽셋팅
        xAxis.setTextSize(10);
        yAxis.setTextColor(Color.BLACK);
        yAxis.setAxisLineColor(Color.BLACK);
        yAxis.setDrawAxisLine(false);
        yAxis.setDrawGridLines(false);
        //yAxis.setAxisMaximum(1f);
        //yAxis.setAxisMinimum(0f);


        setBarEntry();
    }
    /*
    * #3. 데이타 수집 및 스타일
    * BarEntry - X,Y 데이터 BarEntry(x,y) 담기 y값은 float
    * BarDataSet - Bar 데이터 스타일
    * */
    public void setBarEntry()
    {
        Log.e("YYYM", "setBarEntry: "+mJsonArrayData);
        ArrayList<BarEntry> entries = new ArrayList<>();
/*        entries.add(new BarEntry(24,1726));
        entries.add(new BarEntry(25,1236));
        entries.add(new BarEntry(26,14083));
        entries.add(new BarEntry(27,15670));
        entries.add(new BarEntry(28,17582));
        entries.add(new BarEntry(29,21037));
        entries.add(new BarEntry(30,6623));*/
        try {
            for (int i=0; i<mJsonArrayData.length(); i++){
                JSONObject jsonObject = mJsonArrayData.getJSONObject(i);
                Log.d("YYYM", "uptCnt: "+jsonObject.getString("uptCnt"));
                Log.d("YYYM", "uptDt: "+jsonObject.getString("uptDt"));
                String date = jsonObject.getString("uptDt");
                String data = jsonObject.getString("uptCnt");
                Float dateF = Float.valueOf(date.substring(6,8));
                Float dataF = Float.valueOf(data);
                entries.add(new BarEntry(dateF, dataF));
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }


        String title = "본인인증 완료건수";
        barDataSet = new BarDataSet(entries, title);
        barDataSet.setColor(Color.parseColor("#9370DB"));
        barDataSet.setDrawIcons(false);
        barDataSet.setDrawValues(true);
        //데이터값 스트링 변경

        setSyncDataAndGraph();
    }
    /*
    * #4. 데이터 + 그래프 싱크
    * */
    public void setSyncDataAndGraph()
    {
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.5f);
        barData.setValueTextSize(5);
        barChart.setData(barData); // BarData를 실제 barchart에 전달
        barChart.invalidate();     // 갱신
    }
}
