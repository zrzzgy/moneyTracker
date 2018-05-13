package runze.moneytracker.dependencyinjection;

import javax.inject.Singleton;

import dagger.Component;
import runze.moneytracker.HomeActivity;
import runze.moneytracker.fragments.InputScreenFragment;
import runze.moneytracker.fragments.SettingsScreenFragment;
import runze.moneytracker.fragments.SpendingDetailFragment;
import runze.moneytracker.fragments.StatsScreenFragment;
import runze.moneytracker.presenters.InputScreenPresenter;
import runze.moneytracker.presenters.SettingsScreenPresenter;
import runze.moneytracker.presenters.StatsScreenPresenter;

/**
 *  App component for dependency injection
 */
@Component(modules = {AppModule.class})
@Singleton
public interface AppComponent {
    void inject(HomeActivity homeActivity);

    void inject(InputScreenFragment inputScreenFragment);
    void inject(SpendingDetailFragment spendingDetailFragment);
    void inject(StatsScreenFragment statsScreenFragment);
    void inject(SettingsScreenFragment settingsScreenFragment);

    void inject(InputScreenPresenter inputScreenPresenter);
    void inject(StatsScreenPresenter statsScreenPresenter);
    void inject(SettingsScreenPresenter settingsScreenPresenter);
}
