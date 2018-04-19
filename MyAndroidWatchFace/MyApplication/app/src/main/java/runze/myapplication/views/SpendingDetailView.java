package runze.myapplication.views;

import android.content.Context;
import android.widget.RelativeLayout;

import runze.myapplication.presenters.IPresenter;

public class SpendingDetailView extends RelativeLayout implements IView{

    public SpendingDetailView(Context context) {
        super(context);
    }

    @Override
    public void attachPresenter(IPresenter presenter) {

    }

    @Override
    public void detachPresenter() {

    }
}
