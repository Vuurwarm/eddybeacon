package tech.livx.ibeacon.interfaces;

/**
 * Created by damionunderworld on 2015/11/23.
 */
public interface ApiCallBackInterface {
    void OnSuccess(String value, int code);
    void OnFailed(String value, int code);
}
