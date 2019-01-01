package runze.moneytracker.models;

public class UnsyncedExpense {
    private Expense mExpense;
    private boolean mIsAdd;

    public UnsyncedExpense(Expense expense, boolean isAdd) {
        mExpense = expense;
        mIsAdd = isAdd;
    }

    public Expense getExpense() {
        return mExpense;
    }

    public boolean isAdd() {
        return mIsAdd;
    }

}
