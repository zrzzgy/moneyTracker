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
import java.util.Iterator;
import java.util.List;

import runze.moneytracker.HomeActivity;
import runze.moneytracker.R;
import runze.moneytracker.utils.Expense;
import runze.moneytracker.views.IView;
import runze.moneytracker.views.SettingsScreenView;

import static runze.moneytracker.HomeActivity.CATEGORIES_KEY;
import static runze.moneytracker.HomeActivity.EXPENSES;


public class SettingsScreenPresenter implements IPresenter{
    private final String TAG = this.getClass().getSimpleName();

    private HomeActivity mParentActivity;
    private SettingsScreenView mView;
    private List<String> mCategories;
    private Gson gson = new Gson();

    private List<String> backupCategories;
    private List<Expense> backupExpenses;


    public SettingsScreenPresenter(HomeActivity activity){
        mParentActivity = activity;
        String stringCategories = mParentActivity.mSharedPreferences.getString(CATEGORIES_KEY, "");
        Gson gson = new Gson();
        mCategories = gson.fromJson(stringCategories, new TypeToken<List<String>>(){}.getType());
        if (mCategories == null){
            mCategories = new ArrayList<>();
        }
        backupCategories = new ArrayList<>();
        backupExpenses = new ArrayList<>();
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

    public void editCategory(MenuItem item){
        final EditText input = new EditText(mParentActivity);
        int position = ((AdapterView.AdapterContextMenuInfo)item.getMenuInfo()).position;
        final String cate = mCategories.get(position);
        input.setText(cate);
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
                replaceCategory(cate, input.getText().toString());
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    public void removeCategory(MenuItem item){
        int position = ((AdapterView.AdapterContextMenuInfo)item.getMenuInfo()).position;
        String cate = mCategories.get(position);
        backupCategories.clear();
        backupCategories.addAll(mCategories);
        mCategories.remove(position);
        mParentActivity.mEditor.putString(CATEGORIES_KEY, gson.toJson(mCategories)).apply();
        List<Expense> expenses = mParentActivity.loadData();
        backupExpenses.clear();
        backupExpenses.addAll(expenses);
        Iterator<Expense> expenseIterator = expenses.iterator();
        while(expenseIterator.hasNext()){
            Expense expense = expenseIterator.next();
            if (expense.getCategory().equals(cate)){
                expenseIterator.remove();
            }
        }
        mParentActivity.mEditor.putString(EXPENSES, gson.toJson(expenses)).apply();
        updateView();
    }

    public void undoRemoveCategory(){
        mCategories.clear();
        mCategories.addAll(backupCategories);
        mParentActivity.mEditor.putString(CATEGORIES_KEY, gson.toJson(mCategories)).apply();
        mParentActivity.mEditor.putString(EXPENSES, gson.toJson(backupExpenses)).apply();
        updateView();
    }

    private void replaceCategory(String oldCategory, String newCategory){
        mCategories.set(mCategories.indexOf(oldCategory), newCategory);
        mParentActivity.mEditor.putString(CATEGORIES_KEY, gson.toJson(mCategories)).apply();

        List<Expense> expenses = mParentActivity.loadData();
        for (Expense expense : expenses) {
            if (expense.getCategory().equals(oldCategory)) {
                expense.setCategory(newCategory);
            }
        }
        mParentActivity.mEditor.putString(EXPENSES, gson.toJson(expenses)).apply();
        updateView();
    }
}
