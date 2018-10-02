package runze.moneytracker.views;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.ViewGroup;
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
    private final int DAILY_TAB = 0;
    private final int CATEGORY_TAB = 1;

    public StatsScreenBaseView(LayoutInflater inflater, ViewGroup container, Context context) {
        super(context);
        final Context contextThemeWrapper = new ContextThemeWrapper(getContext(), R.style.TabAppTheme);
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
        localInflater.inflate(R.layout.stats_view_layout, container, false);
        init();
        //todo check when we use context view
        //((HomeActivity) getContext()).registerForContextMenu(mStatsList);
    }

    private void init() {
        mTabLayout = findViewById(R.id.upper_tab_layout);
        mTabDaily = mTabLayout.getTabAt(0);
        mTabCategory = mTabLayout.getTabAt(1);
        mTabDaily.select();

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()) {
                    case DAILY_TAB:
                        DailyExpenseAnalysisPresenter presenter1 = new DailyExpenseAnalysisPresenter(getContext());
                        DailyExpenseAnalysisView view1 = new DailyExpenseAnalysisView(getContext());
                        presenter1.attachView(view1);
                        presenter1.updateView();
                        mTabDaily.setCustomView(view1);
                        break;
                    case CATEGORY_TAB:
                        CategoryExpenseAnalysisPresenter presenter2 = new CategoryExpenseAnalysisPresenter(getContext());
                        CategoryExpenseAnalysisView view2 = new CategoryExpenseAnalysisView(getContext());
                        presenter2.attachView(view2);
                        presenter2.updateView();
                        mTabCategory.setCustomView(view2);
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
