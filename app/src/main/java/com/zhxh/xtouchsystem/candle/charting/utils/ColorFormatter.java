package com.zhxh.xtouchsystem.candle.charting.utils;

import com.zhxh.xtouchsystem.candle.charting.data.Entry;

/**
 * Interface that can be used to return a customized color instead of setting
 * colors via the setColor(...) method of the DataSet.
 *
 * @author zhxh
 */
public interface ColorFormatter {

    public int getColor(Entry e, int index);
}