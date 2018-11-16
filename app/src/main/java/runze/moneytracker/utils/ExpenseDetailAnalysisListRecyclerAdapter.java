package runze.moneytracker.utils;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import runze.moneytracker.R;
import runze.moneytracker.models.Expense;

public class ExpenseDetailAnalysisListRecyclerAdapter extends RecyclerView.Adapter<ExpenseDetailAnalysisListRecyclerAdapter.ViewHolder>{
    private List<Expense> mDataSet;


    public ExpenseDetailAnalysisListRecyclerAdapter(List<Expense> dataSet){
        mDataSet = dataSet;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mAmountTextView;
        private TextView mCategoryTextView;
        private TextView mDateTextView;

        ViewHolder(LinearLayout v) {
            super(v);
            mAmountTextView = v.findViewById(R.id.detailed_analysis_list_item_amount);
            mCategoryTextView = v.findViewById(R.id.detailed_analysis_list_item_category);
            mDateTextView = v.findViewById(R.id.detailed_analysis_list_item_date);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detailed_analysis_list_item_layout, parent, false);
        return new ViewHolder(linearLayout);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Expense singleExpense = mDataSet.get(position);
        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
        String categoriesList = Arrays.toString(singleExpense.getCategory().toArray());

        holder.mAmountTextView.setText(singleExpense.getAmount().toString());
        holder.mCategoryTextView.setText(categoriesList.substring(1, categoriesList.length()-1));
        holder.mDateTextView.setText(df.format(singleExpense.getDate()));
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
