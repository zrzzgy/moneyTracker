package runze.moneytracker.dependencyinjection;

import java.util.ArrayList;
import java.util.HashSet;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import runze.moneytracker.fragments.AnalyzeScreenFragment;
import runze.moneytracker.fragments.InputScreenFragment;
import runze.moneytracker.fragments.SettingsScreenFragment;
import runze.moneytracker.models.DailyExpenseTotal;
import runze.moneytracker.models.DataModel;
import runze.moneytracker.models.Expense;
import runze.moneytracker.presenters.ExpenseAnalyzePresenter;
import runze.moneytracker.presenters.InputScreenPresenter;
import runze.moneytracker.presenters.SettingsScreenPresenter;

import static org.mockito.Mockito.mock;

/**
 * App Module for dependency injection n
 */
@Module
public class TestAppModule {
    @Provides
    @Singleton
        // static
    InputScreenFragment provideInputScreenFragment(){
        return mock(InputScreenFragment.class);
    }

    @Provides
    @Singleton
    AnalyzeScreenFragment provideStatsScreenFragment(){
        return mock(AnalyzeScreenFragment.class);
    }

    @Provides
    @Singleton
    SettingsScreenFragment provideSettingsScreenFragment(){
        return mock(SettingsScreenFragment.class);
    }

    @Provides
    InputScreenPresenter provideInputScreenPresenter(DataModel dataModel){
        return mock(InputScreenPresenter.class);
    }

    @Provides
    SettingsScreenPresenter provideSettingsScreenPresenter(DataModel dataModel){
        return mock(SettingsScreenPresenter.class);
    }

    @Provides
    ExpenseAnalyzePresenter provideExpenseAnalyzePresenter(DataModel dataModel){
        return mock(ExpenseAnalyzePresenter.class);
    }

    @Provides
    @Singleton
    DataModel provideDataModel(){
        return mock(DataModel.class);
    }
}