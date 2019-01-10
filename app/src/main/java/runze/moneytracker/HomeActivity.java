package runze.moneytracker;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import runze.moneytracker.dependencyinjection.AppComponent;
import runze.moneytracker.dependencyinjection.AppModule;
import runze.moneytracker.dependencyinjection.DaggerAppComponent;
import runze.moneytracker.fragments.AboutFragment;
import runze.moneytracker.fragments.ExpenseAnalysisFragment;
import runze.moneytracker.fragments.MainScreenFragment;
import runze.moneytracker.fragments.SettingsScreenFragment;
import runze.moneytracker.models.DataModel;
import runze.moneytracker.models.Expense;
import runze.moneytracker.models.UnsyncedExpense;
import runze.moneytracker.views.AboutView;

/**
 * Home Activity
 */
public class HomeActivity extends AppCompatActivity implements ValueEventListener {
    private final String TAG = this.getClass().getSimpleName();
    public static final String DATA_MODEL_KEY = "DATA_MODEL_KEY";
    public static final String UNSYNCED_EXPENSE_KEY = "UNSYNCED_EXPENSE_KEY";
    private static final String SHARED_PREF_ID = "moneyTrackerPreferenceFile";


    private AppComponent mAppComponent;
    public SharedPreferences mSharedPreferences;
    public SharedPreferences.Editor mEditor;
    private BottomNavigationView mNavigation;
    private FirebaseDatabase database;

    private String userExpenseDataAsString;
    private FirebaseUser user;

    @Inject
    List<UnsyncedExpense> mUnsyncedExpenseList;

    @Inject
    MainScreenFragment mInputFragment;
    @Inject
    ExpenseAnalysisFragment mStatsFragment;
    @Inject
    SettingsScreenFragment mSettingsFragment;
    @Inject
    DataModel mDataModel;

    private Gson gson = new Gson();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_input:
                    fragmentTransaction.replace(R.id.fragment_container, mInputFragment, "inputFragment");
                    fragmentTransaction.addToBackStack("inputFragment");
                    break;
                case R.id.navigation_stats:
                    fragmentTransaction.replace(R.id.fragment_container, mStatsFragment, "statsFragment");
                    fragmentTransaction.addToBackStack("statsFragment");
                    break;
                case R.id.navigation_settings:
                    fragmentTransaction.replace(R.id.fragment_container, mSettingsFragment, "settingsFragment");
                    fragmentTransaction.addToBackStack("settingsFragment");
                    break;
            }
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.commit();
            return true;
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

        List<UnsyncedExpense> cachedUnsyncedChanges =
                gson.fromJson(mSharedPreferences.getString(UNSYNCED_EXPENSE_KEY, ""),
                        new TypeToken<List>() {
                        }.getType());
        if (mUnsyncedExpenseList.isEmpty() &&
                cachedUnsyncedChanges != null) {
            for (int i = 0; i < cachedUnsyncedChanges.size(); i++) {
                if(cachedUnsyncedChanges.get(i) instanceof UnsyncedExpense) {
                    mUnsyncedExpenseList.add(cachedUnsyncedChanges.get(i));
                }
            }
        }

      //  if (getIntent().hasExtra("userName")) {
            user = getIntent().getParcelableExtra("userName");
            database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true);


        DatabaseReference myRef = database.getReference(user.getUid());
            // Read from the database
            myRef.addValueEventListener(this);
       // }

        setContentView(R.layout.base_layout);  // setContentView() to display a view
        initComponents();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mInputFragment).commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveDataModel();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void initComponents() {
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
    public void onBackPressed() {
        if (getCurrentFragment() instanceof AboutFragment) {
            getSupportFragmentManager().popBackStackImmediate();
        } else {
            finish();  // kill current activity
        }
    }

    public Fragment getCurrentFragment() {
        Fragment fragment = null;
        FragmentManager fm = getSupportFragmentManager();
        if (fm != null && fm.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry backEntry = fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1);
            String fragmentTag = backEntry.getName();
            fragment = fm.findFragmentByTag(fragmentTag);
        }
        return fragment;
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    public void saveDataModel() {
        String userData = gson.toJson(mDataModel);
        mEditor.putString(DATA_MODEL_KEY, userData).apply();

        // Write a message to the database
        final DatabaseReference myRef = database.getReference(user.getUid());

        Iterator<UnsyncedExpense> iterator = mUnsyncedExpenseList.iterator();
        while (iterator.hasNext()) {
            final UnsyncedExpense tempUnsyncedExpense = iterator.next();
            if (tempUnsyncedExpense.getExpense().getChildId() != null) {
                if (tempUnsyncedExpense.isAdd()) {
                    final DatabaseReference newChild = myRef.push();
                    tempUnsyncedExpense.getExpense().setChildId(newChild.getKey());
                    String unsyncedExpenseString = gson.toJson(tempUnsyncedExpense.getExpense());
                    newChild.setValue(unsyncedExpenseString, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                mUnsyncedExpenseList.remove(tempUnsyncedExpense);
                                String unsyncedExpenseList = gson.toJson(mUnsyncedExpenseList);
                                mEditor.putString(UNSYNCED_EXPENSE_KEY, unsyncedExpenseList).apply();
                            }
                        }
                    });
                } else {
                    // remove expense from database
                    myRef.child(tempUnsyncedExpense.getExpense().getChildId()).removeValue(new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                mUnsyncedExpenseList.remove(tempUnsyncedExpense);
                                String unsyncedExpenseList = gson.toJson(mUnsyncedExpenseList);
                                mEditor.putString(UNSYNCED_EXPENSE_KEY, unsyncedExpenseList).apply();
                            }
                        }
                    });
                }
            }
            String unsyncedExpenseList = gson.toJson(mUnsyncedExpenseList);
            mEditor.putString(UNSYNCED_EXPENSE_KEY, unsyncedExpenseList).apply();
        }
    }

    private void loadDataModel(List<Expense> databaseExpenseList) {
        HashSet<String> category = new HashSet<>();
        if (!databaseExpenseList.isEmpty()) {
            Iterator<Expense> iterator = databaseExpenseList.iterator();
            while (iterator.hasNext()) {
                category.addAll(iterator.next().getCategory());
            }
            mDataModel.setCategoryList(category);
            mDataModel.setExpenseList(databaseExpenseList);
        }

    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        List<Expense> loadFromDatabaseExpenseList = new ArrayList<>();
        // This method is called once with the initial value and again
        // whenever data at this location is updated.
        Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
        while (iterator.hasNext()) {
            userExpenseDataAsString = iterator.next().getValue(String.class);
            Expense tempExpense = gson.fromJson(userExpenseDataAsString,
                    new TypeToken<Expense>() {}.getType());
            loadFromDatabaseExpenseList.add(tempExpense);
        }
        //setup dataModel
        Log.d(TAG, "Value is: " + userExpenseDataAsString);
        loadDataModel(loadFromDatabaseExpenseList);
        if (mInputFragment != null) {
            mInputFragment.updateView();
        }
        if (mStatsFragment != null) {
            mStatsFragment.updateModel(mDataModel);
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        // Failed to read value
        Log.w(TAG, "Failed to read value.", databaseError.toException());
    }
}
