package runze.myapplication.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import runze.myapplication.HomeActivity;
import runze.myapplication.presenters.statsScreenPresenter.IStatsScreenPresenter;
import runze.myapplication.presenters.statsScreenPresenter.StatsScreenPresenter;
import runze.myapplication.views.statsScreenView.IStatsScreenView;
import runze.myapplication.views.statsScreenView.StatsScreenView;


public class StatsScreenFragment extends BaseFragment {
    @Inject IStatsScreenView mView;
    @Inject IStatsScreenPresenter mPresenter;

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
        if (mView == null) {
            mView = new StatsScreenView(getActivity());
        }
        mPresenter = new StatsScreenPresenter((HomeActivity) getActivity());
        mPresenter.attachView(mView);
        mView.attachPresenter(mPresenter);

        return (View) mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();
        mView.detachPresenter();
        mView = null;
    }

    /**
     * Refreshing views in this fragment
     */
    public void refresh() {
        mPresenter.updateView();
    }

    public IStatsScreenPresenter getPresenter(){
        return mPresenter;
    }
}
