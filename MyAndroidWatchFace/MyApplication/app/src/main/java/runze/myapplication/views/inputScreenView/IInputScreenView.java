package runze.myapplication.views.inputScreenView;

import runze.myapplication.presenters.inputScreenPresenter.IInputScreenPresenter;
import runze.myapplication.views.IView;

public interface IInputScreenView extends IView<IInputScreenPresenter> {
    void clearText();
}
