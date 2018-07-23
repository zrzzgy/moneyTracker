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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import runze.moneytracker.HomeActivity;
import runze.moneytracker.models.DailyExpenseTotal;
import runze.moneytracker.models.DataModel;
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
        Set<Map.Entry<String, Double>> dataForPieChart = categoryAsKey(HomeActivity.mDataModel.getExpenseList());
        List<PieEntry> pieEntries = new ArrayList<>();

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

        mView.displayPieChart(pieData);
    }

    private List<DailyExpenseTotal> dateAsKey(List<Expense> expenses){
        boolean done = false;
        List<DailyExpenseTotal> listOfDailyExpenseTotal = new ArrayList<>();

        for (int i = 0; i < expenses.size(); i++) {
            Expense expense = expenses.get(i);

            for (DailyExpenseTotal dailyExpenseTotal : listOfDailyExpenseTotal) {
                if (dailyExpenseTotal.getDate().equals(expense.getDate())) {
                    double sum = dailyExpenseTotal.getTotalAmount() + expense.getAmount();
                    listOfDailyExpenseTotal.add(new DailyExpenseTotal(sum, expense.getDate()));
                    done = true;
                }
            }
                if (!done){
                    listOfDailyExpenseTotal.add(new DailyExpenseTotal(expense.getAmount(), expense.getDate()));
                }
        }
       return listOfDailyExpenseTotal;
    }

    private Set<Map.Entry<String, Double>> categoryAsKey(List<Expense> expenses){
        HashMap<String, Double> sortedData = new HashMap<>();
        for (int i = 0; i < expenses.size(); i++) {
            Expense expense = expenses.get(i);
            for (String category: expense.getCategory()) {
                if (sortedData.containsKey(category)){
                    double sum = sortedData.get(category) + expense.getAmount();
                    sortedData.put(category, sum);
                }else{
                    sortedData.put(category, expense.getAmount());
                }
            }
        }
        return sortedData.entrySet();
    }

    public List<DailyExpenseTotal> loadDailyTotalExpensesFromDataModel(){
        return dateAsKey(HomeActivity.mDataModel.getExpenseList());
    }

}
