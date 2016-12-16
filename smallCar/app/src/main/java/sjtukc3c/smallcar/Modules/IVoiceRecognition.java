package sjtukc3c.smallcar.Modules;

/**
 * Created by Administrator on 2016/12/16.
 */
public interface IVoiceRecognition {

    //Use Presenter to get Voice data and get the corresponding string
    String voice2String(IVoiceDataPresenter voiceData);
}
