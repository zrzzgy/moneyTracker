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
import java.util.Map;
import java.util.Set;

import runze.moneytracker.R;
import runze.moneytracker.models.DailyExpenseTotal;


public class DaySummaryBarChartRecyclerAdapter extends RecyclerView.Adapter<DaySummaryBarChartRecyclerAdapter.ViewHolder> {
    private final String TAG = this.getClass().getSimpleName();
    private List<DailyExpenseTotal> mDataSet;

    static class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout mView;
        private TextView mBarDayTotal;
        private TextView mBarColorBlock;
        private TextView mBarDate;

        ViewHolder(LinearLayout v, ViewGroup viewGroup) {
            super(v);
            mView = v;
            mBarDayTotal = mView.findViewById(R.id.bar_chart_day_total);
            mBarColorBlock = mView.findViewById(R.id.bar_chart_color_block);
            mBarDate = mView.findViewById(R.id.bar_chart_date);
        }

    }

    public DaySummaryBarChartRecyclerAdapter(List<DailyExpenseTotal> dataSet) {
        mDataSet = dataSet;
    }

    @NonNull
    @Override
    public DaySummaryBarChartRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.daily_total_block, parent, false);
        return new DaySummaryBarChartRecyclerAdapter.ViewHolder(linearLayout, parent);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final DaySummaryBarChartRecyclerAdapter.ViewHolder holder, int position) {
        DailyExpenseTotal singleExpense = (DailyExpenseTotal) mDataSet.toArray()[position];
        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());

        holder.mBarDayTotal.setText(singleExpense.getTotalAmount().toString());
        holder.mBarDate.setText(df.format(singleExpense.getDate()));
        holder.mBarColorBlock.setHeight((int) (150 * singleExpense.getTotalAmount() / getMaxDailyTotal(mDataSet)));
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public double getMaxDailyTotal(List<DailyExpenseTotal> data) {
        double max = 0;

        for (DailyExpenseTotal daily: data) {
            if (max < daily.getTotalAmount()){
                max = daily.getTotalAmount();
            }
        }
        return max;
    }
}
