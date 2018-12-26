package runze.moneytracker.dependencyinjection;

import javax.inject.Singleton;

import dagger.Component;
import runze.moneytracker.HomeActivity;
import runze.moneytracker.fragments.ExpenseAnalysisFragment;
import runze.moneytracker.fragments.MainScreenFragment;
import runze.moneytracker.fragments.SettingsScreenFragment;
import runze.moneytracker.presenters.MainScreenPresenter;
import runze.moneytracker.presenters.SettingsPresenter;

/**
 *  App component for dependency injection
 */

@Component(modules = {AppModule.class})
@Singleton
public interface AppComponent {
    void inject(HomeActivity homeActivity);

    void inject(MainScreenFragment mainScreenFragment);
    void inject(ExpenseAnalysisFragment expenseAnalysisFragment);
    void inject(SettingsScreenFragment settingsScreenFragment);

    void inject(MainScreenPresenter mainScreenPresenter);
    void inject(SettingsPresenter settingsPresenter);
}
