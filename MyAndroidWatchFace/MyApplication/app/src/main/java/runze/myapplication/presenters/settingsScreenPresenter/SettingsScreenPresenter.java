package runze.myapplication.presenters.settingsScreenPresenter;

import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Predicate;

import runze.myapplication.HomeActivity;
import runze.myapplication.R;
import runze.myapplication.utils.Expense;
import runze.myapplication.views.settingsScreenView.ISettingsScreenView;

import static runze.myapplication.HomeActivity.CATEGORIES_KEY;
import static runze.myapplication.HomeActivity.EXPENSES;


public class SettingsScreenPresenter implements ISettingsScreenPresenter {
    private final String TAG = this.getClass().getSimpleName();

    private HomeActivity mParentActivity;
    private ISettingsScreenView mView;
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
    }

    public void updateView(){
        mView.populateListView(mCategories);
    }

    @Override
    public void attachView(ISettingsScreenView view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void saveCategory(String newCategory){
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

    @Override
    public void removeCategory(MenuItem item){
        int position = ((AdapterView.AdapterContextMenuInfo)item.getMenuInfo()).position;
        String cate = mCategories.get(position);
        backupCategories = mCategories;
        mCategories.remove(position);
        mParentActivity.mEditor.putString(CATEGORIES_KEY, gson.toJson(mCategories)).apply();
        List<Expense> expenses = mParentActivity.loadData();
        backupExpenses = expenses;
        Iterator<Expense> expenseIterator = expenses.iterator();
        while(expenseIterator.hasNext()){
            Expense expense = expenseIterator.next();
            if (expense.getmCategory().equals(cate)){
                expenseIterator.remove();
            }
        }
        mParentActivity.mEditor.putString(EXPENSES, gson.toJson(expenses)).apply();
        updateView();
    }

    @Override
    public void undoRemoveCategory(){
        mCategories = backupCategories;
        mParentActivity.mEditor.putString(CATEGORIES_KEY, gson.toJson(mCategories)).apply();
        mParentActivity.mEditor.putString(EXPENSES, gson.toJson(backupExpenses)).apply();
        updateView();
    }
}
