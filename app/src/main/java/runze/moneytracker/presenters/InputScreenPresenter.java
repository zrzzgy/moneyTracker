package runze.moneytracker.presenters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import runze.moneytracker.dependencyinjection.AppComponent;
import runze.moneytracker.models.DataModel;
import runze.moneytracker.models.Expense;
import runze.moneytracker.views.IView;
import runze.moneytracker.views.InputScreenView;

public class InputScreenPresenter implements IPresenter {
    private final String TAG = this.getClass().getSimpleName();

    private InputScreenView mView;
    @Inject DataModel mDataModel;
    private HashSet<String> mCategories;
    private List<Expense> mExpenses;
    private List<Expense> mBackupExpenses = new ArrayList<>();

    public InputScreenPresenter(AppComponent appComponent) {
        appComponent.inject(this);
        mCategories = mDataModel.getCategories();
        mExpenses = mDataModel.getExpenses();
    }

    @Override
    public void attachView(IView view) {
        mView = (InputScreenView) view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void updateView() {

    }

    public void saveData(String categoryString, double amount, String description, Date date) {
        HashSet<String> categories = new HashSet<>(Arrays.asList(categoryString.split(", ")));

        //create new Expense object based on data given
        Expense newExpense = new Expense(categories, amount, date, description);

        //put in new data
        mExpenses.add(newExpense);
        mCategories.addAll(categories);

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
        return mCategories;
    }

    public List<Expense> getExpenses() {
        return mExpenses;
    }

    private void updateModel(){
        mDataModel.setExpenseList(mExpenses);
        mDataModel.setCategoryList(mCategories);
    }
}
