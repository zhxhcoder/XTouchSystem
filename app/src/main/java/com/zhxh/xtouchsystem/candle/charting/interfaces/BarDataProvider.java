package com.zhxh.xtouchsystem.candle.charting.interfaces;

import com.zhxh.xtouchsystem.candle.charting.data.BarData;

public interface BarDataProvider extends BarLineScatterCandleDataProvider {

    public BarData getBarData();

    public boolean isDrawBarShadowEnabled();

    public boolean isDrawValueAboveBarEnabled();

    public boolean isDrawHighlightArrowEnabled();

    public boolean isDrawValuesForWholeStackEnabled();
}
