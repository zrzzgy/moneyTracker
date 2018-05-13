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
import android.widget.Toast;

import java.util.ArrayList;

import runze.moneytracker.R;
import runze.moneytracker.presenters.IPresenter;
import runze.moneytracker.presenters.InputScreenPresenter;
import runze.moneytracker.utils.Expense;
import runze.moneytracker.utils.MTRecyclerAdapter;

public class InputScreenView extends RelativeLayout implements IView{
    private final String TAG = this.getClass().getSimpleName();

    private FloatingActionButton mFab;
    private RecyclerView mRecyclerView;
    private MTRecyclerAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private AlertDialog mAlertDialog;
    private  View mAlertLayout;

    private InputScreenPresenter mPresenter;

    public InputScreenView(Context context){
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.input_view_layout, this);
        init(view);
    }

    private void init(View view){

        mFab = view.findViewById(R.id.new_item_fab);
        mFab.setOnClickListener(mFabListener);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MTRecyclerAdapter(new ArrayList<Expense>());
        mRecyclerView.setAdapter(mAdapter);

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
                    .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {

                        //Cancel dialog
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Log.v(TAG, "input dialog cancelled");
                            dialogInterface.cancel();
                        }
                    })
                    .setPositiveButton(getResources().getString(R.string.ok), null)
                    .create();
            mAlertDialog.setCanceledOnTouchOutside(false);

            mAlertDialog.setOnShowListener(mOnShowListener);
            mAlertDialog.show();
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
            Log.v(TAG, "input dialog confirmed");
            try {
                Double amount = Double.parseDouble(((EditText) mAlertLayout.findViewById(R.id.inputAmount)).getText().toString());
                String selectedCategory = (String) ((Spinner) mAlertLayout.findViewById(R.id.spinner)).getSelectedItem();
                if (selectedCategory != null) {
                    mPresenter.saveData(selectedCategory, amount);
                    Log.v(TAG, "input valid, dismissing input dialog");
                    mAlertDialog.dismiss();
                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.no_category_selected), Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), getResources().getString(R.string.no_amount_entered), Toast.LENGTH_SHORT).show();
            }
        }
    };
}
