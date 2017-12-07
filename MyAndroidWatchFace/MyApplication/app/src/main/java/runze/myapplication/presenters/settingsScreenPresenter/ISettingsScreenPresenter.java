package runze.myapplication.presenters.settingsScreenPresenter;

import runze.myapplication.presenters.IPresenter;
import runze.myapplication.views.settingsScreenView.ISettingsScreenView;


public interface ISettingsScreenPresenter extends IPresenter<ISettingsScreenView> {
    void updateView();

    void saveCategory(String newCategory);
}
