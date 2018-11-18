package runze.moneytracker.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import javax.inject.Inject;

import runze.moneytracker.HomeActivity;
import runze.moneytracker.R;
import runze.moneytracker.models.DataModel;
import runze.moneytracker.presenters.ExpenseAnalyzePresenter;
import runze.moneytracker.utils.ExpenseDetailAnalysisListRecyclerAdapter;
import runze.moneytracker.views.CategoryAnalyzeView;
import runze.moneytracker.views.DayAnalyzeView;


public class ExpenseAnalysisFragment extends BaseFragment {
    private DayAnalyzeView mDayAnalyzeView;
    private CategoryAnalyzeView mCategoryAnalyzeView;
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
        if (mDayAnalyzeView == null) {
            mDayAnalyzeView = new DayAnalyzeView(getContext());
            mDayAnalyzeView.attachPresenter(mAnalyzePresenter);
        }
        if (mCategoryAnalyzeView == null) {
            mCategoryAnalyzeView = new CategoryAnalyzeView(getContext());
            mCategoryAnalyzeView.attachPresenter(mAnalyzePresenter);
        }

        mChildAnalysisLayout.removeAllViews();
        mChildAnalysisLayout.addView(mDayAnalyzeView);
        mAnalyzePresenter.attachView(mDayAnalyzeView);
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
                        mChildAnalysisLayout.addView(mDayAnalyzeView);

                        mAnalyzePresenter.attachView(mDayAnalyzeView);
                        mAnalyzePresenter.updateView();
                        break;
                    case CATEGORY_TAB:
                        mChildAnalysisLayout.removeAllViews();
                        mChildAnalysisLayout.addView(mCategoryAnalyzeView);

                        mAnalyzePresenter.attachView(mCategoryAnalyzeView);
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
        mCategoryAnalyzeView.detachPresenter();
        mDayAnalyzeView.detachPresenter();
    }

}
