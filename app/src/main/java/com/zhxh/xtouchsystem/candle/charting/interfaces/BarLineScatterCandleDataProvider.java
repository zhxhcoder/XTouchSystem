package com.zhxh.xtouchsystem.candle.charting.interfaces;

import com.zhxh.xtouchsystem.candle.charting.components.YAxis.AxisDependency;
import com.zhxh.xtouchsystem.candle.charting.utils.Transformer;

public interface BarLineScatterCandleDataProvider extends ChartInterface {

    Transformer getTransformer(AxisDependency axis);

    int getMaxVisibleCount();

    boolean isInverted(AxisDependency axis);

    int getLowestVisibleXIndex();

    int getHighestVisibleXIndex();
}
