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
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.inject.Inject;

import runze.moneytracker.dependencyinjection.AppComponent;
import runze.moneytracker.dependencyinjection.AppModule;
import runze.moneytracker.dependencyinjection.DaggerAppComponent;
import runze.moneytracker.fragments.ExpenseAnalysisFragment;
import runze.moneytracker.fragments.InputScreenFragment;
import runze.moneytracker.fragments.SettingsScreenFragment;
import runze.moneytracker.models.DataModel;
import runze.moneytracker.utils.MTFragmentPagerAdapter;
import runze.moneytracker.views.SettingsScreenView;

/**
 * Home Activity
 */
public class HomeActivity extends AppCompatActivity implements ValueEventListener{
    private final String TAG = this.getClass().getSimpleName();
    public static final String CATEGORIES_KEY = "CATEGORIES_KEY";
    public static final String EXPENSES_KEY = "EXPENSES_KEY";
    public static final String DATA_MODEL_KEY = "DATA_MODEL_KEY";
    public static final String EMPTY_DATA_MODEL = "EMPTY_DATA_MODEL";
    private static final String SHARED_PREF_ID = "moneyTrackerPreferenceFile";

    private AppComponent mAppComponent;
    public SharedPreferences mSharedPreferences;
    public SharedPreferences.Editor mEditor;
    private BottomNavigationView mNavigation;
    private ViewPager mViewPager;
    private FirebaseDatabase database;

    private String userModelDataAsString;
    private FirebaseUser user;

    @Inject InputScreenFragment mInputFragment;
    @Inject
    ExpenseAnalysisFragment mStatsFragment;
    @Inject SettingsScreenFragment mSettingsFragment;
    @Inject DataModel mDataModel;

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

        //fulfill injected objects
        mAppComponent = DaggerAppComponent.builder().appModule(new AppModule()).build();
        mAppComponent.inject(this);   // used to initialize dependencies

        mSharedPreferences = getSharedPreferences(SHARED_PREF_ID, Context.MODE_PRIVATE);  // BaseActivity.getSharedPreferences()
        mEditor = mSharedPreferences.edit();

        user = getIntent().getParcelableExtra("userName");

        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(user.getUid());
        // Read from the database
        myRef.addValueEventListener(this);

        setContentView(R.layout.bottom_navigation_bar_layout);  // setContentView() to display a view
        initComponents();

        this.getLayoutInflater().inflate((R.layout.home_base_layout), null);
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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        Fragment currentFragment = getCurrentFragment();
        if (currentFragment instanceof SettingsScreenFragment){
            switch (item.getItemId()){
                case R.id.option_menu_edit:
                    ((SettingsScreenView) currentFragment.getView()).showEditDialog(item);
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
        }else if (currentFragment instanceof ExpenseAnalysisFragment){
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
            fragment = fm.findFragmentByTag(fragmentTag);
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
        String userData = gson.toJson(mDataModel);
        mEditor.putString(DATA_MODEL_KEY, userData).apply();
        // Write a message to the database

        DatabaseReference myRef = database.getReference(user.getUid());

        myRef.setValue(userData);

        String dataModel = mSharedPreferences.getString(DATA_MODEL_KEY, EMPTY_DATA_MODEL);
        Log.v(TAG, dataModel);
    }

    private void loadDataModel(){

        //if there is saved data, parse it from gson to list
        if ( userModelDataAsString != null &&
                !userModelDataAsString.equals(EMPTY_DATA_MODEL)){
            DataModel tempModel =  gson.fromJson(userModelDataAsString, new TypeToken<DataModel>(){}.getType());

            // Can't directly set mDataModel = tempModel because this will change the instance of mDataModel
            // causing further update to the model by other presenters not reflected in this mDataModel, and
            // as a result, not saving the correct model.
            mDataModel.setExpenseList(tempModel.getExpenses());
            mDataModel.setDailyTotalList(tempModel.getDailyTotals());
            mDataModel.setCategoryList(tempModel.getCategories());
            mDataModel.setColorList(tempModel.getColorList());

        }
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        // This method is called once with the initial value and again
        // whenever data at this location is updated.
        userModelDataAsString = dataSnapshot.getValue(String.class);
        //setup dataModel
        Log.d(TAG, "Value is: " + userModelDataAsString);
        loadDataModel();
        mInputFragment.updateView();
        mStatsFragment.updateModel(mDataModel);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        // Failed to read value
        Log.w(TAG, "Failed to read value.", databaseError.toException());
    }
}
