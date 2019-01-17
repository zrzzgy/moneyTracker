package runze.moneytracker.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import runze.moneytracker.presenters.IPresenter;
import runze.moneytracker.views.CategoryAnalysisView;
import runze.moneytracker.views.DayAnalysisView;

public class CategoryAnalysisFragment extends Fragment {
    private CategoryAnalysisView mCategoryAnalysisView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (mCategoryAnalysisView == null) {
            mCategoryAnalysisView = new CategoryAnalysisView(getContext());
        }
        return mCategoryAnalysisView;
    }

    public void attachPresenter(IPresenter presenter){
        mCategoryAnalysisView.attachPresenter(presenter);
    }

    public void setCategoryAnalysisView(CategoryAnalysisView categoryAnalysisView){
        mCategoryAnalysisView = categoryAnalysisView;
    }
}
