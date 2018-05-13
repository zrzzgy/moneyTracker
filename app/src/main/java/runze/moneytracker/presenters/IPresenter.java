package runze.moneytracker.presenters;

/**
 * Interface for presenter base
 */

import runze.moneytracker.views.IView;

/**
 * Exposes necessary functionality for linking a presenter with a view. Any presenter that needs
 * to be linked to a view should implement this interface.
 */
public interface IPresenter {

    /**
     * Attach the view to the presenter.
     */
    void attachView(IView view);

    /**
     * Called if the view is destroyed. Should generally be called from Fragment.onDestroyView()
     */
    void detachView();
}
