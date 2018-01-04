package runze.myapplication;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import runze.myapplication.fragments.InputScreenFragment;
import runze.myapplication.fragments.SettingsScreenFragment;
import runze.myapplication.fragments.StatsScreenFragment;
import runze.myapplication.utils.Expense;

public class HomeActivity extends AppCompatActivity {
    public static final String CATEGORIES_KEY = "CATEGORIES_KEY";
    public static final String EXPENSES = "EXPENSES";

    public SharedPreferences mSharedPreferences;
    public SharedPreferences.Editor mEditor;
    public List<Integer> mColorList = new ArrayList<>();

    protected FrameLayout mContentHolder;

    private static final String SHARED_PREF_ID = "moneyTrackerPreferenceFile";
    private final String TAG = this.getClass().getSimpleName();
    private InputScreenFragment mInputFragment;
    private StatsScreenFragment mStatsFragment;
    private SettingsScreenFragment mSettingsFragment;
    private BottomNavigationView mNavigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_input:
                    navigateToFragment(mInputFragment);
                    return true;
                case R.id.navigation_stats:
                    navigateToFragment(mStatsFragment);
                    return true;
                case R.id.navigation_settings:
                    navigateToFragment(mSettingsFragment);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initComponents();
        navigateToFragment(mInputFragment);

        mSharedPreferences = getSharedPreferences(SHARED_PREF_ID, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        logBackStack();
    }

    public void initComponents(){
        setContentView(R.layout.home_activity);
        mContentHolder = findViewById(R.id.home_content_holder);
        mInputFragment = new InputScreenFragment();
        mStatsFragment = new StatsScreenFragment();
        mSettingsFragment = new SettingsScreenFragment();
        mNavigation = findViewById(R.id.navigation);
        mNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void navigateToFragment(Fragment destFragment) {
        FragmentManager fm = getFragmentManager();
        fm.executePendingTransactions();
        FragmentTransaction ft = fm.beginTransaction();
        String fragmentTag = destFragment.getClass().getSimpleName();
        ft.replace(mContentHolder.getId(), destFragment, fragmentTag);

        if (fm.getBackStackEntryCount() > 0){
            if (fm.getBackStackEntryAt(fm.getBackStackEntryCount()-1).toString().equals(fragmentTag)) {
                fm.popBackStackImmediate();
            }
        }
        ft.addToBackStack(fragmentTag);
        ft.commit();
        fm.executePendingTransactions();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        menu.setHeaderTitle(R.string.title_context_menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        Fragment currentFragment = getCurrentFragment();
        if (currentFragment instanceof SettingsScreenFragment){
            switch (item.getItemId()){
                case R.id.option_menu_edit:
                    ((SettingsScreenFragment) currentFragment).getPresenter().editCategory(item);
                    return true;
                case R.id.option_menu_delete:
                    ((SettingsScreenFragment) currentFragment).getPresenter().removeCategory(item);
                    Snackbar.make(mContentHolder, getResources().getString(R.string.snack_bar_message), Snackbar.LENGTH_LONG)
                            .setAction(getResources().getString(R.string.snack_bar_undo), new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                   undoRemoveCategory();
                                }
                            }).show();
                    return true;
            }
        }else if (currentFragment instanceof StatsScreenFragment){
            switch (item.getItemId()){
                case R.id.option_menu_edit:
                    return true;
                case R.id.option_menu_delete:
                    Snackbar.make(mContentHolder, getResources().getString(R.string.snack_bar_message), Snackbar.LENGTH_LONG)
                            .setAction(getResources().getString(R.string.snack_bar_undo), new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            }).show();
                    return true;
            }
        }
        return false;
    }

    @Override
    public void onBackPressed(){
           logBackStack();
           finish();
    }

    private  void logBackStack(){
        FragmentManager fm = getFragmentManager();

        StringBuilder sb = new StringBuilder("Backstack = : ");
        for (int entry = 0; entry < fm.getBackStackEntryCount(); entry++) {
            sb.append(fm.getBackStackEntryAt(entry).getName()).append(", ");
        }
        Log.d(TAG, sb.toString());
    }

    protected Fragment getCurrentFragment() {
        Fragment fragment = null;
        FragmentManager fm = getFragmentManager();
        if (fm != null && fm.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry backEntry = fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1);
            String fragmentTag = backEntry.getName();
            fragment = getFragmentManager().findFragmentByTag(fragmentTag);
        }
        return fragment;
    }

    public List<Expense> loadData(){
        Gson gson = new Gson();
        List<Expense> expenseList = new ArrayList<>();

        //read saved data from preferences
        String savedExpenses = mSharedPreferences.getString(EXPENSES, "");

        //if there is saved data, put it in first
        if (!savedExpenses.equals("")){
            expenseList = gson.fromJson(savedExpenses, new TypeToken<List<Expense>>(){}.getType());
        }
        return  expenseList;
    }

    private void undoRemoveCategory(){
        ((SettingsScreenFragment) getCurrentFragment()).getPresenter().undoRemoveCategory();
    }

    private void undoRemoveExpense(){
        ((StatsScreenFragment) getCurrentFragment()).getPresenter().undoRemoveExpense();
    }
}
