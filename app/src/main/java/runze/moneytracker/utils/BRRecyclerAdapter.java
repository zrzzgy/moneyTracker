package runze.moneytracker.utils;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import runze.moneytracker.R;
import runze.moneytracker.models.DailyExpenseTotal;


public class BRRecyclerAdapter extends RecyclerView.Adapter<BRRecyclerAdapter.ViewHolder> {
    private final String TAG = this.getClass().getSimpleName();
    private List<DailyExpenseTotal> mDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout mView;
        private TextView mBarDayTotal;
        private TextView mBarColorBlock;
        private TextView mBarDate;

        public ViewHolder(LinearLayout v, ViewGroup viewGroup) {
            super(v);
            mView = v;
            mBarDayTotal = mView.findViewById(R.id.bar_chart_day_total);
            mBarColorBlock = mView.findViewById(R.id.bar_chart_color_block);
            mBarDate = mView.findViewById(R.id.bar_chart_date);
        }

    }

    public BRRecyclerAdapter(List<DailyExpenseTotal> dataSet) {
        mDataSet = dataSet;
    }

    @NonNull
    @Override
    public BRRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.daily_total_block, parent, false);
        return new BRRecyclerAdapter.ViewHolder(linearLayout, parent);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final BRRecyclerAdapter.ViewHolder holder, int position) {
        DailyExpenseTotal singleExpense = mDataSet.get(position);
        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());

        holder.mBarDayTotal.setText(singleExpense.getTotalAmount().toString());
        holder.mBarDate.setText(df.format(singleExpense.getDate()));
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void loadItem(DailyExpenseTotal item, int position) {
        mDataSet.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

    public List<DailyExpenseTotal> getDataSet(){
        return mDataSet;
    }

}
