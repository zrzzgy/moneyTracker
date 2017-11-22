package runze.myapplication.views.inputScreenView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;

import runze.myapplication.R;
import runze.myapplication.presenters.inputScreenPresenter.IInputScreenPresenter;

public class InputScreenView extends RelativeLayout implements IInputScreenView {
    public NumberPicker mThousandsPicker;
    public NumberPicker mHundredsPicker;
    public NumberPicker mTensPicker;
    public NumberPicker mOnesPicker;
    public Button mSubmit;

    private IInputScreenPresenter mPresenter;

    public InputScreenView(Context context){
        super(context);
        View v = LayoutInflater.from(context).inflate(R.layout.input_screen_layout, this);
        init(v);
    }

    private void init(View view){
        mThousandsPicker = view.findViewById(R.id.thousandsPicker);
        mHundredsPicker = view.findViewById(R.id.hundredsPicker);
        mTensPicker = view.findViewById(R.id.tensPicker);
        mOnesPicker = view.findViewById(R.id.onesPicker);
        mSubmit = view.findViewById(R.id.submit);
        mSubmit.setOnClickListener(mOnClickListener);

        mThousandsPicker.setMaxValue(9);
        mThousandsPicker.setMinValue(0);
        mThousandsPicker.setWrapSelectorWheel(true);
        mThousandsPicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker numberPicker, int i) {
                numberPicker.setHapticFeedbackEnabled(true);
                numberPicker.performHapticFeedback(5);
            }
        });
        mHundredsPicker.setMaxValue(9);
        mHundredsPicker.setMinValue(0);
        mHundredsPicker.setWrapSelectorWheel(true);
        mHundredsPicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker numberPicker, int i) {
                numberPicker.setHapticFeedbackEnabled(true);
                numberPicker.performHapticFeedback(5);
            }
        });
        mTensPicker.setMaxValue(9);
        mTensPicker.setMinValue(0);
        mTensPicker.setWrapSelectorWheel(true);
        mTensPicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker numberPicker, int i) {
                numberPicker.setHapticFeedbackEnabled(true);
                numberPicker.performHapticFeedback(5);
            }
        });
        mOnesPicker.setMaxValue(9);
        mOnesPicker.setMinValue(0);
        mOnesPicker.setWrapSelectorWheel(true);
        mOnesPicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker numberPicker, int i) {
                numberPicker.setHapticFeedbackEnabled(true);
                numberPicker.performHapticFeedback(5);
            }
        });
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
            mPresenter.saveData();
        }
    };
}
