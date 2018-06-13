package runze.moneytracker.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.PieData;

import java.util.List;

import runze.moneytracker.HomeActivity;
import runze.moneytracker.R;
import runze.moneytracker.presenters.IPresenter;
import runze.moneytracker.presenters.StatsScreenPresenter;
import runze.moneytracker.utils.MyXAxisValueFormatter;


public class StatsScreenView extends RelativeLayout implements IView {
    private final String TAG = getClass().getSimpleName();
    private StatsScreenPresenter mPresenter;
    private BarChart mBarChart;
    private PieChart mPieChart;
    private ListView mStatsList;
    private ArrayAdapter<String> mAdapter;

    public StatsScreenView(Context context) {
        super(context);
        View v = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.stats_view_layout, this);
        init(v);
        ((HomeActivity) getContext()).registerForContextMenu(mStatsList);
    }

    private void init(View view) {
        mBarChart = findViewById(R.id.barChart);
        mPieChart = findViewById(R.id.pieChart);
        mStatsList = findViewById(R.id.statsList);
    }

    @Override
    public void attachPresenter(IPresenter presenter) {
        mPresenter = (StatsScreenPresenter) presenter;
    }

    @Override
    public void detachPresenter() {
        mPresenter = null;
    }

    public void displayBarChart(BarData barData, String[] dateList) {
        mBarChart.setAutoScaleMinMaxEnabled(false);
        mBarChart.getXAxis().setValueFormatter(new MyXAxisValueFormatter(dateList));
        mBarChart.getXAxis().setLabelRotationAngle(45);
        mBarChart.getXAxis().setDrawLabels(true);
        mBarChart.setData(barData);
        mBarChart.invalidate();
    }

    public void displayPieChart(PieData pieData) {
        mPieChart.setData(pieData);
        mPieChart.invalidate();
    }
}
