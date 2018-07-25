package com.zhxh.xtouchsystem.candle.charting.utils;

import com.zhxh.xtouchsystem.candle.charting.data.LineData;
import com.zhxh.xtouchsystem.candle.charting.data.LineDataSet;

/**
 * Interface for providing a custom logic to where the filling line of a DataSet
 * should end. If setFillEnabled(...) is set to true.
 *
 * @author zhxh
 */
public interface FillFormatter {

    /**
     * Returns the vertical (y-axis) position where the filled-line of the
     * DataSet should end.
     *
     * @param dataSet
     * @param data
     * @param chartMaxY
     * @param chartMinY
     * @return
     */
    public float getFillLinePosition(LineDataSet dataSet, LineData data, float chartMaxY,
                                     float chartMinY);
}
