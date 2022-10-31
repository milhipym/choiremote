package com.choi.monitoring.chart;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import com.choi.monitoring.R;
import com.choi.monitoring.ChoiMainStageActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class LineChartImple {
    public Activity mActivity;
    public Context mContext;
    public JSONArray mJsonArrayData;
    public LineChart lineChart;
    public TextView graph_two_number, graph_two_number_time;
    public ArrayList<ILineDataSet> linedataAdd;
    public XAxis xAxis;
    public YAxis yAxis;

    public void setInfo(ChoiMainStageActivity choiMainStageActivity, Context applicationContext, JSONArray dataArray) {
        this.mActivity = choiMainStageActivity;
        this.mContext  = applicationContext;
        this.mJsonArrayData = dataArray;
        graph_two_number      = mActivity.findViewById(R.id.graph_two_number);
        graph_two_number_time = mActivity.findViewById(R.id.graph_two_number_time);
        setWholeGraphStyle();
    }
    /*
     * #1. 그래프 전반적인 스타일
     * */
    public void setWholeGraphStyle()
    {
        lineChart = mActivity.findViewById(R.id.graph_two_graph);
        lineChart.getLegend().setEnabled(false); //그래프 하단의 타이틀(라벨) 없애기
        lineChart.setBackgroundColor(Color.parseColor("#FFFFFF"));
        lineChart.getDescription().setEnabled(false); // description 표시하지 않기
        lineChart.setTouchEnabled(true);// 그래프 터치 가능
        lineChart.setDragXEnabled(true);// X축으로 드래그 가능
        lineChart.setDragYEnabled(false);// Y축으로 드래그 불가능
        lineChart.setScaleEnabled(false);// 확대 불가능
        lineChart.setPinchZoom(true);// pinch zoom 가능 (손가락으로 확대축소하는거)
        lineChart.setVisibleXRange(5, 10);// 최대 x좌표 기준으로 몇개를 보여줄지 (최소값, 최대값)
        lineChart.animateX(1000); //X축 애니메이션
        lineChart.animateY(1500); //Y축 애니메이션

        setXYgraphStyle();
    }

    /*
     * #2. X축, Y축 스타일
     * XAxis, YAxis
     * */
    public void setXYgraphStyle()
    {
/*        ArrayList<String> xVals = new ArrayList<String>(); // X 축 이름 값
        xVals.add("10:10");
        xVals.add("10:20");
        xVals.add("10:30");
        xVals.add("10:40");*/
        xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // X축을 그래프 아래로 위치하기
        xAxis.setTextSize(5);// 레이블 텍스트 사이즈
        xAxis.setTextColor(Color.BLACK);// 레이블 텍스트 색
        xAxis.setAxisLineColor(Color.BLACK);// 축 색
        xAxis.setDrawAxisLine(false);// 그래프 뒷 배경의 그리드 표시하지 않기
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int)value)+"시";
            }
        });

        lineChart.getAxisRight().setEnabled(false); // Y축 오른쪽 보이지않게
        lineChart.getAxisLeft().setEnabled(false); // Y축 왼쪽 보이지않게

        setLineEntry();
    }
    /*
     * #3. 데이타 수집 및 스타일
     * BarEntry - X,Y 데이터 BarEntry(x,y) 담기 y값은 float
     * BarDataSet - Bar 데이터 스타일
     * */
    public void setLineEntry()
    {
        ArrayList<Entry> entries = new ArrayList<>();
/*        entries.add(new Entry(10,133));
        entries.add(new Entry(20,150));
        entries.add(new Entry(30,125));
        entries.add(new Entry(40,123));
        entries.add(new Entry(50,97));*/
        String maxCnt  = "";
        String maxTime = "";
        int preValue   = 0 ;
        //Integer[] maxCntArry  = new Integer[mJsonArrayData.length()+1];
        try {
            for (int i=0; i<mJsonArrayData.length(); i++){
                JSONObject jsonObject = mJsonArrayData.getJSONObject(i);
                Log.d("YYYM", "uptTm: "+jsonObject.getString("uptTm"));
                Log.d("YYYM", "uptCnt: "+jsonObject.getString("uptCnt"));
                String date = jsonObject.getString("uptTm");
                String data = jsonObject.getString("uptCnt");

                if (i == 0) {preValue = Integer.parseInt(data);}
                else if (preValue < Integer.parseInt(data))
                {
                    preValue = Integer.parseInt(data);
                    maxCnt   = data;
                    maxTime  = date;
                }

                //maxCntArry[i] = Integer.parseInt(data);
                Float dateF = Float.valueOf(date);
                Float dataF = Float.valueOf(data);
                entries.add(new Entry(dateF, dataF));
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        graph_two_number.setText(maxCnt);
        graph_two_number_time.setText(maxTime+"시");
        //updateUI(maxCnt, maxTime);

        String title = ""; //10분당 완료건수
        LineDataSet lineDataSet = new LineDataSet(entries, title);
        lineDataSet.setColor(Color.parseColor("#9370DB"));
        lineDataSet.setLineWidth(2);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return ""+(int)value;
            }
        });
        //lineDataSet.setDrawValues(false);
        //lineDataSet.setDrawIcons(false);
        linedataAdd = new ArrayList<>();
        linedataAdd.add(lineDataSet);
        //데이터값 스트링 변경

        setSyncDataAndGraph();
    }
    /*
     * #4. 데이터 + 그래프 싱크
     * */
    public void setSyncDataAndGraph()
    {
        LineData lineData = new LineData(linedataAdd);
        lineData.setValueTextSize(5);
        lineChart.setData(lineData); // BarData를 실제 barchart에 전달
        lineChart.invalidate();     // 갱신
    }

}
