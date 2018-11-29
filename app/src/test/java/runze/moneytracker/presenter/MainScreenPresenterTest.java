package runze.moneytracker.presenter;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import runze.moneytracker.models.DataModel;
import runze.moneytracker.models.Expense;
import runze.moneytracker.presenters.MainScreenPresenter;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MainScreenPresenterTest extends TestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testRemoveExpense() {
        List<Expense> expenseList = new ArrayList<>();
        HashSet<String> category = new HashSet<>();
        Expense expense = spy(new Expense(category, 3.3,new Date(), null));
        expenseList.add(expense);
        DataModel dataModel = new DataModel(expenseList,null,null,null);
        MainScreenPresenter mainScreenPresenter = new MainScreenPresenter(dataModel);

        mainScreenPresenter.removeExpense(expense);
        verify(expense,times(2)).getAmount();

    }
}
