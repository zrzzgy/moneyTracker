package runze.moneytracker.models;

import android.support.annotation.NonNull;

import java.util.HashSet;
import java.util.List;

/**
 * Model class that is saved to the Shared Preferences. Containing all data used in the app
 */
public class DataModel {
    private List<DailyExpenseTotal> mDailyTotalList;
    private List<Expense> mExpenseList;
    private HashSet<String> mCategoryList;
    private List<Integer> mColorList;

    public DataModel(@NonNull List<Expense> expenseList,
                     @NonNull List<DailyExpenseTotal> dailyTotalList,
                     @NonNull HashSet<String> categoryList,
                     @NonNull List<Integer> colorList){
        mExpenseList = expenseList;
        mDailyTotalList = dailyTotalList;
        mCategoryList = categoryList;
        mColorList = colorList;
    }

    public List<DailyExpenseTotal> getDailyTotals() {
        return mDailyTotalList;
    }

    public List<Expense> getExpenses() {
        return mExpenseList;
    }

    public long getExpenseTotal() {
        long total = 0;
        for (Expense expense : mExpenseList) {
            total += expense.getAmount();
        }
        return total;
    }

    public HashSet<String> getCategories() {
        return mCategoryList;
    }

    public List<Integer> getColorList() {
        return mColorList;
    }

    public void setDailyTotalList(List<DailyExpenseTotal> dailyTotalList) {
        this.mDailyTotalList = dailyTotalList;
    }

    public void setExpenseList(List<Expense> expenseList) {
        this.mExpenseList = expenseList;
    }

    public void setCategoryList(HashSet<String> categoryList) {
        this.mCategoryList = categoryList;
    }

    public void setColorList(List<Integer> colorList) {
        this.mColorList = colorList;
    }

}
