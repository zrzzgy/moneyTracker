package runze.moneytracker.presenters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import runze.moneytracker.HomeActivity;
import runze.moneytracker.models.DataModel;
import runze.moneytracker.models.Expense;
import runze.moneytracker.views.IView;
import runze.moneytracker.views.InputScreenView;

import static runze.moneytracker.HomeActivity.CATEGORIES_KEY;
import static runze.moneytracker.HomeActivity.EXPENSES_KEY;

public class InputScreenPresenter implements IPresenter {
    private final String TAG = this.getClass().getSimpleName();

    private HomeActivity mParentActivity;
    private InputScreenView mView;
    private Set<String> mCategories;
    private List<Expense> mBackupExpenses = new ArrayList<>();
    private Gson gson = new Gson();

    public InputScreenPresenter(HomeActivity activity){
            mParentActivity = activity;
            String stringCategories = mParentActivity.mSharedPreferences.getString(CATEGORIES_KEY, "");
            mCategories = gson.fromJson(stringCategories, new TypeToken<Set<String>>(){}.getType());
            if (mCategories == null){
                mCategories = new HashSet<>();
            }
    }

    @Override
    public void attachView(IView view){
        mView = (InputScreenView) view;
    }

    @Override
    public void detachView(){
        mView = null;
    }

    @Override
    public void updateView() {

    }

    public void saveData(String categoryString, double amount, String description, Date date){
        HashSet<String> categories = new HashSet<>(Arrays.asList(categoryString.split(", ")));

        //create new Expense object based on data given
        Expense newExpense = new Expense(categories, amount, date, description);

        //read saved data from preferences, and if there is saved data, put it in first
        List<Expense> expenseList = new ArrayList<>(HomeActivity.mDataModel.getExpenseList());

        //put in new data
        expenseList.add(newExpense);
        HomeActivity.mDataModel.setExpenseList(expenseList);

        HashSet<String> categoryList = HomeActivity.mDataModel.getCategoryList();
        categoryList.addAll(categories);
        HomeActivity.mDataModel.setCategoryList(categoryList);

    }

    public List<Expense> loadExpensesFromDataModel(){
        return HomeActivity.mDataModel.getExpenseList();
    }

    public HashSet<String> loadCategoriesFromDataModel(){
        return HomeActivity.mDataModel.getCategoryList();
    }

    public boolean removeExpenseFromDataModel(Expense itemToDelete){
        boolean result = false;
        List<Expense> expenses = HomeActivity.mDataModel.getExpenseList();
        mBackupExpenses.clear();
        mBackupExpenses.addAll(expenses);

        for (int i = 0; i < expenses.size(); i++) {
            Expense expense = expenses.get(i);
            if (expense.isSameExpense(itemToDelete)) {
                expenses.remove(i);
                HomeActivity.mDataModel.setExpenseList(expenses);
                result = true;
            }
        }

        return result;
    }

    public boolean restoreDeletedItem(){
        return mParentActivity.mEditor.putString(EXPENSES_KEY, gson.toJson(mBackupExpenses)).commit();
    }

    public long calculateTotal() {
        long total = 0;
        List<Expense> list = loadExpensesFromDataModel();
        for (Expense expense:list) {
            total += expense.getAmount();
        }
        return total;
    }
}
