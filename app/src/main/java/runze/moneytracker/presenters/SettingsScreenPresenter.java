package runze.moneytracker.presenters;

import android.util.Log;
import android.view.MenuItem;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import runze.moneytracker.HomeActivity;
import runze.moneytracker.models.DataModel;
import runze.moneytracker.models.Expense;
import runze.moneytracker.models.MessageType;
import runze.moneytracker.views.IView;
import runze.moneytracker.views.SettingsScreenView;

import static runze.moneytracker.HomeActivity.CATEGORIES_KEY;
import static runze.moneytracker.HomeActivity.EXPENSES_KEY;


public class SettingsScreenPresenter implements IPresenter{
    private final String TAG = this.getClass().getSimpleName();

    private SettingsScreenView mView;
    private Gson gson = new Gson();

    private HashSet<String> mBackupCategories;
    private List<Expense> mBackupExpenses;
    private HashSet<String> mCategories;
    private List<Expense> mExpenses;
    private DataModel mDataModel;

    public SettingsScreenPresenter(DataModel dataModel){
        mDataModel = dataModel;
        mCategories = dataModel.getCategories();
        mExpenses = dataModel.getExpenses();

        mBackupCategories = new HashSet<>();
        mBackupExpenses = new ArrayList<>();
    }

    public void updateView(){
        mView.populateListView(mCategories);
    }

    @Override
    public void attachView(IView view) {
        mView = (SettingsScreenView) view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    public void saveCategory(String newCategory){
        Log.v(TAG, "Saving new category: " + newCategory);
        if (newCategory.isEmpty()){
            mView.makeToastMsg(MessageType.CATEGORY_EMPTY);
        }else if(mCategories.contains(newCategory)){
            mView.makeToastMsg(MessageType.CATEGORY_EXISTS);
        } else{
            mCategories.add(newCategory);
            mView.makeToastMsg(MessageType.CATEGORY_SAVE_COMPLETE);
        }
        updateModel();
        updateView();
    }

    public void removeCategory(MenuItem item){
        mBackupCategories.clear();
        mBackupCategories.addAll(mCategories);
        mCategories.remove(item.getTitle().toString());

        mBackupExpenses.clear();
        mBackupExpenses.addAll(mExpenses);
        Iterator<Expense> expenseIterator = mExpenses.iterator();
        while(expenseIterator.hasNext()){
            Expense expense = expenseIterator.next();
            if (expense.getCategory().contains(item.getTitle().toString())){
                if (expense.getCategory().size() == 1) {
                    // If this expense has no category after removing
                    // this one, remove the whole expense as well.
                    expenseIterator.remove();
                }else{
                    // If the expense has other categories after removing
                    // this one, don'e remove the expense, just remove the
                    // category from it.
                    expense.getCategory().remove(item.getTitle().toString());
                }
            }
        }
        updateModel();
        updateView();
    }

    public void undoRemoveCategory(){
        mCategories.clear();
        mCategories.addAll(mBackupCategories);
        mExpenses.clear();
        mExpenses.addAll(mBackupExpenses);
        updateModel();
        updateView();
    }

    public void replaceCategory(String oldCategory, String newCategory){
        mCategories.remove(oldCategory);
        mCategories.add(newCategory);

        for (Expense expense : mExpenses) {
            if (expense.getCategory().contains(oldCategory)) {
                expense.getCategory().remove(oldCategory);
                expense.getCategory().add(newCategory);
            }
        }
        updateModel();
        updateView();
    }

    private void updateModel(){
        mDataModel.setExpenseList(mExpenses);
        mDataModel.setCategoryList(mCategories);
    }
}
