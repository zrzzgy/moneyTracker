package runze.moneytracker.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import runze.moneytracker.HomeActivity;
import runze.moneytracker.R;
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
        mListView = view.findViewById(R.id.categoryList);
        ((HomeActivity) getContext()).registerForContextMenu(mListView);
        mPlaceHolderText = view.findViewById(R.id.nullCategoryPlaceHolder);
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
}
