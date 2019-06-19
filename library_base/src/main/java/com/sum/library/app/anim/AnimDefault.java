package com.sum.library.app.anim;


import com.sum.library.R;

/**
 * Created by Summer on 2016/8/9.
 * 常用的几个界面切换动画
 */
public class AnimDefault {

    private static int[] ANIM = new int[4];

    public static int[] GetAnim(AnimType animType) {

        switch (animType) {
            case DEFAULT:
            case ANIM_BOTTOM://底部入,底部出
                ANIM[0] = R.anim.activity_open_enter;
                ANIM[1] = R.anim.activity_open_exit;
                ANIM[2] = R.anim.activity_close_enter;
                ANIM[3] = R.anim.activity_close_exit;
                break;
            case ANIM_RIGHT://右进右出
                ANIM[0] = R.anim.right_open_enter;
                ANIM[1] = R.anim.right_open_exist;
                ANIM[2] = R.anim.right_close_enter;
                ANIM[3] = R.anim.right_close_exist;
                break;
            case ANIM_CENTER://中间缩放
                ANIM[0] = R.anim.center_open_enter;
                ANIM[1] = R.anim.center_open_exist;
                ANIM[2] = R.anim.center_close_enter;
                ANIM[3] = R.anim.center_close_exit;
                break;
            default:
                break;
        }
        return ANIM;
    }
}
