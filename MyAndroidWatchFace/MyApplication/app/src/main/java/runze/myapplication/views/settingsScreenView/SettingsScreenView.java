package runze.myapplication.views.settingsScreenView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import runze.myapplication.R;
import runze.myapplication.presenters.IPresenter;



public class SettingsScreenView extends LinearLayout implements ISettingsScreenView {
    private ScrollView mScrollView;


    public SettingsScreenView(Context context){
        super(context);
        View v = LayoutInflater.from(context).inflate(R.layout.settings_screen_layout, this);
        init(v);
    }

    private void init(View view){
        mScrollView = view.findViewById(R.id.categoryList);

    }

    @Override
    public void attachPresenter(IPresenter presenter) {

    }

    @Override
    public void detachPresenter() {

    }
}
