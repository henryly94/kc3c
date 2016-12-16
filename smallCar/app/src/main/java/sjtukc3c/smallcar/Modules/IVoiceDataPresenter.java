package sjtukc3c.smallcar.Modules;

/**
 * Created by Administrator on 2016/12/16.
 */
public interface IVoiceDataPresenter {

    //Push button -> begin
    //Loose button -> end
    void beginCatch();

    void endCatch();

    IVoiceDataPresenter getInstance();
}
