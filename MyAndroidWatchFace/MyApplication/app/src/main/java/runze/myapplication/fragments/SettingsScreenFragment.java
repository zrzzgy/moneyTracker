package runze.myapplication.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import runze.myapplication.HomeActivity;
import runze.myapplication.presenters.settingsScreenPresenter.ISettingsScreenPresenter;
import runze.myapplication.presenters.settingsScreenPresenter.SettingsScreenPresenter;
import runze.myapplication.views.settingsScreenView.ISettingsScreenView;
import runze.myapplication.views.settingsScreenView.SettingsScreenView;



public class SettingsScreenFragment extends BaseFragment {
    private ISettingsScreenView mView;
    private ISettingsScreenPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Construct the view if it does not yet exist
        if (mView == null) {
            mView = new SettingsScreenView(getActivity());
        }
        mPresenter = new SettingsScreenPresenter((HomeActivity) getActivity());
        mPresenter.attachView(mView);
        mView.attachPresenter(mPresenter);


        return (View) mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.initView();
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
        mPresenter.initView();
    }
}
