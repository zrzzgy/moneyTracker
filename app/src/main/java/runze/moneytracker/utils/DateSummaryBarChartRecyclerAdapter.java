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
import runze.moneytracker.models.BaseExpenseTotal;
import runze.moneytracker.views.DayAnalysisView;

/**
 * Class used in the recycler view that shows daily spending bar chart
 */
public class DateSummaryBarChartRecyclerAdapter extends RecyclerView.Adapter<DateSummaryBarChartRecyclerAdapter.ViewHolder> {
    private final String TAG = this.getClass().getSimpleName();
    private List<? extends BaseExpenseTotal> mDataSet;

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

    public DateSummaryBarChartRecyclerAdapter(List<? extends BaseExpenseTotal> dataSet) {
        mDataSet = dataSet;
    }

    @NonNull
    @Override
    public DateSummaryBarChartRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.daily_expense_total_bar_chart_bar_layout, parent, false);
        return new DateSummaryBarChartRecyclerAdapter.ViewHolder(relativeLayout);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final DateSummaryBarChartRecyclerAdapter.ViewHolder holder, final int position) {
        // int position: the position of the ViewHolder item
        BaseExpenseTotal singleExpense = (BaseExpenseTotal) mDataSet.toArray()[position];
        SimpleDateFormat df = new SimpleDateFormat("dd", Locale.getDefault());

        holder.mBarDate.setText(df.format(singleExpense.getDate()));
        holder.mBarColorBlock.setHeight((int) (150 * singleExpense.getTotalAmount() / getMaxTotalInTimePeriod(mDataSet)));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // on list item click
                Log.v(TAG, "Clicked " + position);
                ((DayAnalysisView) view.getParent().getParent().getParent().getParent()).
                        setListOfSameDate(getItem(position).getDate());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mDataSet != null) {
            return mDataSet.size();
        }
        return 0;
    }

    public BaseExpenseTotal getItem(int index) {
        return mDataSet.get(index);
    }

    private double getMaxTotalInTimePeriod(List<? extends BaseExpenseTotal> data) {
        double max = 0;

        for (BaseExpenseTotal time : data) {
            if (max < time.getTotalAmount()) {
                max = time.getTotalAmount();
            }
        }
        return max;
    }


}
