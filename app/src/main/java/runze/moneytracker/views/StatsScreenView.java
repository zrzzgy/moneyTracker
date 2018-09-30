package runze.moneytracker.views;

import android.content.Context;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;

import runze.moneytracker.HomeActivity;
import runze.moneytracker.R;
import runze.moneytracker.models.DailyExpenseTotal;
import runze.moneytracker.presenters.IPresenter;
import runze.moneytracker.presenters.StatsScreenPresenter;
import runze.moneytracker.utils.DaySummaryBarChartRecyclerAdapter;


public class StatsScreenView extends RelativeLayout implements IView {
    private final String TAG = getClass().getSimpleName();
    private RecyclerView mRecyclerView;

    private StatsScreenPresenter mPresenter;
    private PieChart mPieChart;
    private ListView mStatsList;
    private TextView mDailyExpenseTitle;
    private TabLayout mTabLayout;
    private TabLayout.Tab mTabDaily;
    private TabLayout.Tab mTabCategory;

    private Description mDescription;

    private DaySummaryBarChartRecyclerAdapter mAdapter;

    public StatsScreenView(Context context) {
        super(context);
        View view = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.stats_view_layout, this);
        init(view);
        //todo check when we use context view
        //((HomeActivity) getContext()).registerForContextMenu(mStatsList);
        mDescription = new Description();
        mDescription.setText("");
    }

    private void init(View view) {
        mRecyclerView = view.findViewById(R.id.daily_expense_detail_graph);
        mRecyclerView.setHasFixedSize(true);

        mPieChart = findViewById(R.id.category_expense_detail_graph);
        //mStatsList = findViewById(R.id.stats_list);
        mDailyExpenseTitle = findViewById(R.id.daily_expense_month);
        mTabLayout = findViewById(R.id.upper_tab_layout);
        mTabDaily = mTabLayout.getTabAt(0);
        mTabCategory = mTabLayout.getTabAt(1);
        mTabDaily.setCustomView(R.layout.daily_expense_view);
        mTabCategory.setCustomView(R.layout.category_expense_view);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mRecyclerView.setOnScrollChangeListener(new OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                   int firstItemPosition =  ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
                   int lastItemPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();

                    Map<String, Integer> keyValuePair = new HashMap<>();

                   for (int i = firstItemPosition; i <= lastItemPosition; i++) {
                       Date itemDate = ((DaySummaryBarChartRecyclerAdapter)mRecyclerView.getAdapter()).getItem(i).getDate();
                       SimpleDateFormat df = new SimpleDateFormat("MM", Locale.getDefault());
                       String month = df.format(itemDate);

                       if (keyValuePair.containsKey(month)){
                           int occurrence = keyValuePair.get(month);
                           keyValuePair.put(month, occurrence+1);
                       }else{
                           keyValuePair.put(month, 1);
                       }
                   }

                   String largerMonth;
                   if (keyValuePair.keySet().size() > 1){
                       Iterator<String> iterator = keyValuePair.keySet().iterator();
                       String firstMonth = iterator.next();
                       String secondMonth = iterator.next();
                       int firstMonthOccurrence = keyValuePair.get(firstMonth);
                       int secondMonthOccurrence = keyValuePair.get(secondMonth);
                       largerMonth = firstMonthOccurrence >= secondMonthOccurrence ? firstMonth : secondMonth;
                   }else{
                       largerMonth = keyValuePair.keySet().iterator().next();
                   }

                    mDailyExpenseTitle.setText(largerMonth);
                }
            });
        }
    }

    @Override
    public void attachPresenter(IPresenter presenter) {
        mPresenter = (StatsScreenPresenter) presenter;
    }

    @Override
    public void detachPresenter() {
        mPresenter = null;
    }

    public void updatePieChart(PieData pieData) {
        mPieChart.setDescription(mDescription);
        mPieChart.setData(pieData);
        mPieChart.setNoDataText("No Data");
        mPieChart.invalidate();
    }

    public void updateBarChart(List<DailyExpenseTotal> expenseTotals){
        mAdapter = new DaySummaryBarChartRecyclerAdapter(expenseTotals);
        mRecyclerView.setAdapter(mAdapter);

    }

}
