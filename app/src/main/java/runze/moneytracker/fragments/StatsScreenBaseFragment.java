package runze.moneytracker.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import runze.moneytracker.HomeActivity;
import runze.moneytracker.presenters.DailyExpenseAnalysisPresenter;
import runze.moneytracker.presenters.IPresenter;
import runze.moneytracker.presenters.StatsScreenBasePresenter;
import runze.moneytracker.views.DailyExpenseAnalysisView;
import runze.moneytracker.views.IView;
import runze.moneytracker.views.StatsScreenBaseView;


public class StatsScreenBaseFragment extends BaseFragment {
    private StatsScreenBaseView mBaseView;
    private StatsScreenBasePresenter mBasePresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ((HomeActivity) getActivity()).getAppComponent().inject(this);

        // Construct the view if it does not yet exist
        if (mBaseView == null) {
            mBaseView = new StatsScreenBaseView(getActivity());
        }
        mBaseView.attachPresenter(mBasePresenter);

        return mBaseView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBasePresenter.detachView();
        mBaseView.detachPresenter();
        mBaseView = null;
    }

}
