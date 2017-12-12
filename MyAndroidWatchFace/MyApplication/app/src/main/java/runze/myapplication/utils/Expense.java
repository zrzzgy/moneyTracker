package runze.myapplication.utils;

import java.util.Date;

/**
 * Created by zhengr2 on 12/7/2017.
 */

public class Expense {
    private String mCategory;
    private Double mAmount;
    private Date mDate;

    public Expense(String cate, Double amount, Date date){
        mCategory = cate;
        mAmount = amount;
        mDate = date;
    }

    public String getmCategory() {
        return mCategory;
    }

    public Double getmAmount() {
        return mAmount;
    }

    public Date getmDate() {
        return mDate;
    }
}
