package runze.myapplication.dependencyinjection;

import javax.inject.Singleton;

import dagger.Component;
import runze.myapplication.HomeActivity;
import runze.myapplication.fragments.InputScreenFragment;
import runze.myapplication.fragments.SettingsScreenFragment;
import runze.myapplication.fragments.StatsScreenFragment;
import runze.myapplication.presenters.inputScreenPresenter.InputScreenPresenter;
import runze.myapplication.presenters.settingsScreenPresenter.SettingsScreenPresenter;
import runze.myapplication.presenters.statsScreenPresenter.StatsScreenPresenter;

/**
 *  App component for dependency injection
 */
@Component(modules = {AppModule.class})
@Singleton
public interface AppComponent {
    void inject(HomeActivity activity);
    void inject(InputScreenFragment fragment);
    void inject(StatsScreenFragment fragment);
    void inject(SettingsScreenFragment fragment);
    void inject(InputScreenPresenter presenter);
    void inject(StatsScreenPresenter presenter);
    void inject(SettingsScreenPresenter presenter);
}
