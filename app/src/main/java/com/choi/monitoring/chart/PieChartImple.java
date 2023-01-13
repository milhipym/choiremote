package com.choi.monitoring.chart;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import com.choi.monitoring.ChoiMainStageActivity;
import com.choi.monitoring.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PieChartImple {
    public Activity mActivity;
    public JSONArray mJsonArrayData;
    public PieChart pieChart;
    public ArrayList<Integer> circleColors;
    public PieDataSet pieDataSet;
    public TextView graph_three_number, graph_two_three_fail_ecs, graph_two_three_fail_edms;

    public void setInfo(ChoiMainStageActivity activity, Context applicationContext, JSONArray jsonArrayData) {
        this.mActivity = activity;
        this.mJsonArrayData = jsonArrayData;

        graph_three_number       = mActivity.findViewById(R.id.graph_three_number);
        graph_two_three_fail_ecs = mActivity.findViewById(R.id.graph_two_three_fail_ecs);
        graph_two_three_fail_edms= mActivity.findViewById(R.id.graph_two_three_fail_edms);
        setWholeGraphStyle();
    }
    /*
     * #1. 그래프 전반적인 스타일
     * */
    public void setWholeGraphStyle()
    {

        pieChart = mActivity.findViewById(R.id.graph_three_graph);
        pieChart.getLegend().setEnabled(false);
        pieChart.setUsePercentValues(true);
        pieChart.setCenterText("단위 %");
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,1,1,5);

        pieChart.setDragDecelerationFrictionCoef(0.15f);
        pieChart.setDrawHoleEnabled(true); // 원형 가운데 구멍
        pieChart.setHoleColor(Color.WHITE);// 원형 가운데 구멍 색깔
        pieChart.setRotationEnabled(true); // 원형차트 고정여부

        pieChart.setTransparentCircleRadius(50f);// 투명한 원반경 설정
        pieChart.setRotationAngle(300f);

        setXYgraphStyle();
    }
    /*
     * #2. 원형 스타일
     * */
    public void setXYgraphStyle()
    {
        //원형 색깔 셋팅
        circleColors = new ArrayList<>();
        circleColors.add(Color.parseColor("#9370DB")); //보라
        //circleColors.add(Color.parseColor("#AFEEEE")); //하늘
        circleColors.add(Color.parseColor("#FF0000")); //빨강

        setLineEntry();
    }
    /*
     * #3. 데이타 수집 및 스타일
     * BarEntry - X,Y 데이터 BarEntry(x,y) 담기 y값은 float
     * BarDataSet - Bar 데이터 스타일
     * */
    public void setLineEntry()
    {
/*         Map<String, Integer> circleData = new HashMap<>();
       circleData.put("장기" ,60);
        circleData.put("자동차",30);
        circleData.put("일반" ,10);

        ArrayList<PieEntry> entries = new ArrayList<>();
        for (String type : circleData.keySet()){
            Log.d("YYYM", "type: "+type+" , floatValue:"+circleData.get(type).floatValue());
            entries.add(new PieEntry(circleData.get(type).floatValue(), type));
        }*/
        String failCnt = "";
        String requestCnt = "0";
        String completeCnt = "0";
        ArrayList<PieEntry> entries = new ArrayList<>();

        try {
            for (int i=0; i<mJsonArrayData.length(); i++){
                JSONObject jsonObject = mJsonArrayData.getJSONObject(i);
                Log.d("YYYM", "stsCnt: "+jsonObject.getString("stsCnt"));
                Log.d("YYYM", "stsNm: "+jsonObject.getString("stsNm"));
                Log.d("YYYM", "stsCd: "+jsonObject.getString("stsCd"));
                String dataCnt  = jsonObject.getString("stsCnt");
                String dataNM = jsonObject.getString("stsNm");
                String dataCd = jsonObject.getString("stsCd");

                if (dataCd.equals("07")) { completeCnt = dataCnt;}
                else if (dataCd.equals("05")) {requestCnt=dataCnt;}

                Float dataCntF  = Float.valueOf(dataCnt);
                entries.add(new PieEntry(dataCntF, dataNM));
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

        //setUI
        Log.e("YYYM", "setLineEntry: "+Integer.parseInt(requestCnt));
        Log.e("YYYM", "setLineEntry: "+Integer.parseInt(completeCnt));
        int failCntA = Integer.parseInt(requestCnt);
        Log.d("YYYM", "failCntA: "+failCntA);
        int failCntB = Integer.parseInt(completeCnt);
        int failCntInt = failCntA - failCntB;
        Log.d("YYYM", "failCntInt: "+failCntInt);
        failCnt = Integer.toString(Math.abs(failCntInt));
        graph_three_number.setText(failCnt+"건");
        graph_two_three_fail_ecs.setText("완료건 "+Integer.toString(failCntB)+"건");
        graph_two_three_fail_edms.setText("요청건 "+Integer.toString(failCntA)+"건");

        String title = ""; //장자일 건수
        pieDataSet = new PieDataSet(entries, title);
        pieDataSet.setColors(circleColors);
        //pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE); //X축 가이드라인
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);   //Y축 가이드라인
        pieDataSet.setValueTextSize(6); //Y값 사이즈
        //pieDataSet.setValueLinePart1OffsetPercentage(50f); //먼지모르겟음....
        pieDataSet.setValueLinePart1Length(0.3f); // 가이드라인 길이 설정
        pieDataSet.setSliceSpace(3f); //원형 파티션 간격
        pieDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return (int)value+"%";
            }
        });

        setSyncDataAndGraph();
    }

    private void setSyncDataAndGraph() {
        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(true);

        pieChart.setData(pieData);
        pieChart.setEntryLabelTextSize(8);
        pieChart.invalidate();
    }

}
