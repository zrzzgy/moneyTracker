package runze.moneytracker.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import runze.moneytracker.HomeActivity;
import runze.moneytracker.presenters.MainScreenPresenter;
import runze.moneytracker.views.MainScreenView;

public class MainScreenFragment extends BaseFragment<MainScreenView, MainScreenPresenter> {
    private MainScreenView mView;
    @Inject
    MainScreenPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mView = new MainScreenView(getContext());
        ((HomeActivity) getActivity()).getAppComponent().inject(this);
        mPresenter.attachView(mView);
        mView.attachPresenter(mPresenter);
        mView.updateView();
        return mView;
    }

    public void updateView() {
        mView.updateView();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();
        mView.detachPresenter();
        mView = null;
    }
}
