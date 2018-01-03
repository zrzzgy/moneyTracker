package runze.myapplication.presenters.settingsScreenPresenter;

import android.view.MenuItem;

import runze.myapplication.presenters.IPresenter;
import runze.myapplication.views.settingsScreenView.ISettingsScreenView;


public interface ISettingsScreenPresenter extends IPresenter<ISettingsScreenView> {
    void updateView();
    void saveCategory(String newCategory);
    void removeCategory(MenuItem item);
    void undoRemoveCategory();
}
