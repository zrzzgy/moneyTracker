package runze.myapplication.views.inputScreenView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import runze.myapplication.R;
import runze.myapplication.presenters.inputScreenPresenter.IInputScreenPresenter;

public class InputScreenView extends RelativeLayout implements IInputScreenView {
    private EditText mInputAmount;
    private Button mSubmit;

    private IInputScreenPresenter mPresenter;

    public InputScreenView(Context context){
        super(context);
        View v = LayoutInflater.from(context).inflate(R.layout.input_screen_layout, this);
        init(v);
    }

    private void init(View view){
        mInputAmount = view.findViewById(R.id.inputAmount);
        mSubmit = view.findViewById(R.id.submit);
        mSubmit.setOnClickListener(mOnClickListener);

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

    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                Double amount = Double.parseDouble(mInputAmount.getText().toString());
                mPresenter.saveData(amount);
                clearText();
            }catch (NumberFormatException e){
                Toast.makeText(getContext(), "Text cannot be empty", Toast.LENGTH_SHORT).show();
            }

        }
    };

    public void clearText(){
        mInputAmount.setText("");
    }
}
