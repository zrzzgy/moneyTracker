package runze.myapplication.views.statsScreenView;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.PieData;

import runze.myapplication.views.IView;


public interface IStatsScreenView extends IView {
    void drawCharts(BarData barData, PieData pieData);
}
