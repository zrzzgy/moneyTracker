package runze.myapplication.dependencyinjection;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import runze.myapplication.HomeActivity;
import runze.myapplication.fragments.InputScreenFragment;
import runze.myapplication.fragments.SettingsScreenFragment;
import runze.myapplication.fragments.StatsScreenFragment;
import runze.myapplication.presenters.inputScreenPresenter.IInputScreenPresenter;
import runze.myapplication.presenters.inputScreenPresenter.InputScreenPresenter;
import runze.myapplication.presenters.settingsScreenPresenter.ISettingsScreenPresenter;
import runze.myapplication.presenters.settingsScreenPresenter.SettingsScreenPresenter;
import runze.myapplication.presenters.statsScreenPresenter.IStatsScreenPresenter;
import runze.myapplication.presenters.statsScreenPresenter.StatsScreenPresenter;
import runze.myapplication.views.inputScreenView.IInputScreenView;
import runze.myapplication.views.inputScreenView.InputScreenView;
import runze.myapplication.views.settingsScreenView.ISettingsScreenView;
import runze.myapplication.views.settingsScreenView.SettingsScreenView;
import runze.myapplication.views.statsScreenView.IStatsScreenView;
import runze.myapplication.views.statsScreenView.StatsScreenView;

/**
 * App Module for dependency injection
 */
@Module
public class AppModule {
    private HomeActivity homeActivity;

    public AppModule(HomeActivity activity){
        homeActivity = activity;
    }

    @Provides
    @Singleton
    InputScreenFragment provideInputScreenFragment(){
        return new InputScreenFragment();
    }

    @Provides
    @Singleton
    StatsScreenFragment provideStatsScreenFragment(){
        return new StatsScreenFragment();
    }

    @Provides
    @Singleton
    SettingsScreenFragment provideSettingsScreenFragment(){
        return new SettingsScreenFragment();
    }

    @Provides
    @Singleton
    IInputScreenView provideInputScreenView(){
        return new InputScreenView(homeActivity);
    }

    @Provides
    @Singleton
    IInputScreenPresenter provideInputScreenPresenter(){
        return new InputScreenPresenter(homeActivity);
    }

    @Provides
    @Singleton
    IStatsScreenView provideStatsScreenView(){
        return new StatsScreenView(homeActivity);
    }

    @Provides
    @Singleton
    IStatsScreenPresenter provideStatsScreenPresenter(){
        return new StatsScreenPresenter(homeActivity);
    }
    @Provides
    @Singleton
    ISettingsScreenView provideSettingsScreenView(){
        return new SettingsScreenView(homeActivity);
    }

    @Provides
    @Singleton
    ISettingsScreenPresenter provideSettingsScreenPresenter(){
        return new SettingsScreenPresenter(homeActivity);
    }
}
