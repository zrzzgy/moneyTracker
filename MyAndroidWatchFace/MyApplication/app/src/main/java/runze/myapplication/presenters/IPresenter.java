package runze.myapplication.presenters;

/**
 * Created by zhengr2 on 11/22/2017.
 */

import runze.myapplication.views.IView;

/**
 * Exposes necessary functionality for linking a presenter with a view. Any presenter that needs
 * to be linked to a view should implement this interface.
 * @param <V> The interface will control a view type of type {@link IView}
 */
public interface IPresenter<V extends IView> {

    /**
     * Attach the view to the presenter.
     */
    void attachView(V view);

    /**
     * Called if the view is destroyed. Should generally be called from Fragment.onDestroyView()
     */
    void detachView();
}
