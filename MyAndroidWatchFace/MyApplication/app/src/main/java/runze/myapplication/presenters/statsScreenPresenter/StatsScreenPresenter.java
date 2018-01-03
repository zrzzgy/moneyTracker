package runze.myapplication.presenters.statsScreenPresenter;

import android.graphics.Color;
import android.text.format.DateUtils;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import runze.myapplication.HomeActivity;
import runze.myapplication.R;
import runze.myapplication.utils.Expense;
import runze.myapplication.views.statsScreenView.IStatsScreenView;

import static runze.myapplication.HomeActivity.EXPENSES;

public class StatsScreenPresenter implements IStatsScreenPresenter {
    private final String TAG = this.getClass().getSimpleName();
    private IStatsScreenView mView;
    private HomeActivity mParentActivity;

    public StatsScreenPresenter(HomeActivity activity){
        mParentActivity = activity;
    }

    @Override
    public void attachView(IStatsScreenView view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void updateView(){
        analyzeData();
    }

    private void analyzeData(){
        List<Expense> expenses = mParentActivity.loadData();
        Set<Map.Entry<String, Double>> dataForPieChart = categoryAsKey(expenses);
        Set<Map.Entry<String, Double>> dataForBarChart = dateAsKey(expenses);
        List<String> tableData = dataToTable(expenses);

        List<BarEntry> barEntries = new ArrayList<>();
        List<PieEntry> pieEntries = new ArrayList<>();

        //bar chart
        float xPosition = 0;
        for (Map.Entry<String, Double> entry: dataForBarChart) {
            barEntries.add(new BarEntry(xPosition++, entry.getValue().floatValue()));
            xPosition += 0.3;
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "Date");
        barDataSet.setLabel(mParentActivity.getResources().getString(R.string.bar_label));
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.2f);

        //pie chart
        for (Map.Entry<String, Double> entry: dataForPieChart) {
            pieEntries.add(new PieEntry(entry.getValue().floatValue(), entry.getKey()));
        }
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Category");
        if (pieEntries.size() > mParentActivity.mColorList.size()) {
            Random rng = new Random();
            for (int i = mParentActivity.mColorList.size(); i < pieEntries.size(); i++) {
                mParentActivity.mColorList.add(Color.rgb(rng.nextInt(255), rng.nextInt(255), rng.nextInt(255)));
            }
        }
        pieDataSet.setColors(mParentActivity.mColorList);
        pieDataSet.setValueTextSize(25);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setLabel(mParentActivity.getResources().getString(R.string.pie_label));
        PieData pieData = new PieData(pieDataSet);

        mView.displayBarChart(barData);
        mView.displayPieChart(pieData);
        mView.displayTable(tableData);
    }

    private Set<Map.Entry<String, Double>> dateAsKey(List<Expense> expenses){
        HashMap<String, Double> sortedData = new HashMap<>();
        for (int i = 0; i < expenses.size(); i++) {
            Expense expense = expenses.get(i);
           String dateForCurrentExpense = DateUtils.formatDateTime(mParentActivity.getApplicationContext(), expense.getmDate().getTime(), DateUtils.FORMAT_SHOW_DATE);
            if (sortedData.containsKey(dateForCurrentExpense)){
                double sum = sortedData.get(dateForCurrentExpense) + expense.getmAmount();
                sortedData.put(dateForCurrentExpense, sum);
            }else{
                sortedData.put(dateForCurrentExpense, expense.getmAmount());
            }
        }
       return sortedData.entrySet();
    }

    private Set<Map.Entry<String, Double>> categoryAsKey(List<Expense> expenses){
        HashMap<String, Double> sortedData = new HashMap<>();
        for (int i = 0; i < expenses.size(); i++) {
            Expense expense = expenses.get(i);
            if (sortedData.containsKey(expense.getmCategory())){
                double sum = sortedData.get(expense.getmCategory()) + expense.getmAmount();
                sortedData.put(expense.getmCategory(), sum);
            }else{
                sortedData.put(expense.getmCategory(), expense.getmAmount());
            }
        }
        return sortedData.entrySet();
    }

    private List<String> dataToTable(List<Expense> expenses){
        List<String> result = new ArrayList<>();
        for (Expense expense: expenses) {
            result.add(expense.getmDate().toString() + " " +
            expense.getmCategory() + " " +
            expense.getmAmount());
        }
        return result;
    }
}
