package runze.myapplication.views;

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

import runze.myapplication.HomeActivity;
import runze.myapplication.R;
import runze.myapplication.presenters.IPresenter;
import runze.myapplication.presenters.statsScreenPresenter.IStatsScreenPresenter;
import runze.myapplication.utils.MyXAxisValueFormatter;


public class StatsScreenView extends RelativeLayout {
    private final String TAG = getClass().getSimpleName();
    private IStatsScreenPresenter mPresenter;
    private BarChart mBarChart;
    private PieChart mPieChart;
    private ListView mStatsList;
    private ArrayAdapter<String> mAdapter;

    public StatsScreenView(Context  context){
        super(context);
        View v = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.stats_view_layout, this);
        init(v);
        ((HomeActivity) getContext()).registerForContextMenu(mStatsList);
    }

    private void init(View view){
        mBarChart = findViewById(R.id.barChart);
        mPieChart = findViewById(R.id.pieChart);
        mStatsList = findViewById(R.id.statsList);
    }

    public void attachPresenter(IPresenter presenter) {
        mPresenter = (IStatsScreenPresenter) presenter;
    }

    public void detachPresenter() {
        mPresenter = null;
    }

    public void displayBarChart(BarData barData, String[] dateList) {
        mBarChart.setData(barData);
        mBarChart.setAutoScaleMinMaxEnabled(true);
        mBarChart.getXAxis().setValueFormatter(new MyXAxisValueFormatter(dateList));
        mBarChart.getXAxis().setLabelRotationAngle(45);
        mBarChart.getXAxis().setDrawLabels(true);
        mBarChart.invalidate();
    }

    public void displayPieChart(PieData pieData){
        mPieChart.setData(pieData);
        mPieChart.invalidate();
    }

    public void displayTable(List<String> data){
//        if (!data.isEmpty()){
//            if (mAdapter == null) {
//                mAdapter = new ArrayAdapter<>(getContext(), R.layout.stats_table_item);
//                mAdapter.add(getResources().getString(R.string.stats_list_title));
//                mAdapter.addAll(data);
//                mStatsList.setAdapter(mAdapter);
//            }else{
//                Set<String> listToSet = new HashSet<>(data);
//                mAdapter.clear();
//                mAdapter.add(getResources().getString(R.string.stats_list_title));
//                mAdapter.addAll(listToSet);
//            }
//        }
    }
}
