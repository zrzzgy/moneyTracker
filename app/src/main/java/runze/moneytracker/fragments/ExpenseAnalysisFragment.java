package runze.moneytracker.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
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
    private final String TAG = this.getClass().getSimpleName();

    @Inject
    ExpenseAnalyzePresenter mAnalyzePresenter;
    private TabLayout mTabLayout;
    private TabLayout.Tab mTabDaily;
    private TabLayout.Tab mTabCate;
    private final int DAILY_TAB = 0;
    private final int CATEGORY_TAB = 1;
    private ViewPager mViewPager;
    private MTPagerAdapter mPagerAdapter;
    private DayAnalysisView mDayAnalysisView;
    private CategoryAnalysisView mCategoryAnalysisView;

    private TabLayout.OnTabSelectedListener mOnTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            int tabPosition = tab.getPosition();
            mViewPager.setCurrentItem(tabPosition);

            switch (tabPosition) {
                case DAILY_TAB:
                    mAnalyzePresenter.attachView(mDayAnalysisView);
                    mDayAnalysisView.attachPresenter(mAnalyzePresenter);
                    mAnalyzePresenter.updateView();
                    break;
                case CATEGORY_TAB:
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
    };

    private ViewPager.SimpleOnPageChangeListener mSimpleOnPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            // When swiping between pages, select the
            // corresponding tab.
            switch (position) {
                case DAILY_TAB:
                    Log.v(TAG, "Swiped to time analysis.");
                    //delegate to the OnTabSelectedListener to do everything
                    mTabDaily.select();
                    break;
                case CATEGORY_TAB:
                    Log.v(TAG, "swiped to category analysis.");
                    //delegate to the OnTabSelectedListener to do everything
                    mTabCate.select();
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
        mTabCate = mTabLayout.getTabAt(1);

        mDayAnalysisView = new DayAnalysisView(getContext());
        mCategoryAnalysisView = new CategoryAnalysisView(getContext());

        mPagerAdapter = new MTPagerAdapter();
        mPagerAdapter.addView(mDayAnalysisView);
        mPagerAdapter.addView(mCategoryAnalysisView);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);

        mViewPager.addOnPageChangeListener(mSimpleOnPageChangeListener);
        mTabLayout.addOnTabSelectedListener(mOnTabSelectedListener);

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
