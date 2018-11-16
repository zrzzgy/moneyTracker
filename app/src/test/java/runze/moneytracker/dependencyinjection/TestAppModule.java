package runze.moneytracker.dependencyinjection;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import runze.moneytracker.fragments.ExpenseAnalysisFragment;
import runze.moneytracker.fragments.InputScreenFragment;
import runze.moneytracker.fragments.SettingsScreenFragment;
import runze.moneytracker.models.DataModel;
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
    ExpenseAnalysisFragment provideStatsScreenFragment(){
        return mock(ExpenseAnalysisFragment.class);
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