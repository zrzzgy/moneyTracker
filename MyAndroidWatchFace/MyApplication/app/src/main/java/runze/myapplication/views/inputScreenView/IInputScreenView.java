package runze.myapplication.views.inputScreenView;

import android.widget.ArrayAdapter;

import runze.myapplication.presenters.inputScreenPresenter.IInputScreenPresenter;
import runze.myapplication.views.IView;

public interface IInputScreenView extends IView<IInputScreenPresenter> {
    void updateSpinner(ArrayAdapter<String> adapter);

    void clearText();

    int getSpinnerIndex();

    void setSpinnerIndex(int index);
}
