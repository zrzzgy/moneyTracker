package runze.myapplication.presenters;

import runze.myapplication.HomeActivity;
import runze.myapplication.views.IView;
import runze.myapplication.views.SettingsScreenView;

public class SpendingDetailPresenter implements IPresenter {
    private HomeActivity mParentActivity;
    private SettingsScreenView mView;

    public SpendingDetailPresenter(HomeActivity homeActivity){
        mParentActivity = homeActivity;
    }

    @Override
    public void attachView(IView view) {
        mView = (SettingsScreenView) view;
    }

    @Override
    public void detachView() {
        mView = null;
    }
}
