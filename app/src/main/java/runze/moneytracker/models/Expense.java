package runze.moneytracker.models;

import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

/**
 * Object that saves categories, amount, date, and description of a spending
 */
public class Expense {
    private HashSet<String> mCategories;
    private Double mAmount;
    private Date mDate;
    private String mDescription;
    private String mChildId;

    public Expense(@NonNull HashSet<String> category, @NonNull Double amount, @NonNull Date date, String description){
        mCategories = category;
        mAmount = amount;
        mDate = date;
        mDescription = description == null ?  "" : description;
    }

    public HashSet<String> getCategory() {
        return mCategories;
    }

    public String getChildId() {
        return mChildId;
    }

    public void setChildId(String childId) {
        mChildId = childId;
    }

    public void setCategory(HashSet<String> newCategory){
        mCategories = newCategory;
    }

    public Double getAmount() {
        return mAmount;
    }

    public void setAmount(Double amount) {
        mAmount = amount;
    }

    public Date getDate() {
        return mDate;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getDay() {
        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
        return df.format(mDate);
    }

    public ArrayList<Integer> getWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(mDate);
        int weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);
        int year = cal.get(Calendar.YEAR);
        ArrayList<Integer> weekAndYear = new ArrayList<>();
        weekAndYear.add(0, weekOfYear);
        weekAndYear.add(1, year);
        return weekAndYear;
    }

    public String getMonth() {
        SimpleDateFormat df = new SimpleDateFormat("MM-yyyy", Locale.getDefault());
        return df.format(mDate);
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public boolean isSameExpense(Expense expense){
        return this.getAmount().equals(expense.getAmount()) &&
                this.getCategory().equals(expense.getCategory()) &&
                this.getDescription().equals(expense.getDescription()) &&
                this.getDate().equals(expense.getDate());
    }

    @Override
    public String toString() {
        return "Amount: " + mAmount.toString() + "\n" +
                "Category: " + mCategories.toString() + "\n" +
                "Date: " + getDate().toString();
    }
}
