package com.zhxh.xtouchsystem.candle.charting.interfaces;

import com.zhxh.xtouchsystem.candle.charting.data.CandleData;

public interface CandleDataProvider extends BarLineScatterCandleDataProvider {

    CandleData getCandleData();
}
