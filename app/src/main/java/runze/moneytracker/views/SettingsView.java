package runze.moneytracker.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.skydoves.colorpickerpreference.ColorEnvelope;
import com.skydoves.colorpickerpreference.ColorListener;
import com.skydoves.colorpickerpreference.ColorPickerDialog;

import runze.moneytracker.HomeActivity;
import runze.moneytracker.R;
import runze.moneytracker.SignInActivity;
import runze.moneytracker.presenters.IPresenter;
import runze.moneytracker.presenters.SettingsPresenter;


/**
 *
 */
public class SettingsView extends LinearLayout implements IView{
    private SettingsPresenter mPresenter;
    private Button mChangeThemeButton;
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
        mChangeThemeButton = view.findViewById(R.id.change_theme_button);
        mLogoutButton = view.findViewById(R.id.logout_button);
        mAboutButton = view.findViewById(R.id.about_button);

        mChangeThemeButton.setOnClickListener(mChangeThemeButtonListener);
    }

    @Override
    public void attachPresenter(IPresenter presenter) {
        mPresenter = (SettingsPresenter) presenter;
    }

    @Override
    public void detachPresenter() {
        mPresenter = null;
    }


    private OnClickListener mChangeThemeButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ColorPickerDialog.Builder builder = new ColorPickerDialog.Builder(getContext(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            builder.setTitle("ColorPicker Dialog");
            builder.setPositiveButton("Ok", new ColorListener() {
                @Override
                public void onColorSelected(ColorEnvelope colorEnvelope) {
                    setBackgroundColor(colorEnvelope.getColor());

                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
           builder.show();
        }
    };

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
}
