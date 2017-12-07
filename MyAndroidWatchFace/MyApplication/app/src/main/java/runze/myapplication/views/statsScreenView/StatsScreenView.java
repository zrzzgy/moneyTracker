package runze.myapplication.views.statsScreenView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import runze.myapplication.R;
import runze.myapplication.presenters.IPresenter;



public class StatsScreenView extends RelativeLayout implements IStatsScreenView {

    public StatsScreenView(Context  context){
        super(context);
        View v = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.stats_screen_layout, this);
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
