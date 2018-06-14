package runze.moneytracker.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import runze.moneytracker.R;
import runze.moneytracker.models.Expense;
import runze.moneytracker.presenters.IPresenter;
import runze.moneytracker.presenters.InputScreenPresenter;
import runze.moneytracker.utils.MTRecyclerAdapter;
import runze.moneytracker.utils.RecyclerItemTouchHelper;

public class InputScreenView extends RelativeLayout implements IView, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{
    private final String TAG = this.getClass().getSimpleName();

    private RecyclerView mRecyclerView;
    private InputScreenPresenter mPresenter;

    private TextView mErrorMessage;
    private AlertDialog mAlertDialog;
    private  View mAlertLayout;

    private MTRecyclerAdapter mAdapter;

    public InputScreenView(Context context){
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.home_base, this);
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
                String description = ((EditText) mAlertLayout.findViewById(R.id.inputDescription)).getText().toString();
                if (!selectedCategory.isEmpty()) {
                    mPresenter.saveData(selectedCategory, amount, description);
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
        mAdapter = new MTRecyclerAdapter(mPresenter.loadData());
        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, final int position) {
        if (viewHolder instanceof MTRecyclerAdapter.ViewHolder) {

            // backup of removed item for undo purpose
            final Expense deletedItem = mAdapter.getDataSet().get(position);

            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(this, "Item Removed", Snackbar.LENGTH_SHORT);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    mAdapter.restoreItem(deletedItem, position);
                }
            });
            snackbar.setActionTextColor(android.graphics.Color.YELLOW);
            snackbar.show();
        }
    }
}
