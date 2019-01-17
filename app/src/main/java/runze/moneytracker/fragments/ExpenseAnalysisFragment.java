package runze.moneytracker.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import runze.moneytracker.HomeActivity;
import runze.moneytracker.R;
import runze.moneytracker.models.DataModel;
import runze.moneytracker.presenters.ExpenseAnalyzePresenter;
import runze.moneytracker.utils.MTPagerAdapter;
import runze.moneytracker.views.CategoryAnalysisView;
import runze.moneytracker.views.DayAnalysisView;


public class ExpenseAnalysisFragment extends BaseFragment {
    private DayAnalysisView mDayAnalysisView;
    private CategoryAnalysisView mCategoryAnalysisView;
    @Inject
    ExpenseAnalyzePresenter mAnalyzePresenter;
    private TabLayout mTabLayout;
    private TabLayout.Tab mTabDaily;
    private final int DAILY_TAB = 0;
    private final int CATEGORY_TAB = 1;
    private ViewPager mViewPager;
    private MTPagerAdapter mPagerAdapter;

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
        View view = LayoutInflater.from(getContext()).inflate(R.layout.analysis_base_layout, null);
        if (mDayAnalysisView == null) {
            mDayAnalysisView = new DayAnalysisView(getContext());
            mDayAnalysisView.attachPresenter(mAnalyzePresenter);
        }
        if (mCategoryAnalysisView == null) {
            mCategoryAnalysisView = new CategoryAnalysisView(getContext());
            mCategoryAnalysisView.attachPresenter(mAnalyzePresenter);
        }
        init(view);
        if (mDayAnalysisView.getParent() != null) {
            ((ViewGroup) mDayAnalysisView.getParent()).removeView(mDayAnalysisView);
        }
       // mFrameLayout.addView(mDayAnalysisView);
        mViewPager.setCurrentItem(0);
        mAnalyzePresenter.attachView(mDayAnalysisView);
        mDayAnalysisView.attachPresenter(mAnalyzePresenter); // to set presenter

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mAnalyzePresenter.updateView();
    }

    public void updateModel(DataModel dataModel) {
        if (mAnalyzePresenter != null) {
            mAnalyzePresenter.updateModel(dataModel);
        }
    }

    private void init(View view) {
        mViewPager = view.findViewById(R.id.tab_view_pager);
        mTabLayout = view.findViewById(R.id.tab_layout);
        mTabDaily = mTabLayout.getTabAt(0);
        List<View> viewList = new ArrayList<>();
        viewList.add(mDayAnalysisView);
        viewList.add(mCategoryAnalysisView);
        mPagerAdapter = new MTPagerAdapter(viewList);
        mViewPager.setAdapter(mPagerAdapter);

        mViewPager.addOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        // When swiping between pages, select the
                        // corresponding tab.
                       switch(position) {
                           case 0:
                               if (mCategoryAnalysisView.getParent() != null) {
                                   ((ViewGroup) mCategoryAnalysisView.getParent()).removeAllViews();
                               }
                               mViewPager.setCurrentItem(0);
                               mAnalyzePresenter.attachView(mDayAnalysisView);
                               mDayAnalysisView.attachPresenter(mAnalyzePresenter);
                               mAnalyzePresenter.updateView();
                               break;
                           case 1:
                               if (mDayAnalysisView.getParent() != null) {
                                   ((ViewGroup) mDayAnalysisView.getParent()).removeAllViews();
                               }
                               mViewPager.setCurrentItem(1);
                               mAnalyzePresenter.attachView(mCategoryAnalysisView);
                               mCategoryAnalysisView.attachPresenter(mAnalyzePresenter);
                               mAnalyzePresenter.updateView();
                               break;
                       }

                    }

                });

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case DAILY_TAB:
                        if (mCategoryAnalysisView.getParent() != null) {
                            ((ViewGroup) mCategoryAnalysisView.getParent()).removeAllViews();
                        }
                        mViewPager.setCurrentItem(tab.getPosition());
                        mAnalyzePresenter.attachView(mDayAnalysisView);
                        mDayAnalysisView.attachPresenter(mAnalyzePresenter);
                        mAnalyzePresenter.updateView();
                        break;
                    case CATEGORY_TAB:
                        if (mDayAnalysisView.getParent() != null) {
                            ((ViewGroup) mDayAnalysisView.getParent()).removeAllViews();
                        }
                        mViewPager.setCurrentItem(tab.getPosition());
                        mAnalyzePresenter.attachView(mCategoryAnalysisView);
                        mCategoryAnalysisView.attachPresenter(mAnalyzePresenter);
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
