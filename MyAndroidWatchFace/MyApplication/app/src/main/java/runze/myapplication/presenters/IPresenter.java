package runze.myapplication.presenters;

/**
 * Interface for presenter base
 */

import android.view.View;

import runze.myapplication.views.IView;

/**
 * Exposes necessary functionality for linking a presenter with a view. Any presenter that needs
 * to be linked to a view should implement this interface.
 * @param <V> The interface will control a view type of type {@link IView}
 */
public interface IPresenter<V extends View> {

    /**
     * Attach the view to the presenter.
     */
    void attachView(V view);

    /**
     * Called if the view is destroyed. Should generally be called from Fragment.onDestroyView()
     */
    void detachView();
}
