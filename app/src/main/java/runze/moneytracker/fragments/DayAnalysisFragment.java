package runze.moneytracker.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import runze.moneytracker.presenters.IPresenter;
import runze.moneytracker.views.DayAnalysisView;

public class DayAnalysisFragment extends Fragment {
    private DayAnalysisView mDayAnalysisView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (mDayAnalysisView == null) {
            mDayAnalysisView = new DayAnalysisView(getContext());
        }
        return mDayAnalysisView;
    }

    public void attachPresenter(IPresenter presenter){
        mDayAnalysisView.attachPresenter(presenter);
    }

  public void setDayAnalysisView(DayAnalysisView dayAnalysisView){
        mDayAnalysisView = dayAnalysisView;
  }
}
