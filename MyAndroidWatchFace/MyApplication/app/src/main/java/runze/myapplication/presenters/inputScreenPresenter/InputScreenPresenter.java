package runze.myapplication.presenters.inputScreenPresenter;

import runze.myapplication.HomeActivity;
import runze.myapplication.views.inputScreenView.IInputScreenView;

public class InputScreenPresenter implements IInputScreenPresenter {
    private HomeActivity mParentActivity;
    private IInputScreenView mView;

    public InputScreenPresenter(HomeActivity activity){
            mParentActivity = activity;
    }

    public void initView(){

    }

    public void attachView(IInputScreenView view){
        mView = view;
    }

    public void detachView(){
        mView = null;
    }

    public void saveData(){
        int total = 0;
        mParentActivity.mEditor.putInt("total", total);
    }
}
