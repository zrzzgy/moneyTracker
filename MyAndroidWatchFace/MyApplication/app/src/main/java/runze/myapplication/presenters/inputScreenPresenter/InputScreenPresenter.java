package runze.myapplication.presenters.inputScreenPresenter;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import runze.myapplication.HomeActivity;
import runze.myapplication.R;
import runze.myapplication.utils.Expense;
import runze.myapplication.views.inputScreenView.IInputScreenView;

import static runze.myapplication.HomeActivity.CATEGORIES_KEY;
import static runze.myapplication.HomeActivity.EXPENSES;

public class InputScreenPresenter implements IInputScreenPresenter {
    private final String TAG = this.getClass().getSimpleName();

    private HomeActivity mParentActivity;
    private IInputScreenView mView;
    private Set<String> mCategories;

    public InputScreenPresenter(HomeActivity activity){
            mParentActivity = activity;
            String stringCategories = mParentActivity.mSharedPreferences.getString(CATEGORIES_KEY, "");
            Gson gson = new Gson();
            mCategories = gson.fromJson(stringCategories, new TypeToken<Set<String>>(){}.getType());
            if (mCategories == null){
                mCategories = new HashSet<>();
            }
    }

    public void updateView(int index){
        Log.d(TAG, "categories are: " + mCategories.toString());
        ArrayAdapter<String > adapter =
                new ArrayAdapter<>(mParentActivity.getApplicationContext(), R.layout.category_item, new ArrayList<>(mCategories));
        mView.updateSpinner(adapter);
        mView.setSpinnerIndex(index);
    }

    public void attachView(IInputScreenView view){
        mView = view;
    }

    public void detachView(){
        mView = null;
    }

    public void saveData(String category, double amount){
        if (amount > 0) {
            List<Expense> expenseList = new ArrayList<>();
            Gson gson = new Gson();

            //create new Expense object based on data given
            Expense newExpense = new Expense(category, amount);

            //read saved data from preferences
            String savedExpenses = mParentActivity.mSharedPreferences.getString(EXPENSES, "");

            //if there is saved data, put it in first
            if (!savedExpenses.equals("")){
                List<Expense> oldExpenseList = gson.fromJson(savedExpenses, new TypeToken<List<Expense>>(){}.getType());
                expenseList.addAll(oldExpenseList);
            }

            //put in new data
            expenseList.add(newExpense);

            //save edited data
            if (mParentActivity.mEditor.putString(EXPENSES, gson.toJson(expenseList)).commit()) {
                Toast.makeText(mParentActivity.getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mParentActivity.getApplicationContext(), "Save Failed", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(mParentActivity.getApplicationContext(), "Amount must be larger than zero", Toast.LENGTH_SHORT).show();
        }
    }
}
