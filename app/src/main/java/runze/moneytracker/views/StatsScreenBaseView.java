package runze.moneytracker.views;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import runze.moneytracker.R;
import runze.moneytracker.presenters.CategoryExpenseAnalysisPresenter;
import runze.moneytracker.presenters.DailyExpenseAnalysisPresenter;
import runze.moneytracker.presenters.IPresenter;
import runze.moneytracker.presenters.StatsScreenBasePresenter;


public class StatsScreenBaseView extends RelativeLayout implements IView {
    private final String TAG = getClass().getSimpleName();

    private StatsScreenBasePresenter mPresenter;
    private TabLayout mTabLayout;
    private TabLayout.Tab mTabDaily;
    private TabLayout.Tab mTabCategory;
    private FrameLayout mGraph;
    private final int DAILY_TAB = 0;
    private final int CATEGORY_TAB = 1;

    public StatsScreenBaseView(Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.stats_view_layout, this);
        init(view);
    }

    private void init(View view) {
        mGraph = view.findViewById(R.id.graph);
        mTabLayout = view.findViewById(R.id.upper_tab_layout);
        mTabDaily = mTabLayout.getTabAt(0);
        mTabCategory = mTabLayout.getTabAt(1);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()) {
                    case DAILY_TAB:
                        DailyExpenseAnalysisPresenter presenter1 = new DailyExpenseAnalysisPresenter(getContext());
                        DailyExpenseAnalysisView view1 = new DailyExpenseAnalysisView(getContext());
                        presenter1.attachView(view1);
                        presenter1.updateView();
                        mGraph.removeAllViews();
                        mGraph.addView(view1);
                        break;
                    case CATEGORY_TAB:
                        CategoryExpenseAnalysisPresenter presenter2 = new CategoryExpenseAnalysisPresenter(getContext());
                        CategoryExpenseAnalysisView view2 = new CategoryExpenseAnalysisView(getContext());
                        presenter2.attachView(view2);
                        presenter2.updateView();
                        mGraph.removeAllViews();
                        mGraph.addView(view2);
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
    public void attachPresenter(IPresenter presenter) {
        mPresenter = (StatsScreenBasePresenter) presenter;
    }

    @Override
    public void detachPresenter() {
        mPresenter = null;
    }

}
