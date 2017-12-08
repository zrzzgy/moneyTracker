package runze.myapplication.views.statsScreenView;

import java.util.HashMap;
import java.util.List;

import runze.myapplication.views.IView;


public interface IStatsScreenView extends IView {
    void renderData(List<String> data);
}
