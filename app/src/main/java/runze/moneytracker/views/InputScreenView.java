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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import runze.moneytracker.R;
import runze.moneytracker.models.Expense;
import runze.moneytracker.presenters.IPresenter;
import runze.moneytracker.presenters.InputScreenPresenter;
import runze.moneytracker.utils.MainScreenRecyclerAdapter;
import runze.moneytracker.utils.RecyclerItemTouchHelper;

public class InputScreenView extends RelativeLayout implements IView, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{
    private final String TAG = this.getClass().getSimpleName();

    private RecyclerView mRecyclerView;
    private TextView mTotalView;
    private InputScreenPresenter mPresenter;

    private TextView mErrorMessage;
    private AlertDialog mAlertDialog;
    private  View mAlertLayout;

    private MainScreenRecyclerAdapter mAdapter;

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

        mTotalView = view.findViewById(R.id.total_text_view);

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
                    .setPositiveButton(getResources().getString(R.string.ok), mInputDialogConfirmListener)
                    .create();

            mAlertDialog.setCanceledOnTouchOutside(false);
            mAlertDialog.show();

            //setup auto-complete for categories
            List<String> categories = new ArrayList<>(mPresenter.loadCategoriesFromDataModel());
            ArrayAdapter<String> categoryAutoCompleteAdapter = new ArrayAdapter<>(getContext(), R.layout.drop_down_menu, categories);
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


    private DialogInterface.OnClickListener mInputDialogConfirmListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            Log.v(TAG, "confirming input dialog");

            Double amount = Double.parseDouble(((EditText) mAlertLayout.findViewById(R.id.input_amount)).getText().toString());
            if (amount <= 0){
                mErrorMessage.setText(getResources().getString(R.string.amount_invalid));
            }else {
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
        mAdapter = new MainScreenRecyclerAdapter(mPresenter.loadExpensesFromDataModel());
        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);

        long total = mPresenter.calculateTotal();
        mTotalView.setText(String.format(getResources().getString(R.string.total_title), String.valueOf(total)));
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, final int position) {
        if (viewHolder instanceof MainScreenRecyclerAdapter.ViewHolder) {

            // backup of removed item for undo purpose
            final Expense itemToDelete = mAdapter.getDataSet().get(position);

            // remove the item from both the recycler view and the sharedPreferences
            if (mPresenter.removeExpenseFromPreferences(itemToDelete)){
                mAdapter.removeItem(viewHolder.getAdapterPosition());
                Log.v(TAG, "Item removed");
            }else{
                Log.e(TAG, "Error when removing item from preferences");
            }

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(this, getResources().getString(R.string.snack_bar_message), Snackbar.LENGTH_LONG);
            snackbar.setAction(getResources().getString(R.string.snack_bar_undo), new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    if (mPresenter.restoreDeletedItem()){
                        mAdapter.restoreItem(itemToDelete, position);
                        Log.v(TAG, "Item restored");
                    }else{
                        Log.e(TAG, "Error when restoring item to preferences");
                    }
                }
            });
            snackbar.setActionTextColor(android.graphics.Color.YELLOW);
            snackbar.show();
        }
    }
}
