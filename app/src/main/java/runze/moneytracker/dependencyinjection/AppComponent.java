package runze.moneytracker.dependencyinjection;

import javax.inject.Singleton;

import dagger.Component;
import runze.moneytracker.HomeActivity;
import runze.moneytracker.fragments.InputScreenFragment;
import runze.moneytracker.fragments.SettingsScreenFragment;
import runze.moneytracker.fragments.StatsScreenBaseFragment;
import runze.moneytracker.presenters.InputScreenPresenter;
import runze.moneytracker.presenters.SettingsScreenPresenter;
import runze.moneytracker.presenters.StatsScreenBasePresenter;

/**
 *  App component for dependency injection
 */

@Component(modules = {AppModule.class})
@Singleton
public interface AppComponent {
    void inject(HomeActivity homeActivity);

    void inject(InputScreenFragment inputScreenFragment);
    void inject(StatsScreenBaseFragment statsScreenBaseFragment);
    void inject(SettingsScreenFragment settingsScreenFragment);

    void inject(InputScreenPresenter inputScreenPresenter);
    void inject(StatsScreenBasePresenter statsScreenBasePresenter);
    void inject(SettingsScreenPresenter settingsScreenPresenter);
}
