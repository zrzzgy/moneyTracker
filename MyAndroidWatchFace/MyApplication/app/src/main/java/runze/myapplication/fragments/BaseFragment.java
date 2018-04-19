package runze.myapplication.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import java.io.Serializable;

import runze.myapplication.HomeActivity;
import runze.myapplication.presenters.IPresenter;

public abstract class BaseFragment<V extends View, P extends IPresenter<V>> extends Fragment {
    protected String TAG;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        TAG = this.getClass().getSimpleName() + " " + hashCode();
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    /**
     * Handles back presses for the fragment
     *
     * @return true if the fragment completely handled the back; false if handling should continue
     */
    public boolean onBackPressed() {
        return false;
    }

    /**
     * Replaces the current fragment with the specified fragment
     *
     * @param containerId        the container in which launch the fragment
     * @param navigateToFragment the fragment to launch
     */
    protected final void replaceFragment(int containerId, Fragment navigateToFragment) {
        getFragmentManager().beginTransaction().replace(containerId, navigateToFragment).commit();
    }

    /**
     * Launches an activity and finishes the current activity, removing it from the back stack.
     *
     * @param navigateTo activity to navigate to     *
     */
    protected final void launchActivity(Class navigateTo) {
        boolean startedFromNotification = getActivity().getIntent().getBooleanExtra("fromNotification", false);
        launchActivity(navigateTo, true, "fromNotification", startedFromNotification);
    }

    /**
     * Launches an activity and passes a serializable extra
     *
     * @param navigateTo                  activity to launch
     * @param shouldFinishCurrentActivity true to finish current activity, removing it from the back stack
     * @param extraString                 key for the extra
     * @param extra                       serializable extra to pass with the intent
     */
    private void launchActivity(Class navigateTo, boolean shouldFinishCurrentActivity,
                                String extraString, Serializable extra) {
        if (!navigateTo.equals(this.getClass())) {
            Intent intent = new Intent(getActivity(), navigateTo);

            if (extraString != null) {
                intent.putExtra(extraString, extra);
            }

            if (navigateTo == HomeActivity.class) {
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }

            startActivity(intent);

            if (shouldFinishCurrentActivity) {
                getActivity().finish();
            }
        }
    }
}

