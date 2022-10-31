package com.choi.monitoring.callapi;

import android.content.Context;
import android.util.Log;

import com.choi.monitoring.ChoiIntroActivity;
import com.choi.monitoring.ChoiMainStageActivity;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CallAPIServise {
    public Context  mContext;
    public Retrofit retrofit;
    public ChoiMainStageActivity mActivity;

    static String HOST = "http://10.88.22.88:8300";
    static String URL  = "/mobile/monitoring/";
    static String FULLURL = HOST+URL;
    static String SERVICENAME = "cusAgree";
    static int networkLimitTime = 600;

    static String FIRST  = "getAllSmsSuccCnt.do";
    static String SECOND = "getTimeSmsSuccCnt.do";
    static String THIRD  = "getKakaoSmsCnt.do";
    static String FOURTH = "getSmsSuccTopFiveCnt.do";
    static String FIFTH  = "getDriverStatus.do";


    public void apiCreate(Context context, ChoiMainStageActivity activity, Boolean firstRequest) {
        this.mContext  = context;
        this.mActivity = activity;

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(networkLimitTime, TimeUnit.SECONDS)
                .readTimeout(networkLimitTime, TimeUnit.SECONDS)
                .writeTimeout(networkLimitTime, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(FULLURL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        if (firstRequest) firstGraph(FIRST);

        //mActivity.go2Main();
    }

    private void firstGraph(String method) {
        //method = "getDriverStatus.do";
        callApi(retrofit, method);
    }

    public void callApi(Retrofit retrofit, String method)
    {
        Log.e("YYYM", "callApi: " + retrofit +" , method:" +method);
        CHOICAllApiInterface api = retrofit.create(CHOICAllApiInterface.class);
        api.cusAgree(method).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("YYYM", "onResponse1: "+response.toString());
                Log.d("YYYM", "onResponse2: "+response.body());
                Log.d("YYYM", "onResponse3: "+response.raw());
                ResponseBody rd = response.body();
                getDataFromJson(rd);
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("YYYM", "onFailure: ", t);
            }
        });
    }

    public void getDataFromJson(ResponseBody rd)
    {
        String param_1 = "";
        String param_2 = "";
        JSONArray dataListArray = new JSONArray();
        JSONArray dataSubArray = new JSONArray();
        try {
            String reponseData = rd.string();
            JSONObject jsonObject_ = new JSONObject(reponseData);
            //#1. 결과result...
            JSONObject jsonObjectRe = jsonObject_.getJSONObject("result");
            Log.d("YYYM", "resultNm: "+jsonObjectRe.getString("resultNm"));
            Log.d("YYYM", "resultNm: "+jsonObjectRe.getString("resultCd"));

            //#2. 데이타추출
            JSONObject resultObjData = jsonObject_.getJSONObject("data");
            String chartLocation = resultObjData.getString("chartLocation");
            Log.d("YYYM", "resultObjDataLocation: " + chartLocation);

            //if (chartLocation.equals("FOURTH")) {
            //}else {
                dataListArray = resultObjData.getJSONArray("dataList");
            //}

            //#3.추출키 셋 및 다음 쿼리진행
            if (chartLocation.equals("FIRST"))
            { param_1 = "uptCnt"; param_2="uptDt"; callApi(retrofit, SECOND);
                mActivity.ResponseGetData(chartLocation, dataListArray, dataSubArray);}
            else if (chartLocation.equals("SECOND"))
            { param_1 = "uptTM"; param_2="uptCnt"; callApi(retrofit, THIRD);
                mActivity.ResponseGetData(chartLocation, dataListArray, dataSubArray);}
            else if (chartLocation.equals("THIRD"))
            { param_1 = "strCnt"; param_2="strNm"; callApi(retrofit, FOURTH);
                mActivity.ResponseGetData(chartLocation, dataListArray, dataSubArray);}
            else if (chartLocation.equals("FOURTH"))
            { param_1 = "countCnt"; param_2="contNm"; callApi(retrofit, FIFTH);
                dataSubArray = resultObjData.getJSONArray("top3Data");
            mActivity.ResponseGetData(chartLocation, dataListArray, dataSubArray);}
/*            else if (chartLocation.equals("FIFTH"))
            { param_1 = "drvCnt"; param_2="drvNm"; mMainActivity.ResponseGetData(chartLocation, dataArray);}*/

/*            for (int i=0; i<dataListArray.length()-1; i++){
                JSONObject jsonObject = dataListArray.getJSONObject(i);
                Log.d("YYYM", "param_1: "+jsonObject.getString(param_1));
                Log.d("YYYM", "param_2: "+jsonObject.getString(param_2));
            }*/

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

}
