package runze.myapplication.presenters.inputScreenPresenter;

import runze.myapplication.presenters.IPresenter;
import runze.myapplication.views.InputScreenView;

public interface IInputScreenPresenter extends IPresenter<InputScreenView> {
    /**
     * Presenter method for populating home screen
     */
    void updateView(int index);

    void saveData(String category, double amount);
}
