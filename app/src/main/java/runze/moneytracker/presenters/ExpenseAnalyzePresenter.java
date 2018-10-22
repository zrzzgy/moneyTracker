package runze.moneytracker.presenters;

import android.graphics.Color;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import runze.moneytracker.models.DailyExpenseTotal;
import runze.moneytracker.models.DataModel;
import runze.moneytracker.models.Expense;
import runze.moneytracker.views.CategoryAnalyzeView;
import runze.moneytracker.views.DayAnalyzeView;
import runze.moneytracker.views.IView;

public class ExpenseAnalyzePresenter implements IPresenter {
    private IView mView;
    private List<Expense> mExpenses;
    private HashSet<String> mCategories;
    private List<Integer> mColorList;

    public ExpenseAnalyzePresenter(DataModel dataModel) {
        super();
        mExpenses = dataModel.getExpenses();
        mCategories = dataModel.getCategories();
        mColorList = dataModel.getColorList();
    }

    @Override
    public void attachView(IView view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    /**
     * Sort the data into a list according to different date, merge expenses from the same date
     * @param expenses a list of individual expenses
     * @return a list of daily expense total with expenses from the same date merged
     */
    private List<DailyExpenseTotal> dateAsKey(List<Expense> expenses) {
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
            if (!done) {
                listOfDailyExpenseTotal.add(new DailyExpenseTotal(expense.getAmount(), expense.getDate()));
            }
        }
        return orderAndAddPlaceHolderDates(listOfDailyExpenseTotal);
    }


    /**
     * Sort the date-oriented data by placing earlier dates in the front,
     * and add dates with 0 expense so that dates are consecutive.
     * @param data list of expenses sorted and merged by date
     * @return list of consecutive expenses sorted and merged by date
     */
    private List<DailyExpenseTotal> orderAndAddPlaceHolderDates(List<DailyExpenseTotal> data) {
        List<DailyExpenseTotal> result = new LinkedList<>();
        int n = data.size();

        if (n > 0) {
            //ascending sort data
            for (int i = 0; i < n; i++) {
                for (int j = 1; j < n - i; j++) {
                    if (data.get(j - 1).getDate().getTime() < data.get(j).getDate().getTime()) {
                        // swap data[j] and data[j+1]
                        DailyExpenseTotal temp = data.get(j - 1);
                        data.set(j - 1, data.get(j));
                        data.set(j, temp);
                    }
                }
            }

            //add place holder dates
            for (int i = 1; i < n; i++) {
                long timeInBetween = data.get(i - 1).getDate().getTime() - data.get(i).getDate().getTime();
                long daysInBetween = timeInBetween / (1000 * 60 * 60 * 24);
                result.add(data.get(i - 1));
                for (int j = 0; j < daysInBetween - 1; j++) {
                    long nextTime = result.get(result.size() - 1).getDate().getTime() - 1000 * 60 * 60 * 24;
                    Date nextDate = new Date(nextTime);
                    DailyExpenseTotal placeHolder = new DailyExpenseTotal((double) 0, nextDate);
                    result.add(placeHolder);
                }
            }

            result.add(data.get(n - 1));
        }

        return result;
    }

    public void updateView() {
        analyzeData();
    }

    private void analyzeData() {
        if (mView instanceof DayAnalyzeView) {
            ((DayAnalyzeView) mView).updateBarChart(dateAsKey(mExpenses));
        }else if (mView instanceof CategoryAnalyzeView)
            ((CategoryAnalyzeView) mView).updatePieChart(analyzeDataForPieChart());
    }

    private PieData analyzeDataForPieChart() {
        Set<Map.Entry<String, Double>> dataForPieChart = categoryAsKey(mExpenses);
        List<PieEntry> pieEntries = new ArrayList<>();

        //pie chart
        for (Map.Entry<String, Double> entry : dataForPieChart) {
            pieEntries.add(new PieEntry(entry.getValue().floatValue(), entry.getKey()));
        }
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        if (pieEntries.size() > mColorList.size()) {
            Random rng = new Random();
            for (int i = mColorList.size(); i < pieEntries.size(); i++) {
                mColorList.add(Color.rgb(rng.nextInt(255), rng.nextInt(255), rng.nextInt(255)));
            }
        }
        pieDataSet.setColors(mColorList);
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