package runze.myapplication.presenters.settingsScreenPresenter;

import android.view.MenuItem;

import runze.myapplication.presenters.IPresenter;
import runze.myapplication.views.SettingsScreenView;


public interface ISettingsScreenPresenter extends IPresenter<SettingsScreenView> {
    void updateView();
    void saveCategory(String newCategory);
    void editCategory(MenuItem item);
    void removeCategory(MenuItem item);
    void undoRemoveCategory();
}
