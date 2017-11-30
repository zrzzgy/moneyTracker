package runze.myapplication.presenters.settingsScreenPresenter;

import runze.myapplication.HomeActivity;
import runze.myapplication.views.settingsScreenView.ISettingsScreenView;



public class SettingsScreenPresenter implements ISettingsScreenPresenter {
    private HomeActivity mParentActivity;
    private ISettingsScreenView mView;

    public SettingsScreenPresenter(HomeActivity activity){
        mParentActivity = activity;
    }

    public void initView(){

    }

    @Override
    public void attachView(ISettingsScreenView view) {
        mView = view;
    }

    @Override
    public void detachView() {

    }
}
