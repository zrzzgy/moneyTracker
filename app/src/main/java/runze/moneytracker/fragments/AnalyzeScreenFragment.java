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
import runze.moneytracker.views.CategoryAnalyzeView;
import runze.moneytracker.views.DayAnalyzeView;
import runze.moneytracker.views.IView;


public class AnalyzeScreenFragment extends BaseFragment {
    private DayAnalyzeView mDayAnalyzeView;
    private CategoryAnalyzeView mCategoryAnalyzeView;
    @Inject
    ExpenseAnalyzePresenter mAnalyzePresenter;
    private TabLayout mTabLayout;
    private TabLayout.Tab mTabDaily;
    private TabLayout.Tab mTabCategory;
    private FrameLayout mChildAnalyzeLayout;
    private final int DAILY_TAB = 0;
    private final int CATEGORY_TAB = 1;
    private RecyclerView mDetailList;

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
        View view = LayoutInflater.from(getContext()).inflate(R.layout.analyze_view_layout, null);
        init(view);
        if (mDayAnalyzeView == null) {
            mDayAnalyzeView = new DayAnalyzeView(getContext());
            mDayAnalyzeView.attachPresenter(mAnalyzePresenter);
        }
        if (mCategoryAnalyzeView == null) {
            mCategoryAnalyzeView = new CategoryAnalyzeView(getContext());
            mCategoryAnalyzeView.attachPresenter(mAnalyzePresenter);
        }

        mChildAnalyzeLayout.removeAllViews();
        mChildAnalyzeLayout.addView(mDayAnalyzeView);
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
        mChildAnalyzeLayout = view.findViewById(R.id.child_analyze_view);
        mTabLayout = view.findViewById(R.id.tab_layout);
        mDetailList = view.findViewById(R.id.analyze_view_expense_detail_list);
        mTabDaily = mTabLayout.getTabAt(0);
        mTabCategory = mTabLayout.getTabAt(1);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case DAILY_TAB:
                        mChildAnalyzeLayout.removeAllViews();
                        mChildAnalyzeLayout.addView(mDayAnalyzeView);

                        mAnalyzePresenter.attachView(mDayAnalyzeView);
                        mAnalyzePresenter.updateView();
                        break;
                    case CATEGORY_TAB:
                        mChildAnalyzeLayout.removeAllViews();
                        mChildAnalyzeLayout.addView(mCategoryAnalyzeView);

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
