package add_class.utils;

import android.text.TextPaint;
import android.text.style.ClickableSpan;

/**
 * Created by sdl on 2018/8/2.
 */
public abstract class NoLineClickSpan extends ClickableSpan {


    @Override
    public void updateDrawState(TextPaint ds) {
        //TODO 这里修改字体颜色
        ds.setColor(ds.linkColor);
        //没有下划线
        ds.setUnderlineText(false);
    }
}
