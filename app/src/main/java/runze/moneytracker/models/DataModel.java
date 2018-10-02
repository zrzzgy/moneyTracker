package runze.moneytracker.models;

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

    public DataModel(List<Expense> expenseList,
                     List<DailyExpenseTotal> dailyTotalList,
                     HashSet<String> categoryList, List<Integer> colorList){
        mExpenseList = expenseList;
        mDailyTotalList = dailyTotalList;
        mCategoryList = categoryList;
        mColorList = colorList;
    }


    public List<DailyExpenseTotal> getDailyTotalList() {
        return mDailyTotalList;
    }

    public void setDailyTotalList(List<DailyExpenseTotal> dailyTotalList) {
        this.mDailyTotalList = dailyTotalList;
    }

    public List<Expense> getExpenseList() {
        return mExpenseList;
    }

    public void setExpenseList(List<Expense> expenseList) {
        this.mExpenseList = expenseList;
    }

    public HashSet<String> getCategoryList() {
        return mCategoryList;
    }

    public void setCategoryList(HashSet<String> categoryList) {
        this.mCategoryList = categoryList;
    }

    public List<Integer> getColorList() {
        return mColorList;
    }

    public void setColorList(List<Integer> mColorList) {
        this.mColorList = mColorList;
    }

}
