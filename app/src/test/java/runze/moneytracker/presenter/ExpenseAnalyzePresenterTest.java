package runze.moneytracker.presenter;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import runze.moneytracker.models.DailyExpenseTotal;
import runze.moneytracker.models.DataModel;
import runze.moneytracker.models.Expense;
import runze.moneytracker.presenters.ExpenseAnalyzePresenter;

public class ExpenseAnalyzePresenterTest extends TestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testDateAsKey() {
        HashSet<String> category = new HashSet<>();
        Double amount = 2.2;

        Calendar calendar = Calendar.getInstance();
        calendar.set(118, 10, 24);
        Date date2 = calendar.getTime();
        calendar.set(118,10,26);
        Date date1 = calendar.getTime();
        calendar.set(118,10,25);
        Date date3 = calendar.getTime();

        Expense expense1 = new Expense(category, amount, date1, null);
        Expense expense2 = new Expense(category, amount, date2,null);
        List<Expense> expenseList = new ArrayList<>();
        expenseList.add(expense1);
        expenseList.add(expense2);

        DailyExpenseTotal dailyExpenseTotal1 = new DailyExpenseTotal(2.2, date1);
        DailyExpenseTotal dailyExpenseTotal2 = new DailyExpenseTotal((double)0, date3);
        DailyExpenseTotal dailyExpenseTotal3 = new DailyExpenseTotal(2.2, date2);

        DataModel dataModel = new DataModel(expenseList,null,null,null);
        ExpenseAnalyzePresenter expenseAnalyzePresenter = new ExpenseAnalyzePresenter(dataModel);

        List<DailyExpenseTotal> dailyExpenseTotalListResult = expenseAnalyzePresenter.sortDataForDayAnalysis();
        assertEquals(dailyExpenseTotal1.getDate(), dailyExpenseTotalListResult.get(0).getDate());
        assertEquals(dailyExpenseTotal2.getDate(), dailyExpenseTotalListResult.get(1).getDate());
        assertEquals(dailyExpenseTotal3.getDate(), dailyExpenseTotalListResult.get(2).getDate());
        assertEquals(dailyExpenseTotal1.getTotalAmount(), dailyExpenseTotalListResult.get(0).getTotalAmount());
        assertEquals(dailyExpenseTotal2.getTotalAmount(), dailyExpenseTotalListResult.get(1).getTotalAmount());
        assertEquals(dailyExpenseTotal3.getTotalAmount(), dailyExpenseTotalListResult.get(2).getTotalAmount());
     }
}
