package runze.moneytracker.dependencyinjection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import runze.moneytracker.fragments.ExpenseAnalysisFragment;
import runze.moneytracker.fragments.MainScreenFragment;
import runze.moneytracker.fragments.SettingsScreenFragment;
import runze.moneytracker.models.DailyExpenseTotal;
import runze.moneytracker.models.DataModel;
import runze.moneytracker.models.Expense;
import runze.moneytracker.models.UnsyncedExpense;
import runze.moneytracker.presenters.ExpenseAnalyzePresenter;
import runze.moneytracker.presenters.MainScreenPresenter;
import runze.moneytracker.presenters.SettingsPresenter;

/**
 * App Module for dependency injection n
 */
@Module
public class AppModule {
    @Provides
    @Singleton   // static
    MainScreenFragment provideInputScreenFragment(){
        return new MainScreenFragment();
    }

    @Provides
    @Singleton
    ExpenseAnalysisFragment provideStatsScreenFragment(){
        return new ExpenseAnalysisFragment();
    }

    @Provides
    @Singleton
    SettingsScreenFragment provideSettingsScreenFragment(){
        return new SettingsScreenFragment();
    }

    @Provides
    MainScreenPresenter provideInputScreenPresenter(DataModel dataModel, List<UnsyncedExpense> unsyncedExpenseList){
        return new MainScreenPresenter(dataModel, unsyncedExpenseList);
    }

    @Provides
    SettingsPresenter provideSettingsScreenPresenter(){
        return new SettingsPresenter();
    }

    @Provides
    ExpenseAnalyzePresenter provideExpenseAnalyzePresenter(DataModel dataModel){
        return new ExpenseAnalyzePresenter(dataModel);
    }

    @Provides
    @Singleton
    DataModel provideDataModel(){
        return new DataModel(new ArrayList<Expense>(),
                new ArrayList<DailyExpenseTotal>(),
                new HashSet<String>(),
                new ArrayList<Integer>());
    }

    @Provides
    @Singleton
    List<UnsyncedExpense> provideUnsyncedExpenseList() {
        return new ArrayList<>();
    }
}
