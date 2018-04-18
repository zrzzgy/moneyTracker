package runze.myapplication.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

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
        View v = LayoutInflater.from(context).inflate(R.layout.input_view_layout, this);
        init(v);
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
}
