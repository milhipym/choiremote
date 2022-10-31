package com.choi.monitoring.chart;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.TextView;

import com.choi.monitoring.ChoiMainStageActivity;
import com.choi.monitoring.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
        circleColors.add(Color.parseColor("#CC00CC")); //빨강

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
            sortJsonArrayData(mJsonArrayData);

        }catch (JSONException e) {
            e.printStackTrace();
        }

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

    private void sortJsonArrayData(JSONArray mJsonArrayData) {
        JSONArray sortedJsonArray = new JSONArray();
        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
        try {
            for (int i = 0; i < mJsonArrayData.length(); i++) {                   //동적 배열 size(), 정적 배열 length()
                jsonValues.add(mJsonArrayData.getJSONObject(i));
                System.out.println(i+""+mJsonArrayData.getJSONObject(i));
            }
            Collections.sort(jsonValues, new Comparator<JSONObject>() {
                private static final String KEY_NUM = "contCnt";             //JSON key 변수 선언 생성
                @Override
                public int compare(JSONObject b, JSONObject a) {
                    //int valA = 0;
                    //int valB = 0;
                    //값이 문자열 일 때
                    String valA = "";
                    String valB = "";
                    try {
                        //valA = Integer.parseInt(a.getString(KEY_NUM));
                        //valB = Integer.parseInt(b.getString(KEY_NUM));

                        //값이 문자열 일 때
                        valA = (String) a.get(KEY_NUM);
                        valB = (String) b.get(KEY_NUM);
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //return Integer.compare(valA,valB);
                    //값이 문자열 일 때
                    return valA.compareTo(valB);
                }
            });
            for (int i = 0; i < mJsonArrayData.length(); i++) {
                sortedJsonArray.put(jsonValues.get(i));
            }

            //setUI
            mJsonArrayData.getJSONObject(0).getString("contNm");
            graph_four_lit_title.setText(mJsonArrayData.getJSONObject(0).getString("contNm").substring(0,4));
            graph_four_mti_title.setText(mJsonArrayData.getJSONObject(1).getString("contNm").substring(0,4));
            graph_four_gni_title.setText(mJsonArrayData.getJSONObject(2).getString("contNm").substring(0,3));
            graph_four_lit_number.setText(mJsonArrayData.getJSONObject(0).getString("contCnt"));
            graph_four_mti_number.setText(mJsonArrayData.getJSONObject(1).getString("contCnt"));
            graph_four_gni_number.setText(mJsonArrayData.getJSONObject(2).getString("contCnt"));
        }catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setSyncDataAndGraph() {
        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(true);

        pieChart.setData(pieData);
        pieChart.setEntryLabelTextSize(5);
        pieChart.setEntryLabelColor(Color.parseColor("#330000"));

        MarkerView markerView = new MyMarkerView(mActivity, R.layout.markview);
        markerView.setChartView(pieChart);
        pieChart.setMarker(markerView);

        pieChart.invalidate();
    }

}
