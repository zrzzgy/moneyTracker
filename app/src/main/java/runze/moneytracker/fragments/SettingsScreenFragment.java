package runze.moneytracker.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import runze.moneytracker.HomeActivity;
import runze.moneytracker.presenters.SettingsPresenter;
import runze.moneytracker.views.SettingsView;



public class SettingsScreenFragment extends BaseFragment {
    private SettingsView mView;
    @Inject
    SettingsPresenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mView = new SettingsView(getContext());
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

    public SettingsPresenter getPresenter() {
        return mPresenter;
    }

    /**
     * Refreshing views in this fragment
     */
    public void refresh() {
        mPresenter.updateView();
    }

    @Nullable
    @Override
    public View getView() {
        return mView;
    }
}
