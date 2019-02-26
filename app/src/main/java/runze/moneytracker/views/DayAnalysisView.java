package runze.moneytracker.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import runze.moneytracker.R;
import runze.moneytracker.models.BaseExpenseTotal;
import runze.moneytracker.models.Expense;
import runze.moneytracker.presenters.ExpenseAnalyzePresenter;
import runze.moneytracker.presenters.IPresenter;
import runze.moneytracker.utils.DailyAnalysisRecyclerAdapter;
import runze.moneytracker.utils.DateSummaryBarChartRecyclerAdapter;

public class DayAnalysisView extends RelativeLayout implements IView {

    private final String TAG = this.getClass().getSimpleName();
    private TextView mDailyExpenseMonth;
    private RecyclerView mDailyExpenseDetailGraph;
    private ExpenseAnalyzePresenter mPresenter;
    private RecyclerView mDateExpenseDetailList;
    private TextView mDateExpenseDetailTotal;
    private Spinner mSpinner;
    private int whichSpinnerIsSelected = 1;
    private Button mDataAnalysisButton;
    private View mAlertLayout;
    private AlertDialog mAlertDialog;

    public DayAnalysisView(Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.day_analysis_layout, this);
        init(view);
    }

    private void init(View view) {
        mDailyExpenseMonth = view.findViewById(R.id.time_range_month_year);
        mDailyExpenseDetailGraph = view.findViewById(R.id.time_range_sorted_expense_graph);
        mDateExpenseDetailList = view.findViewById(R.id.expense_detail_list_category);
        mDataAnalysisButton = view.findViewById(R.id.data_analyze_button);

        mDataAnalysisButton.setOnClickListener(dataAnalysisButtonListener);
        // mAnalysisDetailList = view.findViewById(R.id.analyze_view_expense_detail_list);
        mDateExpenseDetailTotal = view.findViewById(R.id.time_range_expense_detail_total);
        mSpinner = view.findViewById(R.id.time_range_spinner);
        String[] mItems = getResources().getStringArray(R.array.date);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                String[] date = getResources().getStringArray(R.array.date);
                if (date[pos].equals("Day")) {
                    mPresenter.updateSpinnerView(1);
                    whichSpinnerIsSelected = 1;

                } else if (date[pos].equals("Week")) {
                    mPresenter.updateSpinnerView(2);
                    whichSpinnerIsSelected = 2;

                } else if (date[pos].equals("Month")) {
                    mPresenter.updateSpinnerView(3);
                    whichSpinnerIsSelected =3;

                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
        LinearLayoutManager mDailyExpenseDetailGraphLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true);
        mDailyExpenseDetailGraph.setLayoutManager(mDailyExpenseDetailGraphLayoutManager);

        mDateExpenseDetailList.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mDateExpenseDetailList.setLayoutManager(mLayoutManager);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mDailyExpenseDetailGraph.setOnScrollChangeListener(new OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    int firstItemPosition = ((LinearLayoutManager) mDailyExpenseDetailGraph.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
                    int lastItemPosition = ((LinearLayoutManager) mDailyExpenseDetailGraph.getLayoutManager()).findLastCompletelyVisibleItemPosition();

                    Map<String, Integer> keyValuePair = new HashMap<>();

                    for (int i = firstItemPosition; i <= lastItemPosition; i++) {
                        Date itemDate = ((DateSummaryBarChartRecyclerAdapter) mDailyExpenseDetailGraph.getAdapter()).getItem(i).getDate();
                        SimpleDateFormat dfMonth = new SimpleDateFormat("MM", Locale.getDefault());
                        String month = dfMonth.format(itemDate);

                        if (keyValuePair.containsKey(month)) {
                            int occurrence = keyValuePair.get(month);
                            keyValuePair.put(month, occurrence + 1);
                        } else {
                            keyValuePair.put(month, 1);
                        }
                    }

                    String largerMonth;
                    if (keyValuePair.keySet().size() > 1) {
                        Iterator<String> iterator = keyValuePair.keySet().iterator();
                        String firstMonth = iterator.next();
                        String secondMonth = iterator.next();
                        int firstMonthOccurrence = keyValuePair.get(firstMonth);
                        int secondMonthOccurrence = keyValuePair.get(secondMonth);
                        largerMonth = firstMonthOccurrence >= secondMonthOccurrence ? firstMonth : secondMonth;
                    } else {
                        largerMonth = keyValuePair.keySet().iterator().next();
                    }
                    switch (largerMonth) {
                        case "01":
                            mDailyExpenseMonth.setText(R.string.January);
                            break;
                        case "02":
                            mDailyExpenseMonth.setText(R.string.February);
                            break;
                        case "03":
                            mDailyExpenseMonth.setText(R.string.March);
                            break;
                        case "04":
                            mDailyExpenseMonth.setText(R.string.April);
                            break;
                        case "05":
                            mDailyExpenseMonth.setText(R.string.May);
                            break;
                        case "06":
                            mDailyExpenseMonth.setText(R.string.June);
                            break;
                        case "07":
                            mDailyExpenseMonth.setText(R.string.July);
                            break;
                        case "08":
                            mDailyExpenseMonth.setText(R.string.August);
                            break;
                        case "09":
                            mDailyExpenseMonth.setText(R.string.September);
                            break;
                        case "10":
                            mDailyExpenseMonth.setText(R.string.October);
                            break;
                        case "11":
                            mDailyExpenseMonth.setText(R.string.November);
                            break;
                        case "12":
                            mDailyExpenseMonth.setText(R.string.December);
                            break;
                        default:
                            break;

                    }
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

    public void updateBarChart(List<? extends BaseExpenseTotal> expenseTotals) {
        DateSummaryBarChartRecyclerAdapter mDateSummaryBarChartRecyclerAdapter = new DateSummaryBarChartRecyclerAdapter(expenseTotals);
        mDailyExpenseDetailGraph.setAdapter(mDateSummaryBarChartRecyclerAdapter);
    }

    public void setListOfSameDate(Date date) {
        if (whichSpinnerIsSelected == 1) {
            SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
            List<Expense> listOfSameDay = mPresenter.getListOfSameDay(df.format(date));
            mDateExpenseDetailTotal.setText("");
            double dayAmount = 0;
            for (Expense expense : listOfSameDay) {
                dayAmount += expense.getAmount();
            }
            SimpleDateFormat day = new SimpleDateFormat("dd", Locale.getDefault());
            mDateExpenseDetailTotal.setText(String.format("%s %s%s %s%s%s",
                    mDailyExpenseMonth.getText(),
                    day.format(date),
                    getContext().getString(R.string.partVsTotal),
                    String.valueOf(dayAmount),
                    getContext().getString(R.string.slash),
                    String.valueOf(mPresenter.getExpenseTotal())));
            mDateExpenseDetailList.setAdapter(new DailyAnalysisRecyclerAdapter(listOfSameDay));
        } else if (whichSpinnerIsSelected == 2) {
            List<Expense> listOfSameWeek = mPresenter.getListOfSameWeek(date);
            mDateExpenseDetailTotal.setText("");
            double dayAmount = 0;
            for (Expense expense : listOfSameWeek) {
                dayAmount += expense.getAmount();
            }
            SimpleDateFormat day = new SimpleDateFormat("dd", Locale.getDefault());
            mDateExpenseDetailTotal.setText(String.format("%s %s%s %s%s%s",
                    mDailyExpenseMonth.getText(),
                    day.format(date),
                    getContext().getString(R.string.partVsTotal),
                    String.valueOf(dayAmount),
                    getContext().getString(R.string.slash),
                    String.valueOf(mPresenter.getExpenseTotal())));
            mDateExpenseDetailList.setAdapter(new DailyAnalysisRecyclerAdapter(listOfSameWeek));
        } else if(whichSpinnerIsSelected == 3) {
            SimpleDateFormat df = new SimpleDateFormat("MM-yyyy", Locale.getDefault());
            List<Expense> listOfSameMonth = mPresenter.getListOfSameMonth(df.format(date));
            mDateExpenseDetailTotal.setText("");
            double dayAmount = 0;
            for (Expense expense : listOfSameMonth) {
                dayAmount += expense.getAmount();
            }
            SimpleDateFormat day = new SimpleDateFormat("dd", Locale.getDefault());
            mDateExpenseDetailTotal.setText(String.format("%s %s%s %s%s%s",
                    mDailyExpenseMonth.getText(),
                    day.format(date),
                    getContext().getString(R.string.partVsTotal),
                    String.valueOf(dayAmount),
                    getContext().getString(R.string.slash),
                    String.valueOf(mPresenter.getExpenseTotal())));
            mDateExpenseDetailList.setAdapter(new DailyAnalysisRecyclerAdapter(listOfSameMonth));
        }

    }

    private OnClickListener dataAnalysisButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.v(TAG, "creating new item dialog");
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;

            mAlertLayout = inflater.inflate(R.layout.analysis_dialog, null);
            mAlertDialog = new AlertDialog.Builder(getContext())
                    .setView(mAlertLayout)
                    .setTitle(getResources().getString(R.string.data_analysis_title))
                    .setPositiveButton(getResources().getString(R.string.ok), mInputDialogConfirmListener)
                    .create();

            mAlertDialog.setCanceledOnTouchOutside(false);
            mAlertDialog.show();
        }
    };

    private DialogInterface.OnClickListener mInputDialogConfirmListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            Log.v(TAG, "confirming output dialog");

        }
    };
}
