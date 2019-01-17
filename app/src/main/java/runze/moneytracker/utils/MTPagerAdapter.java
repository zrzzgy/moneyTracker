package runze.moneytracker.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import runze.moneytracker.fragments.CategoryAnalysisFragment;
import runze.moneytracker.fragments.DayAnalysisFragment;

/**
 * Class used for swiping to switch between screens
 */
public class MTPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragmentList;

    public MTPagerAdapter(FragmentManager fm) {
        super(fm);
        mFragmentList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    public void addFragment(Fragment fragment){
        mFragmentList.add(fragment);
    }
}
