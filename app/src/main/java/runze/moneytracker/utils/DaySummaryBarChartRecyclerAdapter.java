package runze.moneytracker.utils;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import runze.moneytracker.R;
import runze.moneytracker.models.DailyExpenseTotal;
import runze.moneytracker.views.DayAnalysisView;

/**
 * Class used in the recycler view that shows daily spending bar chart
 */
public class DaySummaryBarChartRecyclerAdapter extends RecyclerView.Adapter<DaySummaryBarChartRecyclerAdapter.ViewHolder> {
    private final String TAG = this.getClass().getSimpleName();
    private List<DailyExpenseTotal> mDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout mView;
        private TextView mBarColorBlock;
        private TextView mBarDate;

        ViewHolder(RelativeLayout v) {
            super(v);
            mView = v;
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
        RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.daily_expense_total_bar_chart_bar_layout, parent, false);
        return new DaySummaryBarChartRecyclerAdapter.ViewHolder(relativeLayout);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final DaySummaryBarChartRecyclerAdapter.ViewHolder holder, final int position) {
        // int position: the position of the ViewHolder item
        DailyExpenseTotal singleExpense = (DailyExpenseTotal) mDataSet.toArray()[position];
        SimpleDateFormat df = new SimpleDateFormat("dd", Locale.getDefault());

        holder.mBarDate.setText(df.format(singleExpense.getDate()));
        holder.mBarColorBlock.setHeight((int) (150 * singleExpense.getTotalAmount() / getMaxDailyTotal(mDataSet)));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // on list item click
                Log.v(TAG, "Clicked " + position);
                ((DayAnalysisView) view.getParent().getParent().getParent().getParent()).
                        setListOfSameDay(getItem(position).getDate());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public DailyExpenseTotal getItem(int index) {
        return mDataSet.get(index);
    }

    private double getMaxDailyTotal(List<DailyExpenseTotal> data) {
        double max = 0;

        for (DailyExpenseTotal daily : data) {
            if (max < daily.getTotalAmount()) {
                max = daily.getTotalAmount();
            }
        }
        return max;
    }


}
