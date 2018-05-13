package runze.moneytracker.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import runze.moneytracker.HomeActivity;
import runze.moneytracker.presenters.SettingsScreenPresenter;
import runze.moneytracker.views.SettingsScreenView;



public class SettingsScreenFragment extends BaseFragment {
    @Inject SettingsScreenView mView;
    @Inject SettingsScreenPresenter mPresenter;

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

        mPresenter.attachView(mView);
        mView.attachPresenter(mPresenter);

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.updateView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();
        mView.detachPresenter();
        mView = null;
    }

    public SettingsScreenPresenter getPresenter() {
        return mPresenter;
    }

    /**
     * Refreshing views in this fragment
     */
    public void refresh() {
        mPresenter.updateView();
    }
}
