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
import android.widget.RelativeLayout;

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
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout viewBackground;
        public LinearLayout viewForeground;
        private LinearLayout mView;
        private TextView mAmountTextView;
        private TextView mCategoryTextView;
        private TextView mDateTextView;
        private TextView mDescriptionTextView;

        public ViewHolder(LinearLayout v, ViewGroup viewGroup) {
            super(v);
            mView = v;
            mAmountTextView = mView.findViewById(R.id.list_amount);
            mCategoryTextView = mView.findViewById(R.id.list_category);
            mDateTextView = mView.findViewById(R.id.list_date);
            mDescriptionTextView = mView.findViewById(R.id.list_description);
            viewBackground = mView.findViewById(R.id.view_background);
            viewForeground = mView.findViewById(R.id.view_foreground);
        }

    }

    public MTRecyclerAdapter(List<Expense> dataSet) {
        mDataSet = dataSet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expense_block, parent, false);
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

    public void removeItem(int position) {
        mDataSet.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(Expense item, int position) {
        mDataSet.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }
}
