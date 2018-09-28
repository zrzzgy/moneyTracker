package runze.moneytracker.models;

import java.util.Date;

/**
 * Class that records date and the total amount of money spent on that date
 */
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
