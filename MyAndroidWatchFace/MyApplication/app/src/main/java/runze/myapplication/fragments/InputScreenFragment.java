package runze.myapplication.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import runze.myapplication.HomeActivity;
import runze.myapplication.presenters.inputScreenPresenter.InputScreenPresenter;
import runze.myapplication.views.InputScreenView;

public class InputScreenFragment extends BaseFragment<InputScreenView, InputScreenPresenter> {
    @Inject InputScreenView mView;
    @Inject InputScreenPresenter mPresenter;

    private int spinnerIndexWhenPaused = 0;

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

        return (View) mView;
    }

    @Override
    public void onPause(){
        super.onPause();
//        spinnerIndexWhenPaused = mView.getSpinnerIndex();
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
        mPresenter.updateView(spinnerIndexWhenPaused);
    }
}
