package runze.myapplication.presenters.statsScreenPresenter;

import runze.myapplication.presenters.IPresenter;
import runze.myapplication.views.statsScreenView.IStatsScreenView;



public interface IStatsScreenPresenter extends IPresenter<IStatsScreenView> {
    void updateView();
    void undoRemoveExpense();
}
