package runze.moneytracker;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.leakcanary.LeakCanary;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

import runze.moneytracker.dependencyinjection.AppComponent;
import runze.moneytracker.dependencyinjection.AppModule;
import runze.moneytracker.dependencyinjection.DaggerAppComponent;
import runze.moneytracker.fragments.InputScreenFragment;
import runze.moneytracker.fragments.SettingsScreenFragment;
import runze.moneytracker.fragments.StatsScreenFragment;
import runze.moneytracker.models.DailyExpenseTotal;
import runze.moneytracker.models.DataModel;
import runze.moneytracker.models.Expense;

public class HomeActivity extends AppCompatActivity{
    private final String TAG = this.getClass().getSimpleName();
    public static final String CATEGORIES_KEY = "CATEGORIES_KEY";
    public static final String EXPENSES_KEY = "EXPENSES_KEY";
    public static final String DAILY_TOTAL_KEY = "DAILY_TOTAL_KEY";
    public static final String DATA_MODEL_KEY = "DATA_MODEL_KEY";
    public static final String EMPTY_DATA_MODEL = "EMPTY_DATA_MODEL";
    private static final String SHARED_PREF_ID = "moneyTrackerPreferenceFile";

    private AppComponent mAppComponent;
    public SharedPreferences mSharedPreferences;
    public SharedPreferences.Editor mEditor;
    public List<Integer> mColorList = new ArrayList<>();

    private FrameLayout mContainer;
    private BottomNavigationView mNavigation;

    @Inject InputScreenFragment mInputFragment;
    @Inject StatsScreenFragment mStatsFragment;
    @Inject SettingsScreenFragment mSettingsFragment;

    public static DataModel mDataModel;

    private Gson gson = new Gson();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_input:
                    mInputFragment.onResume();
                    return true;
                case R.id.navigation_stats:
                    mStatsFragment.onResume();
                    return true;
                case R.id.navigation_settings:
                   mSettingsFragment.onResume();
                   return true;
            }
            return false;
        }
    };


    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (LeakCanary.isInAnalyzerProcess(this)) {  //  create LeakCanary to check memory leak
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(getApplication());


        //fulfill injected objects
        mAppComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
        mAppComponent.inject(this);

        mSharedPreferences = getSharedPreferences(SHARED_PREF_ID, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        //setup dataModel
        loadDataModel();

        setContentView(R.layout.bottom_nav);
        initComponents();

        this.getLayoutInflater().inflate((R.layout.home_base), null);
    }

    @Override
    public void onAttachFragment(android.app.Fragment fragment) {
        super.onAttachFragment(fragment);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveDataModel();

    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    public void initComponents(){
        mContainer = findViewById(R.id.container);
        
        mNavigation = findViewById(R.id.navigation);
        mNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
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
                    Snackbar.make(mContainer, getResources().getString(R.string.snack_bar_message), Snackbar.LENGTH_LONG)
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
                    Snackbar.make(mContainer, getResources().getString(R.string.snack_bar_message), Snackbar.LENGTH_LONG)
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
           finish();
    }

    protected Fragment getCurrentFragment() {
        Fragment fragment = null;
        FragmentManager fm = getSupportFragmentManager();
        if (fm != null && fm.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry backEntry = fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1);
            String fragmentTag = backEntry.getName();
            fragment = getSupportFragmentManager().findFragmentByTag(fragmentTag);
        }
        return fragment;
    }

    private void undoRemoveCategory(){
        ((SettingsScreenFragment) getCurrentFragment()).getPresenter().undoRemoveCategory();
    }

    public AppComponent getAppComponent(){
        return mAppComponent;
    }

    private void saveDataModel(){
        mEditor.putString(DATA_MODEL_KEY, gson.toJson(mDataModel)).apply();
        String dataModel = mSharedPreferences.getString(DATA_MODEL_KEY, EMPTY_DATA_MODEL);
        Log.v(TAG, dataModel);
    }

    private void loadDataModel(){
        mDataModel = new DataModel(new ArrayList<Expense>(), new ArrayList<DailyExpenseTotal>(), new HashSet<String>());

        //read saved data from preferences
        String dataModel = mSharedPreferences.getString(DATA_MODEL_KEY, EMPTY_DATA_MODEL);

        //if there is saved data, parse it from gson to list
        if (!dataModel.equals(EMPTY_DATA_MODEL)){
            mDataModel = gson.fromJson(dataModel, new TypeToken<DataModel>(){}.getType());
        }
    }
}
