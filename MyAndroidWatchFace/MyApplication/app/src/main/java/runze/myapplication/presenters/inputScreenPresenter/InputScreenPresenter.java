package runze.myapplication.presenters.inputScreenPresenter;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import runze.myapplication.HomeActivity;
import runze.myapplication.R;
import runze.myapplication.utils.Expense;
import runze.myapplication.views.inputScreenView.IInputScreenView;

import static runze.myapplication.HomeActivity.CATEGORIES_KEY;
import static runze.myapplication.HomeActivity.EXPENSES;

public class InputScreenPresenter implements IInputScreenPresenter {
    private HomeActivity mParentActivity;
    private IInputScreenView mView;
    private Set<String> mCategories;

    public InputScreenPresenter(HomeActivity activity){
            mParentActivity = activity;
            mCategories = mParentActivity.mSharedPreferences.getStringSet(CATEGORIES_KEY, new HashSet<String>());
    }

    public void updateView(){
        ArrayAdapter<String > adapter =
                new ArrayAdapter<>(mParentActivity.getApplicationContext(), R.layout.category_item, new ArrayList<>(mCategories));
        mView.updateSpinner(adapter);
    }

    public void attachView(IInputScreenView view){
        mView = view;
    }

    public void detachView(){
        mView = null;
    }

    public void saveData(String category, double amount){
        if (amount > 0) {
            JSONArray jsonArray;
            Expense newExpense = new Expense(category, amount);
            String savedExpenses = mParentActivity.mSharedPreferences.getString(EXPENSES, "");
            try {
                jsonArray = new JSONArray(savedExpenses);
                jsonArray.put(newExpense);
                mParentActivity.mEditor.putString(EXPENSES, jsonArray.toString()).apply();
            }catch (JSONException e){
                Log.e("", e.toString());
            }
        }else {
            Toast.makeText(mParentActivity.getApplicationContext(), "Amount must be larger than zero", Toast.LENGTH_SHORT).show();
        }
    }
}
