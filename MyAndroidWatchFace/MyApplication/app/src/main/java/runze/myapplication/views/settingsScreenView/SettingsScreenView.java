package runze.myapplication.views.settingsScreenView;

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
import java.util.List;
import java.util.Set;

import runze.myapplication.R;
import runze.myapplication.presenters.IPresenter;
import runze.myapplication.presenters.settingsScreenPresenter.ISettingsScreenPresenter;


public class SettingsScreenView extends LinearLayout implements ISettingsScreenView {
    private ISettingsScreenPresenter mPresenter;
    private ListView mListView;
    private TextView mPlaceHolderText;
    private EditText mNewCategoryText;
    private Button mAddButton;
    private ArrayAdapter<String> mAdapter;

    public SettingsScreenView(Context context){
        super(context);
        View v = LayoutInflater.from(context).inflate(R.layout.settings_screen_layout, this);
        init(v);
    }

    private void init(View view){
        mListView = view.findViewById(R.id.categoryList);
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
        mPresenter = (ISettingsScreenPresenter) presenter;
    }

    @Override
    public void detachPresenter() {
        mPresenter = null;
    }

    private void hideShowPlaceHolderText(Set<String> cate){
        if (!cate.isEmpty()){
            mPlaceHolderText.setVisibility(GONE);
        }else{
            mPlaceHolderText.setVisibility(VISIBLE);
        }
    }

    public void populateListView(Set<String> cate){
        hideShowPlaceHolderText(cate);
        if (cate != null) {
            if (mAdapter == null) {
                mAdapter = new ArrayAdapter<>(getContext(), R.layout.category_item, new ArrayList<>(cate));
                mListView.setAdapter(mAdapter);
            }else{
                mAdapter.clear();
                mAdapter.addAll(cate);
            }
        }
    }
}
