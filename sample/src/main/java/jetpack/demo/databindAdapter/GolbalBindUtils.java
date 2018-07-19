package jetpack.demo.databindAdapter;

import android.databinding.BindingAdapter;
import android.widget.TextView;

/**
 * Created by sdl on 2018/7/19.
 */
public final class GolbalBindUtils {

    //自定义DataBinding处理操作
    @BindingAdapter("bind:addTab")
    public static void addTextViewTab(TextView view, CharSequence text) {
        view.setText(text + "[---]" + text);
    }

    @BindingAdapter("bind:studentName")
    public static void addStudentName(TextView view, CharSequence text) {
        view.setText(text + "[addStudentName]" + text);
    }
}
