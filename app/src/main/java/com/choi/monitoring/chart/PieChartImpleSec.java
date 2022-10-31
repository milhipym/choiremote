package com.choi.monitoring.chart;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.TextView;

import com.choi.monitoring.ChoiMainStageActivity;
import com.choi.monitoring.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PieChartImpleSec {
    public Activity mActivity;
    public JSONArray mJsonArrayData;
    public JSONArray mJsonArraySubData;
    public PieChart pieChart;
    public ArrayList<Integer> circleColors;
    public PieDataSet pieDataSet;
    public TextView graph_four_lit_number, graph_four_mti_number, graph_four_gni_number;
    public TextView graph_four_lit_title, graph_four_mti_title, graph_four_gni_title;

    public void setInfo(ChoiMainStageActivity activity, Context applicationContext, JSONArray dataArray, JSONArray dataSubArray) {
        this.mActivity = activity;
        this.mJsonArrayData = dataArray;
        this.mJsonArraySubData = dataSubArray;

        graph_four_lit_number = mActivity.findViewById(R.id.graph_four_lit_number);
        graph_four_mti_number = mActivity.findViewById(R.id.graph_four_mti_number);
        graph_four_gni_number = mActivity.findViewById(R.id.graph_four_gni_number);

        graph_four_lit_title = mActivity.findViewById(R.id.graph_four_lit_title);
        graph_four_mti_title = mActivity.findViewById(R.id.graph_four_mti_title);
        graph_four_gni_title = mActivity.findViewById(R.id.graph_four_gni_title);

        setWholeGraphStyle();
    }
    /*
     * #1. 그래프 전반적인 스타일
     * */
    public void setWholeGraphStyle()
    {

        pieChart = mActivity.findViewById(R.id.graph_four_graph);
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
        circleColors.add(Color.parseColor("#AFEEEE")); //하늘
        circleColors.add(Color.parseColor("#FF0000")); //빨강
        circleColors.add(Color.parseColor("#EC407A")); //보라
        circleColors.add(Color.parseColor("#C51162")); //하늘
        circleColors.add(Color.parseColor("#7986CB")); //빨강
        circleColors.add(Color.parseColor("#7E57C2")); //하늘
        circleColors.add(Color.parseColor("#D50000")); //빨강

        setLineEntry();
    }
    /*
     * #3. 데이타 수집 및 스타일
     * BarEntry - X,Y 데이터 BarEntry(x,y) 담기 y값은 float
     * BarDataSet - Bar 데이터 스타일
     * */
    public void setLineEntry()
    {
        String failCnt = "";
        String requestCnt = "0";
        String completeCnt = "0";
        ArrayList<PieEntry> entries = new ArrayList<>();
        try {
            for (int i=0; i<mJsonArrayData.length(); i++){
                JSONObject jsonObject = mJsonArrayData.getJSONObject(i);
                Log.d("YYYM", "contCnt: " +jsonObject.getString("contCnt"));
                Log.d("YYYM", "contNm: "  +jsonObject.getString("contNm"));
                Log.d("YYYM", "contCd: "  +jsonObject.getString("contCd"));
                String dataCnt  = jsonObject.getString("contCnt");
                String dataNM = jsonObject.getString("contNm");
                String dataCd = jsonObject.getString("contCd");

                Float dataCntF  = Float.valueOf(dataCnt);
                entries.add(new PieEntry(dataCntF, dataNM));
            }
            //TOP3 데이터 뽑기
            String top1Cnt, top1Cd, top1Nm = "";
            String top2Cnt, top2Cd, top2Nm = "";
            String top3Cnt, top3Cd, top3Nm = "";
            for (int j=0; j<mJsonArraySubData.length(); j++){
                JSONObject jsonObject = mJsonArraySubData.getJSONObject(j);
                if (j == 0)
                {
                    Log.d("YYYM", "top1Cnt: " + jsonObject.getString("top1Cnt"));
                    Log.d("YYYM", "top1Cd: " + jsonObject.getString("top1Cd"));
                    Log.d("YYYM", "top1Nm: " + jsonObject.getString("top1Nm"));
                    top1Cnt = jsonObject.getString("top1Cnt");
                    top1Cd = jsonObject.getString("top1Cd");
                    top1Nm = jsonObject.getString("top1Nm");
                }
                else if (j == 1)
                {
                    Log.d("YYYM", "top2Cnt: " + jsonObject.getString("top2Cnt"));
                    Log.d("YYYM", "top2Cd: " + jsonObject.getString("top2Cd"));
                    Log.d("YYYM", "top2Nm: " + jsonObject.getString("top2Nm"));

                    top2Cnt = jsonObject.getString("top2Cnt");
                    top2Cd = jsonObject.getString("top2Cd");
                    top2Nm = jsonObject.getString("top2Nm");
                }
                else if (j == 2)
                {
                    Log.d("YYYM", "top3Cnt: " + jsonObject.getString("top3Cnt"));
                    Log.d("YYYM", "top3Cd: " + jsonObject.getString("top3Cd"));
                    Log.d("YYYM", "top3Nm: " + jsonObject.getString("top3Nm"));

                    top3Cnt = jsonObject.getString("top3Cnt");
                    top3Cd = jsonObject.getString("top3Cd");
                    top3Nm = jsonObject.getString("top3Nm");
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

        //setUI
        Log.e("YYYM", "setLineEntry: "+Integer.parseInt(requestCnt));
        Log.e("YYYM", "setLineEntry: "+Integer.parseInt(completeCnt));
        graph_four_lit_title.setText("일반대리");
        graph_four_mti_title.setText("카카오대리");
        graph_four_gni_title.setText("UBI");
        graph_four_lit_number.setText("411");
        graph_four_mti_number.setText("322");
        graph_four_gni_number.setText("233");

        String title = ""; //장자일 건수
        pieDataSet = new PieDataSet(entries, title);
        pieDataSet.setColors(circleColors);
        //pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE); //X축 가이드라인
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);   //Y축 가이드라인
        pieDataSet.setValueTextSize(3); //Y값 사이즈
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
        pieChart.invalidate();
    }

}
