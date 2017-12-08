package runze.myapplication.presenters.statsScreenPresenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import runze.myapplication.HomeActivity;
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
        mView.renderData(analyzeData());
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

    public  List<String> analyzeData(){
        List<Expense> expenses = loadData();
        HashMap<String, Double> sortedData = new HashMap<>();
        List<String> dataAsString = new ArrayList<>();
        for (int i = 0; i < expenses.size(); i++) {
            Expense expense = expenses.get(i);
            if (sortedData.containsKey(expense.getmCategory())){
                double sum = sortedData.get(expense.getmCategory()) + expense.getmAmount();
                sortedData.put(expense.getmCategory(), sum);
            }else{
                sortedData.put(expense.getmCategory(), expense.getmAmount());
            }
        }
        Set<Map.Entry<String, Double>> set = sortedData.entrySet();
        for (Map.Entry<String, Double> entry : set) {
            dataAsString.add(entry.getKey() + " : " + entry.getValue());
        }
        return dataAsString;
    }
}
