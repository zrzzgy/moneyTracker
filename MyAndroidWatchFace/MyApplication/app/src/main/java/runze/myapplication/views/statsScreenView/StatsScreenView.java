package runze.myapplication.views.statsScreenView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import runze.myapplication.R;
import runze.myapplication.presenters.IPresenter;
import runze.myapplication.presenters.statsScreenPresenter.IStatsScreenPresenter;


public class StatsScreenView extends RelativeLayout implements IStatsScreenView {
    private IStatsScreenPresenter mPresenter;
    private ListView mListView;
    private ArrayAdapter<String> mAdapter;

    public StatsScreenView(Context  context){
        super(context);
        View v = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.stats_screen_layout, this);
        init(v);
    }

    private void init(View view){
        mListView = view.findViewById(R.id.statsList);
    }

    @Override
    public void attachPresenter(IPresenter presenter) {
        mPresenter = (IStatsScreenPresenter) presenter;
    }

    @Override
    public void detachPresenter() {
        mPresenter = null;
    }

    @Override
    public void renderData(List<String> data){
        if (!data.isEmpty()){
            if (mAdapter == null) {
                mAdapter = new ArrayAdapter<>(getContext(), R.layout.category_item, data);
                mListView.setAdapter(mAdapter);
            }else{
                Set<String> listToSet = new HashSet<>(data);
                mAdapter.clear();
                mAdapter.addAll(listToSet);
            }
        }
    }
}
