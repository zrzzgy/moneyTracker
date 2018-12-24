package runze.moneytracker.dependencyinjection;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import runze.moneytracker.fragments.ExpenseAnalysisFragment;
import runze.moneytracker.fragments.MainScreenFragment;
import runze.moneytracker.fragments.SettingsScreenFragment;
import runze.moneytracker.models.DataModel;
import runze.moneytracker.presenters.ExpenseAnalyzePresenter;
import runze.moneytracker.presenters.MainScreenPresenter;
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
    MainScreenFragment provideInputScreenFragment(){
        return mock(MainScreenFragment.class);
    }

    @Provides
    @Singleton
    ExpenseAnalysisFragment provideStatsScreenFragment(){
        return mock(ExpenseAnalysisFragment.class);
    }

    @Provides
    @Singleton
    SettingsScreenFragment provideSettingsScreenFragment(){
        return mock(SettingsScreenFragment.class);
    }

    @Provides
    MainScreenPresenter provideInputScreenPresenter(DataModel dataModel){
        return mock(MainScreenPresenter.class);
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