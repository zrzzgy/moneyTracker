package runze.moneytracker.models;

import java.util.Date;

public class BaseExpenseTotal {
    protected Double mTotalAmount;
    protected Date mDate;

    public BaseExpenseTotal(Double amount, Date date){
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
