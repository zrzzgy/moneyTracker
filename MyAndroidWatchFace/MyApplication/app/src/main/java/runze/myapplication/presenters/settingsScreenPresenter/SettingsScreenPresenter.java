package runze.myapplication.presenters.settingsScreenPresenter;

import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import runze.myapplication.HomeActivity;
import runze.myapplication.R;
import runze.myapplication.views.settingsScreenView.ISettingsScreenView;

import static runze.myapplication.HomeActivity.CATEGORIES_KEY;


public class SettingsScreenPresenter implements ISettingsScreenPresenter {
    private final String TAG = this.getClass().getSimpleName();

    private HomeActivity mParentActivity;
    private ISettingsScreenView mView;
    private Set<String> mCategories;

    public SettingsScreenPresenter(HomeActivity activity){
        mParentActivity = activity;
        String stringCategories = mParentActivity.mSharedPreferences.getString(CATEGORIES_KEY, "");
        Gson gson = new Gson();
        mCategories = gson.fromJson(stringCategories, new TypeToken<Set<String>>(){}.getType());
        if (mCategories == null){
            mCategories = new HashSet<>();
        }
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
        Gson gson = new Gson();

        if (newCategory.isEmpty()){
            Toast.makeText(mParentActivity.getApplicationContext(), mParentActivity.getResources().getString(R.string.no_category_entered), Toast.LENGTH_SHORT).show();
        }else if(mCategories.contains(newCategory)){
            Toast.makeText(mParentActivity.getApplicationContext(), mParentActivity.getResources().getString(R.string.category_already_exists), Toast.LENGTH_SHORT).show();
        } else{
            mCategories.add(newCategory);
            if (mParentActivity.mEditor.putString(CATEGORIES_KEY, gson.toJson(mCategories)).commit()){
                Toast.makeText(mParentActivity.getApplicationContext(), mParentActivity.getResources().getString(R.string.saved), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mParentActivity.getApplicationContext(), mParentActivity.getResources().getString(R.string.save_failed), Toast.LENGTH_SHORT).show();
            }
        }
        updateView();
    }
}
