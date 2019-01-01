package runze.moneytracker.views;

import android.content.Context;
import android.view.LayoutInflater;

import runze.moneytracker.R;
import runze.moneytracker.presenters.IPresenter;

public class AboutView extends SettingsView implements IView{

    public AboutView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.about_view_layout, this);
    }

    @Override
    public void attachPresenter(IPresenter presenter) {

    }

    @Override
    public void detachPresenter() {

    }
}
