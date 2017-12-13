package runze.myapplication;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import runze.myapplication.fragments.InputScreenFragment;
import runze.myapplication.fragments.SettingsScreenFragment;
import runze.myapplication.fragments.StatsScreenFragment;

public class HomeActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    private static final String SHARED_PREF_ID = "moneyTrackerPreferenceFile";
    public SharedPreferences mSharedPreferences;
    public SharedPreferences.Editor mEditor;
    private InputScreenFragment mInputFragment;
    private StatsScreenFragment mStatsFragment;
    private SettingsScreenFragment mSettingsFragment;
    protected FrameLayout mContentHolder;
    private BottomNavigationView mNavigation;
    public List<Integer> mColorList = new ArrayList<>();
    public static final String CATEGORIES_KEY = "CATEGORIES_KEY";
    public static final String EXPENSES = "EXPENSES";

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
        switch ( item.getItemId()){
            case R.id.option_menu_edit:
                return true;
            case R.id.option_menu_delete:
                return true;
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
}
