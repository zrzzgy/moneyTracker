package runze.moneytracker.models;

import java.util.Date;

public class DailyExpenseTotal {
    private Double mTotalAmount;
    private Date mDate;

    public DailyExpenseTotal(Double amount, Date date){
        mTotalAmount = amount;
        mDate = date;
    }

    public Double getTotalAmount() {
        return mTotalAmount;
    }

    public void setTotalAmount(Double amount) {
        mTotalAmount = amount;
    }

    public Date getDate() {
        return mDate;
    }

}
