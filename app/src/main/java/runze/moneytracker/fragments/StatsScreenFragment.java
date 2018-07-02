package runze.moneytracker.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import runze.moneytracker.HomeActivity;
import runze.moneytracker.models.DataModel;
import runze.moneytracker.presenters.StatsScreenPresenter;
import runze.moneytracker.views.StatsScreenView;


public class StatsScreenFragment extends BaseFragment {
    @Inject StatsScreenView mView;
    @Inject StatsScreenPresenter mPresenter;

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
        mView.updateViewWithData();

        return mView;
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

    public StatsScreenPresenter getPresenter(){
        return mPresenter;
    }
}
