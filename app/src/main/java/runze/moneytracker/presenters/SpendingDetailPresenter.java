package runze.moneytracker.presenters;

import runze.moneytracker.HomeActivity;
import runze.moneytracker.views.IView;
import runze.moneytracker.views.SettingsScreenView;

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
