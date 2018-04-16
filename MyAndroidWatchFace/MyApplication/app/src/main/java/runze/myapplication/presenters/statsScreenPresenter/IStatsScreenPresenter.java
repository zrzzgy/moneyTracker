package runze.myapplication.presenters.statsScreenPresenter;

import runze.myapplication.presenters.IPresenter;
import runze.myapplication.views.StatsScreenView;


public interface IStatsScreenPresenter extends IPresenter<StatsScreenView> {
    void updateView();
    void undoRemoveExpense();
}
