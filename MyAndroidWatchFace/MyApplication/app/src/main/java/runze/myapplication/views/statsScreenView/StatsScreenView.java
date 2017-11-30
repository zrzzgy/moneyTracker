package runze.myapplication.views.statsScreenView;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import runze.myapplication.HomeActivity;
import runze.myapplication.R;
import runze.myapplication.presenters.IPresenter;



public class StatsScreenView extends RelativeLayout implements IStatsScreenView {
    private HomeActivity mActivity;

    public StatsScreenView(HomeActivity activity){
        super(activity.getApplicationContext());
        View v = LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.stats_screen_layout, this);
        mActivity = activity;
        init(v);
    }

    private void init(View view){
    }

    @Override
    public void attachPresenter(IPresenter presenter) {

    }

    @Override
    public void detachPresenter() {

    }
}
