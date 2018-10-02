package runze.moneytracker.presenters;

import android.content.Context;

import runze.moneytracker.HomeActivity;
import runze.moneytracker.views.DailyExpenseAnalysisView;
import runze.moneytracker.views.IView;
import runze.moneytracker.views.StatsScreenBaseView;

/**
 * Stats Screen Presenter
 */
public class StatsScreenBasePresenter implements IPresenter {
    private final String TAG = this.getClass().getSimpleName();
    private StatsScreenBaseView mView;
    private DailyExpenseAnalysisView mDailyExpenseAnalysisView;


    public StatsScreenBasePresenter(Context context) {
    }

    @Override
    public void attachView(IView view) {
        mView = (StatsScreenBaseView) view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void updateView() {

    }


}
