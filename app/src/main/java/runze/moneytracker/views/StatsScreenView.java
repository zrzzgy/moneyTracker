package runze.moneytracker.views;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.PieData;

import java.util.List;

import runze.moneytracker.HomeActivity;
import runze.moneytracker.R;
import runze.moneytracker.presenters.IPresenter;
import runze.moneytracker.presenters.StatsScreenPresenter;
import runze.moneytracker.utils.BRRecyclerAdapter;
import runze.moneytracker.utils.MTRecyclerAdapter;
import runze.moneytracker.utils.MyXAxisValueFormatter;
import runze.moneytracker.utils.RecyclerItemTouchHelper;


public class StatsScreenView extends RelativeLayout implements IView {
    private final String TAG = getClass().getSimpleName();
    private RecyclerView mRecyclerView;

    private StatsScreenPresenter mPresenter;
    private BarChart mBarChart;
    private PieChart mPieChart;
    private ListView mStatsList;

    private Description mDescription;

    private BRRecyclerAdapter mAdapter;

    public StatsScreenView(Context context) {
        super(context);
        View view = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.stats_view_layout, this);
        init(view);
        ((HomeActivity) getContext()).registerForContextMenu(mStatsList);
        mDescription = new Description();
        mDescription.setText("");
    }

    private void init(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mBarChart = findViewById(R.id.barChart);
        mPieChart = findViewById(R.id.pieChart);
        mStatsList = findViewById(R.id.statsList);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    public void updateViewWithData(){
        // populate the view with data
        updateRecyclerViewWithData();
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
        mBarChart.setDescription(mDescription);
        mBarChart.setScaleEnabled(false);
        mBarChart.setDrawGridBackground(false);
        mBarChart.setNoDataText("No Data");
        mBarChart.getLegend().setEnabled(false);

        mBarChart.getXAxis().setValueFormatter(new MyXAxisValueFormatter(dateList));
        mBarChart.getXAxis().setLabelRotationAngle(45);
        mBarChart.getXAxis().setDrawLabels(true);
        mBarChart.getXAxis().setEnabled(false);

        mBarChart.setData(barData);
        mBarChart.setVisibleXRangeMaximum(31);
        mBarChart.invalidate();
    }

    public void displayPieChart(PieData pieData) {
        mPieChart.setDescription(mDescription);
        mPieChart.setData(pieData);
        mPieChart.setElevation(10);
        mPieChart.setNoDataText("No Data");
        mPieChart.invalidate();
    }

    private void updateRecyclerViewWithData(){
        mAdapter = new BRRecyclerAdapter(mPresenter.loadDailyTotalExpensesFromPref());
        mRecyclerView.setAdapter(mAdapter);

    }

}
