package runze.myapplication.presenters.inputScreenPresenter;

import android.widget.Toast;

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

    public void saveData(double amount){
        if (amount > 0) {
            mParentActivity.mEditor.putLong("amount", (long) amount);
        }else {
            Toast.makeText(mParentActivity.getApplicationContext(), "Amount must be larger than zero", Toast.LENGTH_SHORT).show();
        }
    }
}
