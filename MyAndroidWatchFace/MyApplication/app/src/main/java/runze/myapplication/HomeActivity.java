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
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import runze.myapplication.fragments.BaseFragment;
import runze.myapplication.fragments.InputScreenFragment;

public class HomeActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    private TextView mTextMessage;
    private static final String SHARED_PREF_ID = "moneyTrackerPreferenceFile";
    public SharedPreferences mSharedPreferences;
    public SharedPreferences.Editor mEditor;
    private BaseFragment mInputFragment;
    protected FrameLayout mContentHolder;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_input:
                    mTextMessage.setText(R.string.title_input);
                    return true;
                case R.id.navigation_stats:
                    mTextMessage.setText(R.string.title_stats);
                    return true;
                case R.id.navigation_settings:
                    mTextMessage.setText(R.string.title_settings);
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

        mSharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREF_ID, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        logBackStack();
    }

    public void initComponents(){
        setContentView(R.layout.home_activity);
        mContentHolder = findViewById(R.id.home_content_holder);
        mInputFragment = new InputScreenFragment();
        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
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
    public void onBackPressed(){
           logBackStack();
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
