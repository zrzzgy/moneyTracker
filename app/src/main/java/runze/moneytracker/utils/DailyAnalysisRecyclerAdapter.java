package runze.moneytracker.utils;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import runze.moneytracker.R;
import runze.moneytracker.models.Expense;

public class DailyAnalysisRecyclerAdapter extends RecyclerView.Adapter<runze.moneytracker.utils.DailyAnalysisRecyclerAdapter.ViewHolder> {
    private List<Expense> mDataSet;


    public DailyAnalysisRecyclerAdapter(List<Expense> dataSet) {
        mDataSet = dataSet;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {     // One holder is one entry in the recycler view
        private TextView mAmountTextView;
        private TextView mCategoryTextView;
        private TextView mDateTextView;
        private TextView mDescription;

        ViewHolder(LinearLayout v) {
            super(v);
            mAmountTextView = v.findViewById(R.id.detailed_analysis_list_item_amount);
            mCategoryTextView = v.findViewById(R.id.detailed_analysis_list_item_category);
            mDateTextView = v.findViewById(R.id.detailed_analysis_list_item_date);
            mDescription = v.findViewById(R.id.detailed_analysis_list_item_description);
        }
    }

    @Override
    public runze.moneytracker.utils.DailyAnalysisRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detailed_analysis_list_item_layout, parent, false);
        return new runze.moneytracker.utils.DailyAnalysisRecyclerAdapter.ViewHolder(linearLayout);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(runze.moneytracker.utils.DailyAnalysisRecyclerAdapter.ViewHolder holder, int position) {
        Expense singleExpense = mDataSet.get(position);
        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());

        holder.mAmountTextView.setText(singleExpense.getAmount().toString());
        String categoriesList = singleExpense.getCategory().toString();
        holder.mCategoryTextView.setText(categoriesList.substring(1, categoriesList.length()-1));
        holder.mCategoryTextView.setVisibility(View.VISIBLE);
        holder.mDateTextView.setVisibility(View.GONE);
        holder.mDescription.setText(singleExpense.getDescription());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}

