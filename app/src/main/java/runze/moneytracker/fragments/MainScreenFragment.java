package runze.moneytracker.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import runze.moneytracker.HomeActivity;
import runze.moneytracker.presenters.MainScreenPresenter;
import runze.moneytracker.views.MainView;

public class MainScreenFragment extends Fragment {
    private MainView mView;
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

        mView = new MainView(getContext());
        ((HomeActivity) getActivity()).getAppComponent().inject(this);
        mPresenter.attachView(mView);
        mView.attachPresenter(mPresenter);
        mView.updateView();
        return mView;
    }

    public void updateView() {
        if (mView != null) {
            mView.updateView();
        }
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
