package runze.moneytracker.dependencyinjection;

import java.util.ArrayList;
import java.util.HashSet;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import runze.moneytracker.fragments.InputScreenFragment;
import runze.moneytracker.fragments.SettingsScreenFragment;
import runze.moneytracker.fragments.AnalyzeScreenFragment;
import runze.moneytracker.models.DailyExpenseTotal;
import runze.moneytracker.models.DataModel;
import runze.moneytracker.models.Expense;
import runze.moneytracker.presenters.ExpenseAnalyzePresenter;
import runze.moneytracker.presenters.InputScreenPresenter;
import runze.moneytracker.presenters.SettingsScreenPresenter;

/**
 * App Module for dependency injection n
 */
@Module
public class AppModule {
    @Provides
    @Singleton   // static
    InputScreenFragment provideInputScreenFragment(){
        return new InputScreenFragment();
    }

    @Provides
    @Singleton
    AnalyzeScreenFragment provideStatsScreenFragment(){
        return new AnalyzeScreenFragment();
    }

    @Provides
    @Singleton
    SettingsScreenFragment provideSettingsScreenFragment(){
        return new SettingsScreenFragment();
    }

    @Provides
    InputScreenPresenter provideInputScreenPresenter(DataModel dataModel){
        return new InputScreenPresenter(dataModel);
    }

    @Provides
    SettingsScreenPresenter provideSettingsScreenPresenter(DataModel dataModel){
        return new SettingsScreenPresenter(dataModel);
    }

    @Provides
    ExpenseAnalyzePresenter provideExpenseAnalyzePresenter(DataModel dataModel){
        return new ExpenseAnalyzePresenter(dataModel);
    }

    @Provides
    @Singleton
    DataModel provideDataModel(){
        return new DataModel(new ArrayList<Expense>(), new ArrayList<DailyExpenseTotal>(),
                new HashSet<String>(),
                new ArrayList<Integer>());
    }
}
