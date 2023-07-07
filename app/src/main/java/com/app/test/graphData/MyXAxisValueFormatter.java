package com.app.test.graphData;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;

public class MyXAxisValueFormatter extends ValueFormatter implements IAxisValueFormatter {
    private final DecimalFormat mFormat;

    public MyXAxisValueFormatter() {
        mFormat = new DecimalFormat("###,###,###,##0.0");
    }



    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mFormat.format(value)+"";
    }
}
