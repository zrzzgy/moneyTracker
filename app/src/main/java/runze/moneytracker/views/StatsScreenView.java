package runze.moneytracker.views;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;

import runze.moneytracker.HomeActivity;
import runze.moneytracker.R;
import runze.moneytracker.presenters.IPresenter;
import runze.moneytracker.presenters.StatsScreenPresenter;
import runze.moneytracker.utils.DaySummaryBarChartRecyclerAdapter;


public class StatsScreenView extends RelativeLayout implements IView {
    private final String TAG = getClass().getSimpleName();
    private RecyclerView mRecyclerView;

    private StatsScreenPresenter mPresenter;
    private PieChart mPieChart;
    private ListView mStatsList;

    private Description mDescription;

    private DaySummaryBarChartRecyclerAdapter mAdapter;

    public StatsScreenView(Context context) {
        super(context);
        View view = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.stats_view_layout, this);
        init(view);
        ((HomeActivity) getContext()).registerForContextMenu(mStatsList);
        mDescription = new Description();
        mDescription.setText("");
    }

    private void init(View view) {
        mRecyclerView = view.findViewById(R.id.bar_chart_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mPieChart = findViewById(R.id.pie_chart);
        mStatsList = findViewById(R.id.stats_list);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mRecyclerView.setOnScrollChangeListener(new OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                   int firstItemPosition =  ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
                   int lastItemPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                   for (int i = firstItemPosition; i <= lastItemPosition; i++) {
                       Date itemDate = ((DaySummaryBarChartRecyclerAdapter)mRecyclerView.getAdapter()).getItem(i).getDate();
                       SimpleDateFormat df = new SimpleDateFormat("MM", Locale.getDefault());
                       String month = df.format(itemDate);
                   }
                }
            });
        }
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

    public void displayPieChart(PieData pieData) {
        mPieChart.setDescription(mDescription);
        mPieChart.setData(pieData);
        mPieChart.setNoDataText("No Data");
        mPieChart.invalidate();
    }

    private void updateRecyclerViewWithData(){
        mAdapter = new DaySummaryBarChartRecyclerAdapter(mPresenter.loadDailyTotalExpensesFromDataModel());
        mRecyclerView.setAdapter(mAdapter);

    }

}
