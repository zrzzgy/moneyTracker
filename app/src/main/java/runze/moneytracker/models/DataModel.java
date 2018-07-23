package runze.moneytracker.models;

import java.util.HashSet;
import java.util.List;

public class DataModel {
    private List<DailyExpenseTotal> mDailyTotalList;
    private List<Expense> mExpenseList;
    private HashSet<String> mCategoryList;

    public DataModel(List<Expense> expenseList,
                     List<DailyExpenseTotal> dailyTotalList,
                     HashSet<String> categoryList){
        mExpenseList = expenseList;
        mDailyTotalList = dailyTotalList;
        mCategoryList = categoryList;
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
}
