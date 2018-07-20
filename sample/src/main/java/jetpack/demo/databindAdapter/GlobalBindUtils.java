package jetpack.demo.databindAdapter;

import android.databinding.BindingAdapter;
import android.widget.TextView;

/**
 * Created by sdl on 2018/7/19.
 */
public final class GlobalBindUtils {

    //自定义DataBinding处理操作
    @BindingAdapter("bind:addTab")
    public static void addTextViewTab(TextView view, CharSequence text) {
        view.setText(text + "[---]" + text);
    }

    @BindingAdapter({"bind:studentName"})
    public static void addStudentName(TextView view, CharSequence text) {
        view.setText("[自定义的属性]->" + text);
    }
    @BindingAdapter({"bind:customText"})
    public static void addCustomText(TextView view, CharSequence text) {
        view.setText("[内容]->" + text);
    }
}
