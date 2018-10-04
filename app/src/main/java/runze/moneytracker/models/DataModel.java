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

    public HashSet<String> getCategories() {
        return mCategoryList;
    }

    public List<Integer> getColorList() {
        return mColorList;
    }

    public void setDailyTotalList(List<DailyExpenseTotal> mDailyTotalList) {
        this.mDailyTotalList = mDailyTotalList;
    }

    public void setExpenseList(List<Expense> mExpenseList) {
        this.mExpenseList = mExpenseList;
    }

    public void setCategoryList(HashSet<String> mCategoryList) {
        this.mCategoryList = mCategoryList;
    }

    public void setColorList(List<Integer> mColorList) {
        this.mColorList = mColorList;
    }

}
