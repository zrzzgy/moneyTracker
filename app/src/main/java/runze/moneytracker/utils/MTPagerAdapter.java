package runze.moneytracker.utils;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Class used for swiping to switch between screens
 */
public class MTPagerAdapter extends PagerAdapter{
    private List<View> mViewLists;

    public MTPagerAdapter(List<View> viewList) {
        mViewLists = viewList;
    }


    @Override
    public int getCount() {
        return mViewLists.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mViewLists.get(position));
        return container;
    }
}
