package runze.moneytracker.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;

import runze.moneytracker.HomeActivity;
import runze.moneytracker.R;
import runze.moneytracker.models.MessageType;
import runze.moneytracker.presenters.IPresenter;
import runze.moneytracker.presenters.SettingsScreenPresenter;


public class SettingsScreenView extends LinearLayout implements IView{
    private SettingsScreenPresenter mPresenter;
    private ListView mListView;
    private TextView mPlaceHolderText;
    private EditText mNewCategoryText;
    private Button mAddButton;
    private ArrayAdapter<String> mAdapter;

    public SettingsScreenView(Context context){
        super(context);
        View v = LayoutInflater.from(context).inflate(R.layout.settings_view_layout, this);
        init(v);
    }

    private void init(View view){
        mListView = view.findViewById(R.id.category_list);
        ((HomeActivity) getContext()).registerForContextMenu(mListView);
        mPlaceHolderText = view.findViewById(R.id.null_category_place_holder);
        mNewCategoryText = view.findViewById(R.id.category_edit_text);
        mAddButton = view.findViewById(R.id.add_button);
        mAddButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.saveCategory(mNewCategoryText.getText().toString());
                mAdapter.notifyDataSetChanged();
                mNewCategoryText.setText("");
            }
        });
    }

    @Override
    public void attachPresenter(IPresenter presenter) {
        mPresenter = (SettingsScreenPresenter) presenter;
    }

    @Override
    public void detachPresenter() {
        mPresenter = null;
    }

    private void hideShowPlaceHolderText(HashSet<String> cate){
        if (!cate.isEmpty()){
            mPlaceHolderText.setVisibility(GONE);
        }else{
            mPlaceHolderText.setVisibility(VISIBLE);
        }
    }

    public void populateListView(HashSet<String> cate){
        hideShowPlaceHolderText(cate);
        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<>(getContext(), R.layout.settings_category_item, new ArrayList<>(cate));
            mListView.setAdapter(mAdapter);
        }else{
            mAdapter.clear();
            mAdapter.addAll(cate);
        }
    }

    public void makeToastMsg(MessageType messageType){
        String message = "";
        switch (messageType){
            case CATEGORY_EMPTY:
                message = getResources().getString(R.string.no_category_entered);
                break;
            case CATEGORY_EXISTS:
                message = getResources().getString(R.string.category_already_exists);
                break;
            case CATEGORY_SAVE_COMPLETE:
                message = getResources().getString(R.string.saved);
                break;
        }

        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void showEditDialog(final MenuItem item){
        final EditText input = new EditText(getContext());
        input.setText(item.getTitle().toString());
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle(R.string.edit);
        alertDialog.setView(input);
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getContext().getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getContext().getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mPresenter.replaceCategory(item.getTitle().toString(), input.getText().toString());
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }
}
