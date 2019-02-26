package runze.moneytracker.utils;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used for swiping to switch between screens
 */
public class MTPagerAdapter extends PagerAdapter {
    private List<View> mViewList;

    public MTPagerAdapter() {
        super();
        mViewList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mViewList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View targetView = mViewList.get(position);
        container.addView(targetView);
        return targetView;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);    }

    public void addView(View view){
        mViewList.add(view);
    }
}
