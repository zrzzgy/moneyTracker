package runze.moneytracker.presenters;

import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import runze.moneytracker.HomeActivity;
import runze.moneytracker.models.Expense;
import runze.moneytracker.views.CategoryExpenseAnalysisView;
import runze.moneytracker.views.IView;

public class CategoryExpenseAnalysisPresenter extends StatsScreenBasePresenter{
    private CategoryExpenseAnalysisView mView;

    public CategoryExpenseAnalysisPresenter(Context context) {
        super(context);
    }

    @Override
    public void attachView(IView view) {
        mView = (CategoryExpenseAnalysisView) view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    public void updateView() {
        analyzeData();
    }

    private void analyzeData() {
        mView.updatePieChart(analyzeDataForPieChart());
    }

    private PieData analyzeDataForPieChart() {
        Set<Map.Entry<String, Double>> dataForPieChart = categoryAsKey(HomeActivity.mDataModel.getExpenseList());
        List<PieEntry> pieEntries = new ArrayList<>();

        //pie chart
        for (Map.Entry<String, Double> entry : dataForPieChart) {
            pieEntries.add(new PieEntry(entry.getValue().floatValue(), entry.getKey()));
        }
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        if (pieEntries.size() > HomeActivity.mDataModel.getColorList().size()) {
            Random rng = new Random();
            for (int i = HomeActivity.mDataModel.getColorList().size(); i < pieEntries.size(); i++) {
                HomeActivity.mDataModel.getColorList().add(Color.rgb(rng.nextInt(255), rng.nextInt(255), rng.nextInt(255)));
            }
        }
        pieDataSet.setColors(HomeActivity.mDataModel.getColorList());
        pieDataSet.setValueTextSize(25);
        pieDataSet.setValueTextColor(Color.WHITE);
        return new PieData(pieDataSet);
    }


    /**
     * Sort the data according to different categories. Expenses of the same category are merged
     * @param expenses a list of individual expenses
     * @return A hash map of <category, amount>
     */
    private Set<Map.Entry<String, Double>> categoryAsKey(List<Expense> expenses) {
        HashMap<String, Double> sortedData = new HashMap<>();
        for (int i = 0; i < expenses.size(); i++) {
            Expense expense = expenses.get(i);
            for (String category : expense.getCategory()) {
                if (sortedData.containsKey(category)) {
                    double sum = sortedData.get(category) + expense.getAmount();
                    sortedData.put(category, sum);
                } else {
                    sortedData.put(category, expense.getAmount());
                }
            }
        }
        return sortedData.entrySet();
    }
}
