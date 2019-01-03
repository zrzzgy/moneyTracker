package runze.moneytracker.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import runze.moneytracker.views.AboutView;

public class AboutFragment extends BaseFragment {
    private AboutView mAboutview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mAboutview = new AboutView(getContext());
        return mAboutview;
    }
}
