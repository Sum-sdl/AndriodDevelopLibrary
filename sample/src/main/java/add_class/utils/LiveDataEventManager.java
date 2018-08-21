package add_class.utils;

import com.sum.library.utils.LiveDataEventBus;

/**
 * Created by sdl on 2018/8/21.
 */
public class LiveDataEventManager {

    public static final String event_login_success = "user_login_success_event";

    //常用状态事件
    public static void sendEvent(String event_key, String event_value) {
        LiveDataEventBus.with(event_key).setValue(event_value);
    }

    public static void sendObject(String event_key, Object o) {
        LiveDataEventBus.with(event_key, Object.class).setValue(o);
    }

    public static void sendLoginSuccess() {
        sendEvent(event_login_success, "1");
    }
}
