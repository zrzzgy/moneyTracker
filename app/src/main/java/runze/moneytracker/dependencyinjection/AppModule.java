package runze.moneytracker.dependencyinjection;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import runze.moneytracker.HomeActivity;
import runze.moneytracker.fragments.InputScreenFragment;
import runze.moneytracker.fragments.SettingsScreenFragment;
import runze.moneytracker.fragments.StatsScreenBaseFragment;
import runze.moneytracker.presenters.InputScreenPresenter;
import runze.moneytracker.presenters.SettingsScreenPresenter;
import runze.moneytracker.presenters.SpendingDetailPresenter;
import runze.moneytracker.presenters.StatsScreenBasePresenter;
import runze.moneytracker.views.InputScreenView;
import runze.moneytracker.views.SettingsScreenView;
import runze.moneytracker.views.SpendingDetailView;

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
    @Singleton   // static
    InputScreenFragment provideInputScreenFragment(){
        return new InputScreenFragment();
    }

    @Provides
    @Singleton
    StatsScreenBaseFragment provideStatsScreenFragment(){
        return new StatsScreenBaseFragment();
    }

    @Provides
    @Singleton
    SettingsScreenFragment provideSettingsScreenFragment(){
        return new SettingsScreenFragment();
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
    StatsScreenBasePresenter provideStatsScreenPresenter(){
        return new StatsScreenBasePresenter(homeActivity);
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
