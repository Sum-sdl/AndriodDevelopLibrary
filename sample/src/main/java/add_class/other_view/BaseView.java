package add_class.other_view;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by sdl on 2017/10/12.
 */

public class BaseView extends View {
    public BaseView(Context context) {
        super(context);
    }

    public BaseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected int dp2px(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    //计算目标矩形居中的文字基线,注意Paint.Align的设置
    protected int getTargetBaseline(Rect rectL, Paint paint) {
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        paint.setTextAlign(Paint.Align.CENTER);
        return (rectL.bottom + rectL.top - fontMetrics.bottom - fontMetrics.top) / 2;
    }

}
