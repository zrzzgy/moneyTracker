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
import android.widget.TextView;

import com.skydoves.colorpickerpreference.ColorEnvelope;
import com.skydoves.colorpickerpreference.ColorListener;
import com.skydoves.colorpickerpreference.ColorPickerDialog;

import runze.moneytracker.GoogleSignInActivity;
import runze.moneytracker.HomeActivity;
import runze.moneytracker.R;
import runze.moneytracker.presenters.IPresenter;
import runze.moneytracker.presenters.SettingsScreenPresenter;


/**
 *
 */
public class SettingsScreenView extends LinearLayout implements IView{
    private SettingsScreenPresenter mPresenter;
    private TextView mHelllo;
    private Button mChangeTheme;
    private Button mLogout;
    private  View mColorDialogView;
    private ColorPickerDialog mColorPickerDialog;
    private ContextThemeWrapper themeWrapper;

    public SettingsScreenView(Context context){
        super(context);
        themeWrapper = new ContextThemeWrapper(getContext(), R.style.AppTheme);
        LayoutInflater layoutInflater = LayoutInflater.from(themeWrapper);
        View v = layoutInflater.inflate(R.layout.settings_view_layout, this);
        init(v);
    }

    private void init(View view){
        mHelllo = view.findViewById(R.id.hello_user_header);
        mChangeTheme = view.findViewById(R.id.change_theme_button);
        mChangeTheme.setOnClickListener(mChangeThemeButtonListener);
        mLogout = view.findViewById(R.id.logout_button);
            }

    @Override
    public void attachPresenter(IPresenter presenter) {
        mPresenter = (SettingsScreenPresenter) presenter;
    }

    @Override
    public void detachPresenter() {
        mPresenter = null;
    }


    private OnClickListener mChangeThemeButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;

            mColorDialogView = inflater.inflate(R.layout.color_picker, null);
            ColorPickerDialog.Builder builder = new ColorPickerDialog.Builder(getContext(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            builder.setTitle("ColorPicker Dialog");
            builder.setPositiveButton("Ok", new ColorListener() {
                @Override
                public void onColorSelected(ColorEnvelope colorEnvelope) {
//                    Intent intent = new Intent();
//                    intent.putExtra("userName", currentUser);
//                    intent.setClass(getContext(),HomeActivity.class);
//                    getContext().startActivity(intent);
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
}
