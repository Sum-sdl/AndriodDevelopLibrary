package add_class.utils;

import android.text.TextPaint;
import android.text.style.ClickableSpan;

/**
 * Created by sdl on 2018/8/2.
 */
public abstract class NoLineClickSpan extends ClickableSpan {
    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(ds.linkColor);
        ds.setUnderlineText(false);
    }
}
