package runze.moneytracker.views;

import runze.moneytracker.presenters.IPresenter;

/**
 * Exposes necessary functionality for linking a view with a presenter. Any view that needs
 * to interact with a presenter should implement this interface.
 * @param <P> The interface will control a presenter type of type {@link IPresenter}
 */
public interface IView<P extends IPresenter> {

    /**
     * Attaches the presenter to the view
     *
     * @param presenter The presenter to use
     */
    void attachPresenter(P presenter);

    /**
     * Detaches the presenter from the view.
     */
    void detachPresenter();
}

