package com.choi.monitoring.chart;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.choi.monitoring.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.Utils;

import okhttp3.internal.Util;

public class MyMarkerView extends MarkerView {

    TextView title_pop, cnt_pop;

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);

        title_pop = findViewById(R.id.title_pop);
        cnt_pop = findViewById(R.id.cnt_pop);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        Log.d("YYYM", "refreshContent: "+e);
        if (e instanceof PieEntry) {
            PieEntry pi = (PieEntry) e;
            Log.e("YYYM", "refreshContent: "+pi.getLabel()+","+pi.getValue()+","+pi.getData());
            title_pop.setText(pi.getLabel());
            cnt_pop.setText(Utils.formatNumber(pi.getValue(),0,true)+" 건");
        }

        super.refreshContent(e, highlight);
    }
}
