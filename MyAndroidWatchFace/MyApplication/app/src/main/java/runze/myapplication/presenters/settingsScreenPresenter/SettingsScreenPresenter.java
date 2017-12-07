package runze.myapplication.presenters.settingsScreenPresenter;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import runze.myapplication.HomeActivity;
import runze.myapplication.views.settingsScreenView.ISettingsScreenView;



public class SettingsScreenPresenter implements ISettingsScreenPresenter {
    private HomeActivity mParentActivity;
    private ISettingsScreenView mView;
    private Set<String> mCategories;
    private final String CATEGORIES_KEY = "CATEGORIES_KEY";

    public SettingsScreenPresenter(HomeActivity activity){
        mParentActivity = activity;
        mCategories = mParentActivity.mSharedPreferences.getStringSet(CATEGORIES_KEY, new HashSet<String>());
    }

    public void updateView(){
        mView.populateListView(mCategories);
    }

    @Override
    public void attachView(ISettingsScreenView view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void saveCategory(String newCategory){
        if (newCategory.isEmpty()){
            Toast.makeText(mParentActivity.getApplicationContext(), "No category name entered", Toast.LENGTH_SHORT).show();
        }else {
            mCategories.add(newCategory);
            mParentActivity.mEditor.putStringSet(CATEGORIES_KEY, mCategories).apply();
        }
        updateView();
    }
}
