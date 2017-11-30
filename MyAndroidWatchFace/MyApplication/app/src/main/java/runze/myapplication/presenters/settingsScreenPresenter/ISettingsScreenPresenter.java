package runze.myapplication.presenters.settingsScreenPresenter;

import runze.myapplication.presenters.IPresenter;
import runze.myapplication.views.settingsScreenView.ISettingsScreenView;

/**
 * Created by zrzzg on 11/29/2017.
 */

public interface ISettingsScreenPresenter extends IPresenter<ISettingsScreenView> {
    void initView();
}
