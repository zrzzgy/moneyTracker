package runze.myapplication.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import runze.myapplication.HomeActivity;
import runze.myapplication.R;
import runze.myapplication.presenters.inputScreenPresenter.IInputScreenPresenter;

public class InputScreenView extends RelativeLayout {
    private final String TAG = this.getClass().getSimpleName();
//    private EditText mInputAmount;
//    private Button mSubmit;
//    private Spinner mSpinner;
    private FloatingActionButton mFab;

    private IInputScreenPresenter mPresenter;

    public InputScreenView(Context context){
        super(context);
        View v = LayoutInflater.from(context).inflate(R.layout.input_view_layout, this);
        init(v);
    }

    private void init(View view){
//        mInputAmount = view.findViewById(R.id.inputAmount);
//        mSpinner = view.findViewById(R.id.spinner);
//        mSubmit = view.findViewById(R.id.submit);
        mFab = view.findViewById(R.id.newItemFab);
        mFab.setOnClickListener(mFabListener);
//        mSubmit.setOnClickListener(mOnClickListener);
    }

    public void attachPresenter(IInputScreenPresenter presenter){
        if (presenter == null) {
            throw new IllegalArgumentException("presenter cannot be null.");
        }
        mPresenter = presenter;
    }

    public void detachPresenter(){
        mPresenter = null;
    }

//    private OnClickListener mOnClickListener = new OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            try {
//                Double amount = Double.parseDouble(mInputAmount.getText().toString());
//                if (mSpinner.getSelectedItem() != null) {
//                    mPresenter.saveData(mSpinner.getSelectedItem().toString(), amount);
//                    clearText();
//                }else{
//                    Toast.makeText(getContext(), getResources().getString(R.string.no_category_selected), Toast.LENGTH_SHORT).show();
//
//                }
//            }catch (NumberFormatException e){
//                Toast.makeText(getContext(), getResources().getString(R.string.no_text_entered), Toast.LENGTH_SHORT).show();
//            }
//
//        }
//    };

    private View.OnClickListener mFabListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            View alertLayout = inflater.inflate(R.layout.input_dialog, null);
            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
            alertDialog.setView(alertLayout);
            alertDialog.setTitle("Add new");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getContext().getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getContext().getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            alertDialog.show();

        }
    };


//    @Override
//    public void updateSpinner(ArrayAdapter<String> adapter){
//        mSpinner.setAdapter(adapter);
//    }
//
//    @Override
//    public void clearText(){
//        mInputAmount.setText("");
//    }
//
//    @Override
//    public int getSpinnerIndex(){
//        return mSpinner.getSelectedItemPosition();
//    }
//
//    @Override
//    public void setSpinnerIndex(int index){
//        mSpinner.setSelection(index);
//    }
}
