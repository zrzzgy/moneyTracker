package runze.moneytracker.presenters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import runze.moneytracker.models.DataModel;
import runze.moneytracker.models.Expense;
import runze.moneytracker.models.UnsyncedExpense;
import runze.moneytracker.views.IView;
import runze.moneytracker.views.MainView;

public class MainScreenPresenter implements IPresenter {
    private final String TAG = this.getClass().getSimpleName();

    private MainView mView;
    private DataModel mDataModel;
    private HashSet<String> mCategories;
    private List<Expense> mExpenses;
    private List<Expense> mBackupExpenses = new ArrayList<>();
    private List<UnsyncedExpense> mUnsyncedExpenseList;

    public MainScreenPresenter(DataModel dataModel, List<UnsyncedExpense> unsyncedExpenseList) {
        mDataModel = dataModel;
        mCategories = dataModel.getCategories();
        mExpenses = dataModel.getExpenses();
        mUnsyncedExpenseList = unsyncedExpenseList;
    }

    @Override
    public void attachView(IView view) {
        mView = (MainView) view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void updateView() {
        mView.updateView();
    }

    public void saveData(String categoryString, double amount, String description, Date date) {
        HashSet<String> categories = new HashSet<>(Arrays.asList(categoryString.split(", ")));

        //create new Expense object based on data given
        Expense newExpense = new Expense(categories, amount, date, description);

        //put in new data
        mExpenses.add(newExpense);
        mCategories.addAll(categories);
        mUnsyncedExpenseList.add(new UnsyncedExpense(newExpense, true));

        updateModel();
    }

    public boolean removeExpense(Expense itemToDelete) {
        boolean result = false;
        mBackupExpenses.clear();
        mBackupExpenses.addAll(mExpenses);

        Iterator<Expense> iterator = mExpenses.iterator();
        while (iterator.hasNext()) {
            Expense expense = iterator.next();
            if (expense.isSameExpense(itemToDelete)) {
                mExpenses.remove(expense);
                mUnsyncedExpenseList.add(new UnsyncedExpense(expense, false));
                result = true;
                break;
            }
        }

        updateModel();
        return result;
    }

    public void restoreDeletedItem() {
        mExpenses = mBackupExpenses;
        updateModel();
    }

    public long calculateTotal() {
        long total = 0;
        for (Expense expense : mExpenses) {
            total += expense.getAmount();
        }
        return total;
    }

    public HashSet<String> getCategories() {
        mCategories = mDataModel.getCategories();
        return mCategories;
    }

    public List<Expense> sortExpenseetByDate() {
        int n = mExpenses.size();

        if (n > 0) {
            for (int i = 0; i < n; i++) {
                for (int j = 1; j < n - i; j++) {
                    if (mExpenses.get(j - 1).getDate().getTime() < mExpenses.get(j).getDate().getTime()) {
                        // swap data[j] and data[j+1]
                        Expense temp = mExpenses.get(j - 1);
                        mExpenses.set(j - 1, mExpenses.get(j));
                        mExpenses.set(j, temp);
                    }
                }
            }
        }
        return mExpenses;
    }

    public List<Expense> getExpenses() {
        mExpenses = mDataModel.getExpenses();
        return sortExpenseetByDate();
    }

    private void updateModel() {
        mDataModel.setExpenseList(mExpenses);
        mDataModel.setCategoryList(mCategories);

    }
}
