package runze.moneytracker.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.List;

import runze.moneytracker.R;
import runze.moneytracker.models.Expense;
import runze.moneytracker.presenters.ExpenseAnalyzePresenter;
import runze.moneytracker.presenters.IPresenter;
import runze.moneytracker.utils.ExpenseCategoryDetailAnalysisListRecyclerAdapter;

public class CategoryAnalysisView extends RelativeLayout implements IView {

    private PieChart mPieChart;
    private Description mDescription;
    private RecyclerView mDetailedExpenseList;
    private ExpenseCategoryDetailAnalysisListRecyclerAdapter mExpenseCategoryDetailAnalysisListRecyclerAdapter;
    private PieData mPieData;
    private ExpenseAnalyzePresenter mPresenter;
    private TextView mCategoryTotalText;

    public CategoryAnalysisView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.category_analysis_layout, this);
        init();
    }

    @Override
    public void attachPresenter(IPresenter presenter) {
        mPresenter = (ExpenseAnalyzePresenter) presenter;
    }

    @Override
    public void detachPresenter() {
        mPresenter = null;
    }

    public void init() {
        mPieChart = findViewById(R.id.category_expense_detail_graph);
        mCategoryTotalText = findViewById(R.id.category_expense_detail_total);
        mDescription = new Description();
        mDescription.setText("");
        mDetailedExpenseList = findViewById(R.id.expense_detail_list_category);
        mDetailedExpenseList.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mDetailedExpenseList.setLayoutManager(mLayoutManager);
    }

    public void updatePieChart(PieData pieData) {
        mPieData = pieData;
        mPieChart.setDescription(mDescription);
        mPieChart.setData(pieData);
        mPieChart.setNoDataText("No Data");
        mPieChart.invalidate();
    }

    public void setGraphOnClickListener(OnChartValueSelectedListener listener){
        mPieChart.setOnChartValueSelectedListener(listener);
    }


    public void setListOfSameCategory (int categoryIndex, float total) {
        String category = ((PieDataSet) mPieData.getDataSet()).getValues().get(categoryIndex).getLabel();
        String categoryTotal =  String.valueOf(((PieDataSet) mPieData.getDataSet()).getValues().get(categoryIndex).getY());

        mCategoryTotalText.setText(categoryTotal + "/" + String.valueOf(total));
        List<Expense> listOfSameCategory = mPresenter.getListOfSameCategory(category);
        mExpenseCategoryDetailAnalysisListRecyclerAdapter = new ExpenseCategoryDetailAnalysisListRecyclerAdapter(listOfSameCategory);
        mDetailedExpenseList.setAdapter(mExpenseCategoryDetailAnalysisListRecyclerAdapter);
    }
}
