package runze.myapplication.utils;

/**
 * Created by zhengr2 on 12/7/2017.
 */

public class Expense {
    private String mCategory;
    private Double mAmount;

    public Expense(){
        mCategory = "";
        mAmount = 0.00;
    }

    public Expense(String cate, Double amount){
        mCategory = cate;
        mAmount = amount;
    }

    public String getmCategory() {
        return mCategory;
    }

    public void setmCategory(String mCategory) {
        this.mCategory = mCategory;
    }

    public Double getmAmount() {
        return mAmount;
    }

    public void setmAmount(Double mAmount) {
        this.mAmount = mAmount;
    }
}
