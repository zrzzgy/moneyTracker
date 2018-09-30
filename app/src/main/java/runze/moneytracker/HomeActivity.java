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
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

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
import runze.moneytracker.utils.MTFragmentPagerAdapter;

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

    private BottomNavigationView mNavigation;
    private ViewPager mViewPager;

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
                    mViewPager.setCurrentItem(0);  // display page 0
                    mInputFragment.onResume();
                    return true;
                case R.id.navigation_stats:
                    mViewPager.setCurrentItem(1);   // display page 1
                    mStatsFragment.onResume();
                    return true;
                case R.id.navigation_settings:
                   mViewPager.setCurrentItem(2);    // display page 2
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
        mAppComponent.inject(this);   // used to initialize dependencies

        mSharedPreferences = getSharedPreferences(SHARED_PREF_ID, Context.MODE_PRIVATE);  // BaseActivity.getSharedPreferences()
        mEditor = mSharedPreferences.edit();

        //setup dataModel
        loadDataModel();

        setContentView(R.layout.bottom_nav);  // setContentView() to display a view
        initComponents();

        this.getLayoutInflater().inflate((R.layout.home_base), null);
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
        MTFragmentPagerAdapter mFragmentPagerAdapter = new MTFragmentPagerAdapter(getSupportFragmentManager());
        mFragmentPagerAdapter.addFragment(mInputFragment);
        mFragmentPagerAdapter.addFragment(mStatsFragment);
        mFragmentPagerAdapter.addFragment(mSettingsFragment);

        mViewPager = findViewById(R.id.home_view_pager);
        mViewPager.setAdapter(mFragmentPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.v(TAG, "Page " + position + " selected");
                for (int i = 0; i < 3; i++) {
                    if (i == position) {
                        mNavigation.getMenu().getItem(position).setChecked(true);
                    }else {
                        mNavigation.getMenu().getItem(position).setChecked(false);
                    }
                }

                switch (position) {
                    case 0:
                        mInputFragment.onResume();
                        break;
                    case 1:
                        mStatsFragment.onResume();
                        break;
                    case 2:
                        mSettingsFragment.onResume();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setCurrentItem(0);

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
                    Snackbar.make(mViewPager, getResources().getString(R.string.snack_bar_message), Snackbar.LENGTH_LONG)
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
                    Snackbar.make(mViewPager, getResources().getString(R.string.snack_bar_message), Snackbar.LENGTH_LONG)
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
    public void onBackPressed(){   // for the return key
           finish(); // end this activity
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
