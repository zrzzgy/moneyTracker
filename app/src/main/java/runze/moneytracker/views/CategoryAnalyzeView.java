package runze.moneytracker.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;

import runze.moneytracker.R;
import runze.moneytracker.presenters.ExpenseAnalyzePresenter;
import runze.moneytracker.presenters.IPresenter;

public class CategoryAnalyzeView extends LinearLayout implements IView {

    private PieChart mPieChart;
    private Description mDescription;
    private RecyclerView mRecyclerView;
    private ExpenseAnalyzePresenter mPresenter;

    public CategoryAnalyzeView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.category_analyze_view, this);
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
        mRecyclerView = findViewById(R.id.category_expense_detail_list);
        mDescription = new Description();
        mDescription.setText("");
    }

    public void updatePieChart(PieData pieData) {
        mPieChart.setDescription(mDescription);
        mPieChart.setData(pieData);
        mPieChart.setNoDataText("No Data");
        mPieChart.invalidate();
    }
}