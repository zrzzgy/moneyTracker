package runze.myapplication.dependencyinjection;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import runze.myapplication.HomeActivity;
import runze.myapplication.fragments.InputScreenFragment;
import runze.myapplication.fragments.SettingsScreenFragment;
import runze.myapplication.fragments.SpendingDetailFragment;
import runze.myapplication.fragments.StatsScreenFragment;
import runze.myapplication.presenters.InputScreenPresenter;
import runze.myapplication.presenters.SettingsScreenPresenter;
import runze.myapplication.presenters.SpendingDetailPresenter;
import runze.myapplication.presenters.StatsScreenPresenter;
import runze.myapplication.views.InputScreenView;
import runze.myapplication.views.SettingsScreenView;
import runze.myapplication.views.SpendingDetailView;
import runze.myapplication.views.StatsScreenView;

/**
 * App Module for dependency injection n
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
    SpendingDetailFragment provideSpendingDetailFragment(){
        return new SpendingDetailFragment();
    }

    @Provides
    @Singleton
    InputScreenView provideInputScreenView(){
        return new InputScreenView(homeActivity);
    }

    @Provides
    @Singleton
    SettingsScreenView provideSettingsScreenView(){
        return new SettingsScreenView(homeActivity);
    }

    @Provides
    @Singleton
    StatsScreenView provideStatsScreenView(){
        return new StatsScreenView(homeActivity);
    }

    @Provides
    @Singleton
    SpendingDetailView provideSpendingDetailView(){
        return new SpendingDetailView(homeActivity);
    }

    @Provides
    @Singleton
    InputScreenPresenter provideInputScreenPresenter(){
        return new InputScreenPresenter(homeActivity);
    }

    @Provides
    @Singleton
    StatsScreenPresenter provideStatsScreenPresenter(){
        return new StatsScreenPresenter(homeActivity);
    }

    @Provides
    @Singleton
    SettingsScreenPresenter provideSettingsScreenPresenter(){
        return new SettingsScreenPresenter(homeActivity);
    }

    @Provides
    @Singleton
    SpendingDetailPresenter provideSpendingDetailPresenter(){
        return new SpendingDetailPresenter(homeActivity);
    }
}
