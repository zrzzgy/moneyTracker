package runze.moneytracker.presenters;

import android.graphics.Color;
import android.view.View;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import runze.moneytracker.models.BaseExpenseTotal;
import runze.moneytracker.models.DailyExpenseTotal;
import runze.moneytracker.models.DataModel;
import runze.moneytracker.models.Expense;
import runze.moneytracker.models.MonthlyExpenseTotal;
import runze.moneytracker.models.WeeklyExpenseTotal;
import runze.moneytracker.views.CategoryAnalysisView;
import runze.moneytracker.views.DayAnalysisView;
import runze.moneytracker.views.IView;

public class ExpenseAnalyzePresenter implements IPresenter {
    private IView mView;
    private DataModel mDataModel;
    private List<Expense> mListOfSameCategory;
    private List<Expense> mListOfSameDay;
    private List<Expense> mListOfSameWeek;
    private List<Expense> mListOfSameMonth;
    private OnChartValueSelectedListener mPieChartOnClickListener = new OnChartValueSelectedListener() {
        @Override
        public void onNothingSelected() {

        }

        @Override
        public void onValueSelected(Entry e, Highlight h) {
            ((CategoryAnalysisView) mView).setListOfSameCategory(((int) h.getX()), mDataModel.getExpenseTotal());
        }
    };

    public ExpenseAnalyzePresenter(DataModel dataModel) {
        super();
        updateModel(dataModel);
    }

    public void updateModel(DataModel dataModel) {
        mDataModel = dataModel;
    }

    @Override
    public void attachView(IView view) {
        mView = view;
        if (mView instanceof CategoryAnalysisView) {
            ((CategoryAnalysisView) mView).setGraphOnClickListener(mPieChartOnClickListener);
        }
    }

    @Override
    public void detachView() {
        mView = null;
    }

    /**
     * Sort the data into a list according to different date, merge expenses from the same date
     *
     * @return a list of daily expense total with expenses from the same date merged
     */
    public void sortDataForDayAnalysis() {
        boolean doneDay;
        boolean doneWeek;
        boolean doneMonth;
        boolean addNewDay = false;
        boolean addNewWeek = false;
        boolean addNewMonth = false;
        List<DailyExpenseTotal> dailyListOfExpenseTotalList = new ArrayList<>();
        List<WeeklyExpenseTotal> weeklyListOfExpenseTotalList = new ArrayList<>();
        List<MonthlyExpenseTotal> monthlyListOfExpenseTotalList = new ArrayList<>();


        for (int i = 0; i < mDataModel.getExpenses().size(); i++) {
            Expense expense = mDataModel.getExpenses().get(i);
            doneDay = false;
            doneWeek = false;
            doneMonth = false;

            addNewDay = false;
            addNewWeek = false;
            addNewMonth = false;

            for (DailyExpenseTotal dailyExpenseTotal : dailyListOfExpenseTotalList) {
                    if (dailyExpenseTotal.getDay().equals(expense.getDay())) {
                        double sum = dailyExpenseTotal.getTotalAmount() + expense.getAmount();
                        dailyExpenseTotal.setTotalAmount(sum);
                        doneDay = true;
                    }
            }

            for (WeeklyExpenseTotal weeklyExpenseTotal : weeklyListOfExpenseTotalList) {
                if (isSameWeek((weeklyExpenseTotal).getWeek(), expense.getWeek())) {
                    double sum = weeklyExpenseTotal.getTotalAmount() + expense.getAmount();
                    weeklyExpenseTotal.setTotalAmount(sum);
                    doneWeek = true;
                }
            }

            for (MonthlyExpenseTotal monthlyExpenseTotal : monthlyListOfExpenseTotalList) {
                if (monthlyExpenseTotal.getMonth().equals(expense.getMonth())) {
                    double sum = monthlyExpenseTotal.getTotalAmount() + expense.getAmount();
                    monthlyExpenseTotal.setTotalAmount(sum);
                    doneMonth = true;
                }
            }

            if (!doneDay) {
                addNewDay = true;
                dailyListOfExpenseTotalList.add(new DailyExpenseTotal(expense.getAmount(), expense.getDate()));
            }
            if (!doneWeek) {
                addNewWeek = true;
                weeklyListOfExpenseTotalList.add(new WeeklyExpenseTotal(expense.getAmount(), expense.getDate()));
            }
            if (!doneMonth) {
                addNewMonth = true;
                monthlyListOfExpenseTotalList.add(new MonthlyExpenseTotal(expense.getAmount(), expense.getDate()));
            }
        }
        if (addNewDay) {
             orderAndAddPlaceHolderDates(dailyListOfExpenseTotalList);
        } else {
            //return listOfExpenseTotal;
        }
    }


    /**
     * Sort the date-oriented data by placing earlier dates in the front,
     * and add dates with 0 expense so that dates are consecutive.
     *
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

            //add place holder
            for (int i = 1; i < n; i++) {
                String day1 = data.get(i - 1).getDay();
                String day2 = data.get(i).getDay();
                Date date1 = new Date();
                Date date2 = new Date();
                try {
                    date1 = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).parse(day1);
                    date2 = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).parse(day2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                long timeInBetween = date1.getTime() - date2.getTime();
                long daysInBetween = (long) Math.ceil((double) timeInBetween / (1000 * 60 * 60 * 24)) - 1;
                result.add(data.get(i - 1)); // Add the latest day first

                for (int j = 0; j < daysInBetween; j++) {
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
        if (mView instanceof DayAnalysisView) {
            ((DayAnalysisView) mView).updateBarChart(sortDataForDayAnalysis());
            ((DayAnalysisView) mView).setListOfSameDay(new Date());
        } else if (mView instanceof CategoryAnalysisView) {
            ((CategoryAnalysisView) mView).updatePieChart(sortDataForCategoryAnalysis());
            ((CategoryAnalysisView) mView).setListOfSameCategory(0, mDataModel.getExpenseTotal());
        }
    }

    private PieData sortDataForCategoryAnalysis() {
        Set<Map.Entry<String, Double>> dataForPieChart = categoryAsKey(mDataModel.getExpenses());
        List<PieEntry> pieEntries = new ArrayList<>();

        //pie chart
        for (Map.Entry<String, Double> entry : dataForPieChart) {
            pieEntries.add(new PieEntry(entry.getValue().floatValue(), entry.getKey()));
        }
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        if (pieEntries.size() > mDataModel.getColorList().size()) {
            Random rng = new Random();
            for (int i = mDataModel.getColorList().size(); i < pieEntries.size(); i++) {
                mDataModel.getColorList().add(Color.rgb(rng.nextInt(255), rng.nextInt(255), rng.nextInt(255)));
            }
        }
        pieDataSet.setColors(mDataModel.getColorList());
        pieDataSet.setValueTextSize(0);
        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        return new PieData(pieDataSet);
    }


    /**
     * Sort the data according to different categories. Expenses of the same category are merged
     *
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

    private boolean isSameWeek(ArrayList<Integer> arrayList1, ArrayList<Integer> arrayList2) {
        if(arrayList1.size() != arrayList2.size())
            return false;
        for (int i = 0; i<arrayList1.size(); i ++) {
            if (arrayList1.get(i) != arrayList2.get(i)) {
                return false;
            }
        }
        return true;
    }

    private void generateListOfSameCategory(String category) {
        mListOfSameCategory = new ArrayList<>();
        List<Expense> expenseList = mDataModel.getExpenses();
        for (int i = 0; i < expenseList.size(); i++) {
            if (expenseList.get(i).getCategory().contains(category)) {
                mListOfSameCategory.add(expenseList.get(i));
            }
        }
    }

    private void generateListOfSameDay(String date) {
        mListOfSameDay = new ArrayList<>();
        List<Expense> expenseList = mDataModel.getExpenses();
        for (int i = 0; i < expenseList.size(); i++) {
            if (expenseList.get(i).getDay().equals(date)) {
                mListOfSameDay.add(expenseList.get(i));
            }
        }
    }

    private void generateListOfSameWeek(Date date) {
        mListOfSameWeek = new ArrayList<>();
        List<Expense> expenseList = mDataModel.getExpenses();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);
        int year = cal.get(Calendar.YEAR);
        ArrayList<Integer> weekAndYear = new ArrayList<>();
        weekAndYear.add(0, weekOfYear);
        weekAndYear.add(1,year);
        for (int i = 0; i < expenseList.size(); i++) {
            if (isSameWeek(expenseList.get(i).getWeek(), weekAndYear)) {
                mListOfSameWeek.add(expenseList.get(i));
            }
        }
    }

    private void generateListOfSameMonth(String date) {
        mListOfSameMonth = new ArrayList<>();

        List<Expense> expenseList = mDataModel.getExpenses();
        for (int i = 0; i < expenseList.size(); i++) {
            if (expenseList.get(i).getMonth().equals(date)) {
                mListOfSameMonth.add(expenseList.get(i));
            }
        }

    }

    private void sortListOfSameCategory(String category) {
        generateListOfSameCategory(category);
        int n = mListOfSameCategory.size();

        if (n > 0) {
            for (int i = 0; i < n; i++) {
                for (int j = 1; j < n - i; j++) {
                    if (mListOfSameCategory.get(j - 1).getDate().getTime() < mListOfSameCategory.get(j).getDate().getTime()) {
                        // swap data[j] and data[j+1]
                        Expense temp = mListOfSameCategory.get(j - 1);
                        mListOfSameCategory.set(j - 1, mListOfSameCategory.get(j));
                        mListOfSameCategory.set(j, temp);
                    }
                }
            }
        }
    }

    public List<Expense> getListOfSameCategory(String category) {
        sortListOfSameCategory(category);
        return mListOfSameCategory;
    }

    public List<Expense> getListOfSameDay(String date) {
        generateListOfSameDay(date);
        return mListOfSameDay;
    }

    public List<Expense> getmListOfSameWeek(Date date) {
        generateListOfSameWeek(date);
        return mListOfSameWeek;
    }

    public List<Expense> getmListOfSameMonth(String date) {
        generateListOfSameMonth(date);
        return mListOfSameMonth;
    }

    public long getExpenseTotal() {
        return mDataModel.getExpenseTotal();
    }
}
