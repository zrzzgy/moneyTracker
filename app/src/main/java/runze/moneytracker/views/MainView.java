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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import runze.moneytracker.HomeActivity;
import runze.moneytracker.R;
import runze.moneytracker.models.Expense;
import runze.moneytracker.presenters.IPresenter;
import runze.moneytracker.presenters.MainScreenPresenter;
import runze.moneytracker.utils.MainScreenRecyclerAdapter;
import runze.moneytracker.utils.RecyclerItemTouchHelper;

public class MainView extends RelativeLayout implements IView, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    private final String TAG = this.getClass().getSimpleName();

    private RecyclerView mExpenseList;
    private TextView mTotalView;
    private MainScreenPresenter mPresenter;

    private TextView mErrorMessage;
    private AlertDialog mAlertDialog;
    private View mAlertLayout;

    private MainScreenRecyclerAdapter mAdapter;


    public MainView(Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.home_screen_layout, this);
        init(view);
    }

    private void init(View view) {
        FloatingActionButton fab = view.findViewById(R.id.new_item_fab);
        fab.setOnClickListener(mFabListener);

        mExpenseList = view.findViewById(R.id.expense_list);
        mExpenseList.setHasFixedSize(true);

        mTotalView = view.findViewById(R.id.total_text_view);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mExpenseList.setLayoutManager(mLayoutManager);
    }

    public void updateView() {
        // populate the view with data
        mAdapter = new MainScreenRecyclerAdapter(mPresenter.getExpenses());
        mExpenseList.setAdapter(mAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mExpenseList);

        long total = mPresenter.calculateTotal();
        mTotalView.setText(String.format(getResources().getString(R.string.total_title), String.valueOf(total)));
    }

    @Override
    public void attachPresenter(IPresenter presenter) {
        if (presenter == null) {
            throw new IllegalArgumentException("presenter cannot be null.");
        }
        mPresenter = (MainScreenPresenter) presenter;
    }

    @Override
    public void detachPresenter() {
        mPresenter = null;
    }

    private OnClickListener mFabListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.v(TAG, "creating new item dialog");
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;

            mAlertLayout = inflater.inflate(R.layout.input_dialog, null);
            mAlertDialog = new AlertDialog.Builder(getContext())
                    .setView(mAlertLayout)
                    .setTitle(getResources().getString(R.string.input_dialog_title))
                    .setNegativeButton(getResources().getString(R.string.cancel), mCancelListener)
                    .setPositiveButton(getResources().getString(R.string.ok), null) // This
                    // is intended to make dialog not disappear if input is invalid
                    .create();

            mAlertDialog.setCanceledOnTouchOutside(false);
            mAlertDialog.setOnShowListener(mOnShowListener); // Make dialog not disappear if input is invalid
            mAlertDialog.show();

            //setup auto-complete for categories
            List<String> categories = new ArrayList<>(mPresenter.getCategories());
            ArrayAdapter<String> categoryAutoCompleteAdapter = new ArrayAdapter<>(getContext(), R.layout.category_auto_complte_drop_down_menu, categories);
            MultiAutoCompleteTextView categoryTextView = mAlertLayout.findViewById(R.id.input_category);
            categoryTextView.setAdapter(categoryAutoCompleteAdapter);
            categoryTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

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

            String rawStringAmount = ((EditText) mAlertLayout.findViewById(R.id.input_amount)).getText().toString();
            double amount = rawStringAmount.equals("") ?  -1 : Double.parseDouble(rawStringAmount);

            if (amount <= 0) {
                mErrorMessage.setText(getResources().getString(R.string.amount_invalid));
            } else {
                String categories = ((EditText) mAlertLayout.findViewById(R.id.input_category)).getText().toString();
                String description = ((EditText) mAlertLayout.findViewById(R.id.input_description)).getText().toString();
                if (!categories.isEmpty()) {
                    DatePicker datePicker = mAlertLayout.findViewById(R.id.date_picker);
                    int year = datePicker.getYear();
                    int month = datePicker.getMonth();
                    int day = datePicker.getDayOfMonth();
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, day);
                    Date date = calendar.getTime();
                    mPresenter.saveData(categories, amount, description, date);
                    ((HomeActivity) getContext()).persistDataAndUpload();
                    Log.v(TAG, "input validated, dismissing input dialog");
                    mAlertDialog.dismiss();
                    updateView();
                } else {
                    mErrorMessage.setText(getResources().getString(R.string.no_category_entered));
                }
            }
        }
    };


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, final int position) {
        if (viewHolder instanceof MainScreenRecyclerAdapter.ViewHolder) {

            // backup of removed item for undo purpose
            final Expense itemToDelete = mAdapter.getDataSet().get(position);

            // remove the item from both the recycler view and the sharedPreferences
            if (mPresenter.removeExpense(itemToDelete)) {
                Log.v(TAG, "Item removed: " + "\n" + itemToDelete.toString());
                ((HomeActivity) getContext()).persistDataAndUpload();
                updateView();
            } else {
                Log.e(TAG, "Error when removing item from preferences");
            }

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(this, getResources().getString(R.string.snack_bar_message), Snackbar.LENGTH_LONG);
            snackbar.setAction(getResources().getString(R.string.snack_bar_undo), new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    mPresenter.restoreDeletedItem();
                    // mAdapter.restoreItem(itemToDelete, position);
                    Log.v(TAG, "Item restored");
                    updateView();
                }
            });
            snackbar.setActionTextColor(android.graphics.Color.YELLOW);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)
                    snackbar.getView().getLayoutParams();
            params.setMargins(0, 0, 0, 0);
            snackbar.getView().setLayoutParams(params);
            snackbar.show();
        }
    }
}
