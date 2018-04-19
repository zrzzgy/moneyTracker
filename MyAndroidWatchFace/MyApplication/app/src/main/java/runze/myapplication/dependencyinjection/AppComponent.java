package runze.myapplication.dependencyinjection;

import javax.inject.Singleton;

import dagger.Component;
import runze.myapplication.HomeActivity;
import runze.myapplication.fragments.InputScreenFragment;
import runze.myapplication.fragments.SettingsScreenFragment;
import runze.myapplication.fragments.SpendingDetailFragment;
import runze.myapplication.fragments.StatsScreenFragment;
import runze.myapplication.presenters.InputScreenPresenter;
import runze.myapplication.presenters.SettingsScreenPresenter;
import runze.myapplication.presenters.StatsScreenPresenter;

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
