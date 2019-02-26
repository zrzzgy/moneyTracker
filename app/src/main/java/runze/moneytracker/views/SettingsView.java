package runze.moneytracker.views;

import android.content.Context;
import android.content.Intent;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import runze.moneytracker.HomeActivity;
import runze.moneytracker.R;
import runze.moneytracker.SignInActivity;
import runze.moneytracker.fragments.SettingsScreenFragment;
import runze.moneytracker.presenters.IPresenter;
import runze.moneytracker.presenters.SettingsPresenter;


/**
 *
 */
public class SettingsView extends LinearLayout implements IView{
    private SettingsPresenter mPresenter;
    private Button mLogoutButton;
    private Button mAboutButton;
    private ContextThemeWrapper themeWrapper;

    public SettingsView(Context context){
        super(context);
        themeWrapper = new ContextThemeWrapper(getContext(), R.style.AppTheme);
        LayoutInflater layoutInflater = LayoutInflater.from(themeWrapper);
        View v = layoutInflater.inflate(R.layout.settings_view_layout, this);
        init(v);
    }

    private void init(View view){
        mLogoutButton = view.findViewById(R.id.logout_button);
        mAboutButton = view.findViewById(R.id.about_button);
    }

    @Override
    public void attachPresenter(IPresenter presenter) {
        mPresenter = (SettingsPresenter) presenter;
    }

    @Override
    public void detachPresenter() {
        mPresenter = null;
    }

    public void setLogOutListener(OnClickListener listener){
        mLogoutButton.setOnClickListener(listener);
    }

    public void setAboutListener(OnClickListener listener){
        mAboutButton.setOnClickListener(listener);
    }

    public void signOut(){
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getResources().getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getContext(), gso);
        googleSignInClient.signOut();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();

        Intent intent = new Intent();
        intent.setClass(getContext(), SignInActivity.class);
        getContext().startActivity(intent);
        ((HomeActivity) getContext()).finish();
    }

    public void navigateToAboutScreen(){
         (( SettingsScreenFragment) ((HomeActivity) getContext()).getCurrentFragment()).navigateAboutScreen();
    }
}
