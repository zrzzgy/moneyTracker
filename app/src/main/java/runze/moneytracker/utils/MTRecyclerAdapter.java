package runze.moneytracker.utils;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import runze.moneytracker.R;
import runze.moneytracker.models.Expense;

/**
 *  Class used by the spending list recycler view
 */
public class MTRecyclerAdapter extends RecyclerView.Adapter<MTRecyclerAdapter.ViewHolder> {
    private final String TAG = this.getClass().getSimpleName();
    private List<Expense> mDataSet;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout mView;
        private TextView mAmountTextView;
        private TextView mCategoryTextView;
        private TextView mDateTextView;
        private TextView mDescriptionTextView;

        ViewHolder(LinearLayout v, ViewGroup viewGroup) {
            super(v);
            mView = v;
            mAmountTextView = mView.findViewById(R.id.list_amount);
            mCategoryTextView = mView.findViewById(R.id.list_category);
            mDateTextView = mView.findViewById(R.id.list_date);
            mDescriptionTextView = mView.findViewById(R.id.list_description);
        }

    }

    public MTRecyclerAdapter(List<Expense> dataSet) {
        mDataSet = dataSet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stats_table_item, parent, false);
        return new ViewHolder(linearLayout, parent);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Expense singleExpense = mDataSet.get(position);

        holder.mAmountTextView.setText(singleExpense.getAmount().toString());
        holder.mCategoryTextView.setText(singleExpense.getCategory());
        holder.mDateTextView.setText(singleExpense.getDate().toString());
        holder.mDescriptionTextView.setText(singleExpense.getDescription());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // on list item click
                //TODO add switch fragment
                Log.v(TAG, "Clicked " + view.getId());
                if (((TextView) view.findViewById(R.id.list_description)).getMaxLines() == 1){
                    ((TextView) view.findViewById(R.id.list_description)).setMaxLines(Integer.MAX_VALUE);
                }else{
                    ((TextView) view.findViewById(R.id.list_description)).setMaxLines(1);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
