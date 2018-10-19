package runze.moneytracker.views;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import runze.moneytracker.R;
import runze.moneytracker.models.DailyExpenseTotal;
import runze.moneytracker.presenters.ExpenseAnalyzePresenter;
import runze.moneytracker.presenters.IPresenter;
import runze.moneytracker.utils.DaySummaryBarChartRecyclerAdapter;

public class DayAnalyzeView extends LinearLayout implements IView {

    private TextView mDailyExpenseMonth;
    private RecyclerView mDailyExpenseDetailGraph;
    private RecyclerView mDailyExpenseDetailList;
    private DaySummaryBarChartRecyclerAdapter mAdapter;
    private ExpenseAnalyzePresenter mPresenter;


    public DayAnalyzeView(Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.day_analyze_view, this);
        init(view);
    }

    private void init(View view){
        mDailyExpenseMonth = view.findViewById(R.id.daily_expense_month);
        mDailyExpenseDetailGraph = view.findViewById(R.id.daily_expense_detail_graph);
        mDailyExpenseDetailList = view.findViewById(R.id.daily_expense_detail_list);

        LinearLayoutManager mDailyExpenseDetailGraphLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true);
        mDailyExpenseDetailGraph.setLayoutManager(mDailyExpenseDetailGraphLayoutManager);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mDailyExpenseDetailGraph.setOnScrollChangeListener(new OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    int firstItemPosition =  ((LinearLayoutManager) mDailyExpenseDetailGraph.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
                    int lastItemPosition = ((LinearLayoutManager) mDailyExpenseDetailGraph.getLayoutManager()).findLastCompletelyVisibleItemPosition();

                    Map<String, Integer> keyValuePair = new HashMap<>();

                    for (int i = firstItemPosition; i <= lastItemPosition; i++) {
                        Date itemDate = ((DaySummaryBarChartRecyclerAdapter)mDailyExpenseDetailGraph.getAdapter()).getItem(i).getDate();
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

                    mDailyExpenseMonth.setText(largerMonth);
                }
            });
        }
    }


    @Override
    public void attachPresenter(IPresenter presenter) {
        mPresenter = (ExpenseAnalyzePresenter) presenter;
    }

    @Override
    public void detachPresenter() {
        mPresenter = null;
    }

    public void updateBarChart(List<DailyExpenseTotal> expenseTotals){
        mAdapter = new DaySummaryBarChartRecyclerAdapter(expenseTotals);
        mDailyExpenseDetailGraph.setAdapter(mAdapter);
    }

}
