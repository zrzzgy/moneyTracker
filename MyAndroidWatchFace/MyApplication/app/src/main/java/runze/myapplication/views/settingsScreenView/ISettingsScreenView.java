package runze.myapplication.views.settingsScreenView;

import java.util.Set;

import runze.myapplication.views.IView;


public interface ISettingsScreenView extends IView {
    void populateListView(Set<String> cate);
}
