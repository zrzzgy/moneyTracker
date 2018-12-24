package runze.moneytracker.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import javax.inject.Inject;

import runze.moneytracker.HomeActivity;
import runze.moneytracker.R;
import runze.moneytracker.models.DataModel;
import runze.moneytracker.presenters.ExpenseAnalyzePresenter;
import runze.moneytracker.views.CategoryAnalysisView;
import runze.moneytracker.views.DayAnalysisView;


public class ExpenseAnalysisFragment extends BaseFragment {
    private DayAnalysisView mDayAnalysisView;
    private CategoryAnalysisView mCategoryAnalysisView;
    @Inject
    ExpenseAnalyzePresenter mAnalyzePresenter;
    private TabLayout mTabLayout;
    private TabLayout.Tab mTabDaily;
    private FrameLayout mChildAnalysisLayout;
    private final int DAILY_TAB = 0;
    private final int CATEGORY_TAB = 1;

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

        // Construct the view if it does not yet exist
        View view = LayoutInflater.from(getContext()).inflate(R.layout.analysis_view_layout, null);
        init(view);
        if (mDayAnalysisView == null) {
            mDayAnalysisView = new DayAnalysisView(getContext());
            mDayAnalysisView.attachPresenter(mAnalyzePresenter);
        }
        if (mCategoryAnalysisView == null) {
            mCategoryAnalysisView = new CategoryAnalysisView(getContext());
            mCategoryAnalysisView.attachPresenter(mAnalyzePresenter);
        }

        mChildAnalysisLayout.removeAllViews();
        mChildAnalysisLayout.addView(mDayAnalysisView);
        mAnalyzePresenter.attachView(mDayAnalysisView);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mAnalyzePresenter.updateView();
    }

    public void updateModel(DataModel dataModel) {
        mAnalyzePresenter.updateModel(dataModel);
    }

    private void init(View view) {
        mChildAnalysisLayout = view.findViewById(R.id.child_analyze_view);
        mTabLayout = view.findViewById(R.id.tab_layout);
        mTabDaily = mTabLayout.getTabAt(0);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case DAILY_TAB:
                        mChildAnalysisLayout.removeAllViews();
                        mChildAnalysisLayout.addView(mDayAnalysisView);

                        mAnalyzePresenter.attachView(mDayAnalysisView);
                        mAnalyzePresenter.updateView();
                        break;
                    case CATEGORY_TAB:
                        mChildAnalysisLayout.removeAllViews();
                        mChildAnalysisLayout.addView(mCategoryAnalysisView);

                        mAnalyzePresenter.attachView(mCategoryAnalysisView);
                        mAnalyzePresenter.updateView();
                        break;

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mTabDaily.select();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mAnalyzePresenter.detachView();
        mCategoryAnalysisView.detachPresenter();
        mDayAnalysisView.detachPresenter();
    }

}
