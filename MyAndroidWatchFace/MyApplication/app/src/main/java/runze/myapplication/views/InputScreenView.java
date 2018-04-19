package runze.myapplication.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import runze.myapplication.R;
import runze.myapplication.presenters.inputScreenPresenter.IInputScreenPresenter;
import runze.myapplication.utils.Expense;
import runze.myapplication.utils.MTRecyclerAdapter;

public class InputScreenView extends RelativeLayout {
    private final String TAG = this.getClass().getSimpleName();

    private FloatingActionButton mFab;
    private RecyclerView mRecyclerView;
    private MTRecyclerAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private IInputScreenPresenter mPresenter;

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

    public void attachPresenter(IInputScreenPresenter presenter){
        if (presenter == null) {
            throw new IllegalArgumentException("presenter cannot be null.");
        }
        mPresenter = presenter;
    }

    public void detachPresenter(){
        mPresenter = null;
    }

    private View.OnClickListener mFabListener = new View.OnClickListener() {

        //fab click
        @Override
        public void onClick(View view) {
            Log.v(TAG, "creating new item dialog");
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            assert inflater != null;
            final View alertLayout = inflater.inflate(R.layout.input_dialog, null);
            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
            alertDialog.setView(alertLayout);
            alertDialog.setTitle(getResources().getString(R.string.input_dialog_title));
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {

                //Cancel dialog
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Log.v(TAG, "input dialog cancelled");
                    dialogInterface.cancel();
                }
            });

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {

                //Confirm dialog
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Log.v(TAG, "input dialog confirmed");

                    try {
                        Double amount = Double.parseDouble(((EditText) alertLayout.findViewById(R.id.inputAmount)).getText().toString());
                        String selectedCategory = (String) ((Spinner) alertLayout.findViewById(R.id.spinner)).getSelectedItem();
                        if (selectedCategory != null) {
                            mPresenter.saveData(selectedCategory, amount);
                            Log.v(TAG, "dismiss input dialog");
                            dialogInterface.dismiss();
                        }else{
                            Toast.makeText(getContext(), getResources().getString(R.string.no_category_selected), Toast.LENGTH_SHORT).show();
                        }
                    }catch (NumberFormatException e){
                        Toast.makeText(getContext(), getResources().getString(R.string.no_amount_entered), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            alertDialog.show();
        }
    };
}
