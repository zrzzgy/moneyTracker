package runze.myapplication.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import runze.myapplication.HomeActivity;
import runze.myapplication.presenters.inputScreenPresenter.IInputScreenPresenter;
import runze.myapplication.presenters.inputScreenPresenter.InputScreenPresenter;
import runze.myapplication.views.inputScreenView.IInputScreenView;
import runze.myapplication.views.inputScreenView.InputScreenView;

public class InputScreenFragment extends BaseFragment<IInputScreenView, IInputScreenPresenter> {
    private IInputScreenView mView;
    private IInputScreenPresenter mPresenter;

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
            mView = new InputScreenView(getActivity());
        }
        mPresenter = new InputScreenPresenter((HomeActivity) getActivity());
        mPresenter.attachView(mView);
        mView.attachPresenter(mPresenter);


        return (View) mView;
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

    /**
     * Refreshing views in this fragment
     */
    public void refresh() {
        mPresenter.updateView();
    }
}
