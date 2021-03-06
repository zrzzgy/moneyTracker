package runze.moneytracker;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.Toast;

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

    private boolean mIsLoggedIn = false;
    private boolean mIsConnectedToInternet = false;

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
                    Log.v(TAG, "navigate main screen");
                    fragmentTransaction.replace(R.id.fragment_container, mInputFragment, "inputFragment");
                    fragmentTransaction.addToBackStack("inputFragment");
                    break;
                case R.id.navigation_stats:
                    Log.v(TAG, "navigate stats screen");
                    fragmentTransaction.replace(R.id.fragment_container, mStatsFragment, "statsFragment");
                    fragmentTransaction.addToBackStack("statsFragment");
                    break;
                case R.id.navigation_settings:
                    Log.v(TAG, "navigate settings screen");
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
                        new TypeToken<List<UnsyncedExpense>>(){}.getType());
        if (mUnsyncedExpenseList.isEmpty() &&
                cachedUnsyncedChanges != null) {
            for (int i = 0; i < cachedUnsyncedChanges.size(); i++) {
                if (cachedUnsyncedChanges.get(i) != null) {
                    mUnsyncedExpenseList.add(cachedUnsyncedChanges.get(i));
                }
            }
        }

        mIsLoggedIn = getIntent().hasExtra("userName");

        if (mIsLoggedIn) {
            user = getIntent().getParcelableExtra("userName");
            getIntent().removeExtra("userName");
            database = FirebaseDatabase.getInstance();

            uploadExpenses();
            DatabaseReference myRef = database.getReference(user.getUid());
            // Read from the database
            myRef.addValueEventListener(this);

            // Handles lost Internet connection if logged in the first place
            DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
            connectedRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean connected = snapshot.getValue(Boolean.class);
                    if (connected) {
                        Log.d(TAG, "Internet connected");
                        mIsConnectedToInternet = true;
                        persistDataAndUpload();
                    } else {
                        mIsConnectedToInternet = false;
                        Log.d(TAG, "Internet not connected");

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w(TAG, "Listener was cancelled");
                }
            });
        } else {
            Log.v(TAG, "load from pref");
            populateDataModel(((DataModel) gson.fromJson(mSharedPreferences.getString(DATA_MODEL_KEY, ""),
                    new TypeToken<DataModel>() {
                    }.getType())).getExpenses());
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mInputFragment != null) {
                        mInputFragment.updateView();
                    }
                }
            }, 500);

        }

        setContentView(R.layout.base_layout);  // setContentView() to display a view
        initComponents();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mInputFragment).commit();
    }

    public void initComponents() {
        mNavigation = findViewById(R.id.navigation);
        mNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
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

    private void persistDataModel() {
        String userData = gson.toJson(mDataModel);
        mEditor.putString(DATA_MODEL_KEY, userData).apply();
        String unsyncedExpenseList = gson.toJson(mUnsyncedExpenseList);
        mEditor.putString(UNSYNCED_EXPENSE_KEY, unsyncedExpenseList).apply();
    }

    private void uploadExpenses() {
        // Write a message to the database
        final DatabaseReference myRef = database.getReference(user.getUid());
        String unsyncedExpenseList = "";
        for (final UnsyncedExpense tempUnsyncedExpense : mUnsyncedExpenseList) {
            if (tempUnsyncedExpense.getExpense().getChildId() == null || !tempUnsyncedExpense.isAdd()) {
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

            unsyncedExpenseList = gson.toJson(mUnsyncedExpenseList);
        }

        mEditor.putString(UNSYNCED_EXPENSE_KEY, unsyncedExpenseList).apply();
    }

    public void persistDataAndUpload() {
        if (mIsLoggedIn && mIsConnectedToInternet) {
            new UploadFileTask().execute();
        }
        persistDataModel();
    }

    private void populateDataModel(List<Expense> databaseExpenseList) {
        HashSet<String> category = new HashSet<>();
        if (!databaseExpenseList.isEmpty()) {
            for (Expense aDatabaseExpenseList : databaseExpenseList) {
                category.addAll(aDatabaseExpenseList.getCategory());
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
        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
            userExpenseDataAsString = dataSnapshot1.getValue(String.class);
            Expense tempExpense = gson.fromJson(userExpenseDataAsString,
                    new TypeToken<Expense>() {
                    }.getType());
            loadFromDatabaseExpenseList.add(tempExpense);
        }
        //setup dataModel
        Log.d(TAG, "Value is: " + userExpenseDataAsString);
        populateDataModel(loadFromDatabaseExpenseList);

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

    private class UploadFileTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            uploadExpenses();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(getApplicationContext(), "sync successful", Toast.LENGTH_SHORT).show();
        }
    }
}
