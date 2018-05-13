package runze.moneytracker.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import runze.moneytracker.R;
import runze.moneytracker.presenters.IPresenter;
import runze.moneytracker.presenters.InputScreenPresenter;
import runze.moneytracker.utils.Expense;
import runze.moneytracker.utils.MTRecyclerAdapter;

public class InputScreenView extends RelativeLayout implements IView{
    private final String TAG = this.getClass().getSimpleName();

    private RecyclerView mRecyclerView;
    private TextView mErrorMessage;
    private AlertDialog mAlertDialog;
    private  View mAlertLayout;

    private InputScreenPresenter mPresenter;

    public InputScreenView(Context context){
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.input_view_layout, this);
        init(view);
    }

    private void init(View view){
        FloatingActionButton fab = view.findViewById(R.id.new_item_fab);
        fab.setOnClickListener(mFabListener);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    public void updateViewWithData(){
        // populate the view with data
        updateRecyclerViewWithData();
    }

    @Override
    public void attachPresenter(IPresenter presenter){
        if (presenter == null) {
            throw new IllegalArgumentException("presenter cannot be null.");
        }
        mPresenter = (InputScreenPresenter) presenter;
    }

    @Override
    public void detachPresenter(){
        mPresenter = null;
    }

    private OnClickListener mFabListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.v(TAG, "creating new item dialog");
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            mAlertLayout = inflater.inflate(R.layout.input_dialog, null);
            mAlertDialog  = new AlertDialog.Builder(getContext())
                    .setView(mAlertLayout)
                    .setTitle(getResources().getString(R.string.input_dialog_title))
                    .setNegativeButton(getResources().getString(R.string.cancel), mCancelListener)
                    .setPositiveButton(getResources().getString(R.string.ok), null)
                    .create();

            mAlertDialog.setCanceledOnTouchOutside(false);
            mAlertDialog.setOnShowListener(mOnShowListener);
            mAlertDialog.show();

            mErrorMessage = mAlertLayout.findViewById(R.id.error_message);
        }
    };

    private DialogInterface.OnClickListener mCancelListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            Log.v(TAG, "input dialog cancelled");
            dialogInterface.cancel();
        }
    };

    private DialogInterface.OnShowListener mOnShowListener = new DialogInterface.OnShowListener() {
        //Confirm dialog
        @Override
        public void onShow(DialogInterface dialogInterface) {
            Button confirmButton = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            confirmButton.setOnClickListener(mInputDialogConfirmListener);
        }
    };

    private OnClickListener mInputDialogConfirmListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.v(TAG, "confirming input dialog");

            Double amount = Double.parseDouble(((EditText) mAlertLayout.findViewById(R.id.inputAmount)).getText().toString());
            if (amount <= 0){
                mErrorMessage.setText(getResources().getString(R.string.amount_invalid));
            }else {
                String selectedCategory = ((EditText) mAlertLayout.findViewById(R.id.inputCategory)).getText().toString();
                if (!selectedCategory.isEmpty()) {
                    mPresenter.saveData(selectedCategory, amount);
                    Log.v(TAG, "input validated, dismissing input dialog");
                    mAlertDialog.dismiss();
                    updateRecyclerViewWithData();
                } else {
                    mErrorMessage.setText(getResources().getString(R.string.no_category_entered));
                }
            }
        }
    };

    private void updateRecyclerViewWithData(){
        MTRecyclerAdapter mAdapter = new MTRecyclerAdapter(mPresenter.loadData());
        mRecyclerView.setAdapter(mAdapter);
    }
}
