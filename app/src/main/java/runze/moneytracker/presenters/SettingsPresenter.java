package runze.moneytracker.presenters;

import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import runze.moneytracker.models.DataModel;
import runze.moneytracker.views.AboutView;
import runze.moneytracker.views.IView;
import runze.moneytracker.views.SettingsView;

public class SettingsPresenter implements IPresenter {
    private final String TAG = this.getClass().getSimpleName();
    private SettingsView mView;

    public void updateView() {
        // Not used
    }

    @Override
    public void attachView(IView view) {
        mView = (SettingsView) view;
        mView.setLogOutListener(mLogOutListener);
        mView.setAboutListener(mAboutListener);
    }

    @Override
    public void detachView() {
        mView = null;
    }

    private View.OnClickListener mLogOutListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.v(TAG, "Logging out");
            ((SettingsView) view.getParent().getParent()).signOut();
        }
    };

    private View.OnClickListener mAboutListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.v(TAG, "About button clicked");
            mView.navigateToAboutScreen();
        }
    };
}
