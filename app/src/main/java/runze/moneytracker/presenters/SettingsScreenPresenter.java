package runze.moneytracker.presenters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import runze.moneytracker.HomeActivity;
import runze.moneytracker.R;
import runze.moneytracker.models.DataModel;
import runze.moneytracker.models.Expense;
import runze.moneytracker.views.IView;
import runze.moneytracker.views.SettingsScreenView;

import static runze.moneytracker.HomeActivity.CATEGORIES_KEY;
import static runze.moneytracker.HomeActivity.EXPENSES_KEY;


public class SettingsScreenPresenter implements IPresenter{
    private final String TAG = this.getClass().getSimpleName();

    private HomeActivity mParentActivity;
    private SettingsScreenView mView;
    private HashSet<String> mCategories;
    private Gson gson = new Gson();

    private HashSet<String> backupCategories;
    private List<Expense> backupExpenses;


    public SettingsScreenPresenter(HomeActivity activity){
        mParentActivity = activity;
        mCategories = HomeActivity.mDataModel.getCategoryList();

        if (mCategories == null){
            mCategories = new HashSet<>();
        }
        backupCategories = new HashSet<>();
        backupExpenses = new ArrayList<>();
    }

    public void updateView(){
        mCategories = HomeActivity.mDataModel.getCategoryList();
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
        Log.v(TAG, "Save new category: " + newCategory);
        if (newCategory.isEmpty()){
            Toast.makeText(mParentActivity.getApplicationContext(), mParentActivity.getResources().getString(R.string.no_category_entered), Toast.LENGTH_SHORT).show();
        }else if(mCategories.contains(newCategory)){
            Toast.makeText(mParentActivity.getApplicationContext(), mParentActivity.getResources().getString(R.string.category_already_exists), Toast.LENGTH_SHORT).show();
        } else{
            mCategories.add(newCategory);
            if (mParentActivity.mEditor.putString(CATEGORIES_KEY, gson.toJson(mCategories)).commit()){
                Toast.makeText(mParentActivity.getApplicationContext(), mParentActivity.getResources().getString(R.string.saved), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mParentActivity.getApplicationContext(), mParentActivity.getResources().getString(R.string.save_failed), Toast.LENGTH_SHORT).show();
            }
        }
        updateView();
    }

    public void editCategory(final MenuItem item){
        final EditText input = new EditText(mParentActivity);
        input.setText(item.getTitle().toString());
        AlertDialog alertDialog = new AlertDialog.Builder(mParentActivity).create();
        alertDialog.setTitle(R.string.edit);
        alertDialog.setView(input);
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, mParentActivity.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, mParentActivity.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                replaceCategory(item.getTitle().toString(), input.getText().toString());
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    public void removeCategory(MenuItem item){
        backupCategories.clear();
        backupCategories.addAll(mCategories);
        mCategories.remove(item.getTitle().toString());
        HomeActivity.mDataModel.setCategoryList(mCategories);

        List<Expense> expenses = HomeActivity.mDataModel.getExpenseList();
        backupExpenses.clear();
        backupExpenses.addAll(expenses);
        Iterator<Expense> expenseIterator = expenses.iterator();
        while(expenseIterator.hasNext()){
            Expense expense = expenseIterator.next();
            if (expense.getCategory().equals(item.getTitle().toString())){
                expenseIterator.remove();
            }
        }
        mParentActivity.mEditor.putString(EXPENSES_KEY, gson.toJson(expenses)).apply();
        updateView();
    }

    public void undoRemoveCategory(){
        mCategories.clear();
        mCategories.addAll(backupCategories);
        HomeActivity.mDataModel.setCategoryList(mCategories);
        HomeActivity.mDataModel.setExpenseList(backupExpenses);
        updateView();
    }

    private void replaceCategory(String oldCategory, String newCategory){
//        mCategories.remove(oldCategory);
//        mCategories.add(newCategory);
//        mParentActivity.saveToPreferences(CATEGORIES_KEY, mCategories);
//
//        List<Expense> expenses = mParentActivity.loadExpensesFroHomeActivity.mDataModel();
//        for (Expense expense : expenses) {
//            if (expense.getCategory().equals(oldCategory)) {
//                expense.setCategory(newCategory);
//            }
//        }
//        mParentActivity.mEditor.putString(EXPENSES_KEY, gson.toJson(expenses)).apply();
//        updateView();
    }
}
