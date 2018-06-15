package runze.moneytracker.presenters;

import android.graphics.Color;
import android.text.format.DateUtils;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
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
import runze.moneytracker.views.IView;
import runze.moneytracker.views.StatsScreenView;

public class StatsScreenPresenter implements IPresenter{
    private final String TAG = this.getClass().getSimpleName();
    private StatsScreenView mView;
    private HomeActivity mParentActivity;

    public StatsScreenPresenter(HomeActivity activity){
        mParentActivity = activity;
    }

    @Override
    public void attachView(IView view) {
        mView = (StatsScreenView) view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    public void updateView(){
        analyzeData();
    }

    private void analyzeData(){
        List<Expense> expenses = mParentActivity.loadExpensesFromPref();
        Set<Map.Entry<String, Double>> dataForPieChart = categoryAsKey(expenses);
        Set<Map.Entry<String, Double>> dataForBarChart = dateAsKey(expenses);

        List<BarEntry> barEntries = new ArrayList<>();
        List<PieEntry> pieEntries = new ArrayList<>();

        //bar chart
        float xPosition = 0;
        int index = 0;
        String[] dateList = new String[dataForBarChart.size()];
        for (Map.Entry<String, Double> entry: dataForBarChart) {
            barEntries.add(new BarEntry(xPosition++, entry.getValue().floatValue()));
            dateList[index++] = entry.getKey();
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "");
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(1);

        //pie chart
        for (Map.Entry<String, Double> entry: dataForPieChart) {
            pieEntries.add(new PieEntry(entry.getValue().floatValue(), entry.getKey()));
        }
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        if (pieEntries.size() > mParentActivity.mColorList.size()) {
            Random rng = new Random();
            for (int i = mParentActivity.mColorList.size(); i < pieEntries.size(); i++) {
                mParentActivity.mColorList.add(Color.rgb(rng.nextInt(255), rng.nextInt(255), rng.nextInt(255)));
            }
        }
        pieDataSet.setColors(mParentActivity.mColorList);
        pieDataSet.setValueTextSize(25);
        pieDataSet.setValueTextColor(Color.WHITE);
        PieData pieData = new PieData(pieDataSet);

        mView.displayBarChart(barData, dateList);
        mView.displayPieChart(pieData);
    }

    private Set<Map.Entry<String, Double>> dateAsKey(List<Expense> expenses){
        HashMap<String, Double> sortedData = new HashMap<>();
        for (int i = 0; i < expenses.size(); i++) {
            Expense expense = expenses.get(i);
           String dateForCurrentExpense = DateUtils.formatDateTime(mParentActivity.getApplicationContext(), expense.getDate().getTime(), DateUtils.FORMAT_SHOW_DATE);
            if (sortedData.containsKey(dateForCurrentExpense)){
                double sum = sortedData.get(dateForCurrentExpense) + expense.getAmount();
                sortedData.put(dateForCurrentExpense, sum);
            }else{
                sortedData.put(dateForCurrentExpense, expense.getAmount());
            }
        }
       return sortedData.entrySet();
    }

    private Set<Map.Entry<String, Double>> categoryAsKey(List<Expense> expenses){
        HashMap<String, Double> sortedData = new HashMap<>();
        for (int i = 0; i < expenses.size(); i++) {
            Expense expense = expenses.get(i);
            if (sortedData.containsKey(expense.getCategory())){
                double sum = sortedData.get(expense.getCategory()) + expense.getAmount();
                sortedData.put(expense.getCategory(), sum);
            }else{
                sortedData.put(expense.getCategory(), expense.getAmount());
            }
        }
        return sortedData.entrySet();
    }
}
