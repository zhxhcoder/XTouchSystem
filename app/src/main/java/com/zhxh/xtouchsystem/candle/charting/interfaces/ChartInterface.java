package com.zhxh.xtouchsystem.candle.charting.interfaces;

import android.graphics.PointF;
import android.graphics.RectF;

import com.zhxh.xtouchsystem.candle.charting.utils.ValueFormatter;

/**
 * Interface that provides everything there is to know about the dimensions,
 * bounds, and range of the chart.
 *
 * @author zhxh
 */
public interface ChartInterface {

    float getXChartMin();

    float getXChartMax();

    float getYChartMin();

    float getYChartMax();

    int getXValCount();

    int getWidth();

    int getHeight();

    PointF getCenterOfView();

    PointF getCenterOffsets();

    RectF getContentRect();

    ValueFormatter getDefaultValueFormatter();
}
