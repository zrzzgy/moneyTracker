package runze.moneytracker.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import runze.moneytracker.HomeActivity;
import runze.moneytracker.R;
import runze.moneytracker.models.DataModel;
import runze.moneytracker.presenters.ExpenseAnalyzePresenter;
import runze.moneytracker.utils.MTPagerAdapter;
import runze.moneytracker.views.CategoryAnalysisView;
import runze.moneytracker.views.DayAnalysisView;


public class ExpenseAnalysisFragment extends Fragment {
    @Inject
    ExpenseAnalyzePresenter mAnalyzePresenter;
    private TabLayout mTabLayout;
    private TabLayout.Tab mTabDaily;
    private final int DAILY_TAB = 0;
    private final int CATEGORY_TAB = 1;
    private ViewPager mViewPager;
    private MTPagerAdapter mPagerAdapter;
    private DayAnalysisView mDayAnalysisView;
    private CategoryAnalysisView mCategoryAnalysisView;
    private DayAnalysisFragment mDayAnalysisFragment;
    private CategoryAnalysisFragment mCategoryAnalysisFragment;

    private TabLayout.OnTabSelectedListener mOnTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            int tabPosition = tab.getPosition();
            mViewPager.setCurrentItem(tabPosition);

            switch (tabPosition) {
                case DAILY_TAB:
                    mAnalyzePresenter.attachView(mDayAnalysisView);
                    mDayAnalysisFragment.attachPresenter(mAnalyzePresenter);
                    mAnalyzePresenter.updateView();
                    break;
                case CATEGORY_TAB:
                    mViewPager.setCurrentItem(tab.getPosition());
                    mAnalyzePresenter.attachView(mCategoryAnalysisView);
                    mCategoryAnalysisFragment.attachPresenter(mAnalyzePresenter);
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
    };

    private ViewPager.SimpleOnPageChangeListener mSimpleOnPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            // When swiping between pages, select the
            // corresponding tab.
            switch (position) {
                case DAILY_TAB:
                    mViewPager.setCurrentItem(0);
                    mAnalyzePresenter.attachView(mDayAnalysisView);
                    mDayAnalysisView.attachPresenter(mAnalyzePresenter);
                    mAnalyzePresenter.updateView();
                    break;
                case CATEGORY_TAB:
                    mViewPager.setCurrentItem(1);
                    mAnalyzePresenter.attachView(mCategoryAnalysisView);
                    mCategoryAnalysisView.attachPresenter(mAnalyzePresenter);
                    mAnalyzePresenter.updateView();
                    break;
            }

        }

    };

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
        mViewPager = view.findViewById(R.id.tab_view_pager);
        mTabLayout = view.findViewById(R.id.tab_layout);
        mTabDaily = mTabLayout.getTabAt(0);
        mPagerAdapter = new MTPagerAdapter(getFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);

        mDayAnalysisView = new DayAnalysisView(getContext());
        mCategoryAnalysisView = new CategoryAnalysisView(getContext());
        mDayAnalysisFragment = new DayAnalysisFragment();
        mCategoryAnalysisFragment = new CategoryAnalysisFragment();
        mDayAnalysisFragment.setDayAnalysisView(mDayAnalysisView);
        mCategoryAnalysisFragment.setCategoryAnalysisView(mCategoryAnalysisView);

        mPagerAdapter.addFragment(mDayAnalysisFragment);
        mPagerAdapter.addFragment(mCategoryAnalysisFragment);

        mViewPager.addOnPageChangeListener(mSimpleOnPageChangeListener);
        mTabLayout.addOnTabSelectedListener(mOnTabSelectedListener);

        mTabDaily.select();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mAnalyzePresenter.detachView();
        mCategoryAnalysisView.detachPresenter();
        mDayAnalysisView.detachPresenter();
    }

}
