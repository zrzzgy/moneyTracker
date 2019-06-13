package runze.moneytracker.utils;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import runze.moneytracker.R;
import runze.moneytracker.models.Expense;

public class CategoryAnalysisRecyclerAdapter extends RecyclerView.Adapter<CategoryAnalysisRecyclerAdapter.ViewHolder>{
    private List<Expense> mDataSet;


    public CategoryAnalysisRecyclerAdapter(List<Expense> dataSet){
        mDataSet = dataSet;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {     // One holder is one entry in the recycler view
        private TextView mAmountTextView;
        private TextView mCategoryTextView;
        private TextView mDateTextView;
        private TextView mDescriptionView;

        ViewHolder(RelativeLayout v) {
            super(v);
            mAmountTextView = v.findViewById(R.id.detailed_analysis_list_item_amount);
            mCategoryTextView = v.findViewById(R.id.detailed_analysis_list_item_category);
            mDateTextView = v.findViewById(R.id.detailed_analysis_list_item_date);
            mDescriptionView = v.findViewById(R.id.detailed_analysis_list_item_description);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detailed_analysis_list_item_layout, parent, false);
        return new ViewHolder(relativeLayout);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Expense singleExpense = mDataSet.get(position);
        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());

        holder.mAmountTextView.setText(singleExpense.getAmount().toString());
        holder.mCategoryTextView.setVisibility(View.GONE);
        holder.mDateTextView.setText(df.format(singleExpense.getDate()));
        holder.mDateTextView.setVisibility(View.VISIBLE);
        holder.mDescriptionView.setText(singleExpense.getDescription());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
