package runze.moneytracker.presenters;

import runze.moneytracker.models.DataModel;
import runze.moneytracker.views.IView;
import runze.moneytracker.views.SettingsScreenView;

public class SettingsScreenPresenter implements IPresenter {
    private final String TAG = this.getClass().getSimpleName();

    private SettingsScreenView mView;


    public SettingsScreenPresenter(DataModel dataModel) {

    }

    public void updateView() {

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
