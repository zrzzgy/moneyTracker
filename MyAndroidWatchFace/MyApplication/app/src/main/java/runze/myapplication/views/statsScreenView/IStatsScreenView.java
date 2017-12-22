package runze.myapplication.views.statsScreenView;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.PieData;

import java.util.List;

import runze.myapplication.views.IView;


public interface IStatsScreenView extends IView {
    void displayBarChart(BarData barData);
    void displayPieChart(PieData pieData);
    void displayTable(List<String> data);
}
