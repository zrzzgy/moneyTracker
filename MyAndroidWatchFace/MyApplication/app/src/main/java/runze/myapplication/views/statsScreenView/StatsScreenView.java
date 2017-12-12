package runze.myapplication.views.statsScreenView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.PieData;

import runze.myapplication.R;
import runze.myapplication.presenters.IPresenter;
import runze.myapplication.presenters.statsScreenPresenter.IStatsScreenPresenter;


public class StatsScreenView extends RelativeLayout implements IStatsScreenView {
    private final String TAG = getClass().getSimpleName();
    private IStatsScreenPresenter mPresenter;
    private BarChart mBarChart;
    private PieChart mPieChart;

    public StatsScreenView(Context  context){
        super(context);
        View v = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.stats_view_layout, this);
        init(v);
    }

    private void init(View view){
        mBarChart = findViewById(R.id.barChart);
        mPieChart = findViewById(R.id.pieChart);
    }

    @Override
    public void attachPresenter(IPresenter presenter) {
        mPresenter = (IStatsScreenPresenter) presenter;
    }

    @Override
    public void detachPresenter() {
        mPresenter = null;
    }

    @Override
    public void drawCharts(BarData barData, PieData pieData){
        mBarChart.setData(barData);
        mBarChart.setFitBars(true);
        mBarChart.invalidate();
        mPieChart.setData(pieData);
        mPieChart.invalidate();
    }
}
