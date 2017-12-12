package runze.myapplication.presenters.statsScreenPresenter;

import android.content.res.AssetManager;
import android.util.Log;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import runze.myapplication.HomeActivity;
import runze.myapplication.utils.Expense;
import runze.myapplication.views.statsScreenView.IStatsScreenView;

import static runze.myapplication.HomeActivity.EXPENSES;
import static runze.myapplication.HomeActivity.STATS_TYPE;

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
        List<Expense> expenses = loadData();
        Set<Map.Entry<String, Double>> dataForPieChart = categoryAsKey(expenses);
        Set<Map.Entry<Date, Double>> dataForBarChart = dateAsKey(expenses);

        List<BarEntry> barEntries = new ArrayList<>();
        List<PieEntry> pieEntries = new ArrayList<>();

        float xPosition = 0;
        for (Map.Entry<Date, Double> entry: dataForBarChart) {
            barEntries.add(new BarEntry(xPosition++, entry.getValue().floatValue()));
        }

        for (Map.Entry<String, Double> entry: dataForPieChart) {
            pieEntries.add(new PieEntry(entry.getValue().floatValue()));
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "Date");
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.9f);

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Category");
        PieData pieData = new PieData(pieDataSet);

        mView.drawCharts(barData, pieData);
    }

    private List<Expense> loadData(){
        Gson gson = new Gson();
        List<Expense> expenseList = new ArrayList<>();

        //read saved data from preferences
        String savedExpenses = mParentActivity.mSharedPreferences.getString(EXPENSES, "");

        //if there is saved data, put it in first
        if (!savedExpenses.equals("")){
            expenseList = gson.fromJson(savedExpenses, new TypeToken<List<Expense>>(){}.getType());
        }
        return  expenseList;
    }

    private Set<Map.Entry<Date, Double>> dateAsKey(List<Expense> expenses){
        HashMap<Date, Double> sortedData = new HashMap<>();
        for (int i = 0; i < expenses.size(); i++) {
            Expense expense = expenses.get(i);
            if (sortedData.containsKey(expense.getmDate())){
                double sum = sortedData.get(expense.getmDate()) + expense.getmAmount();
                sortedData.put(expense.getmDate(), sum);
            }else{
                sortedData.put(expense.getmDate(), expense.getmAmount());
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
}
