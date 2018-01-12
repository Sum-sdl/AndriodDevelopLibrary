package com.sum.library.app.sum;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.sum.library.app.BaseActivity;
import com.sum.library.app.anim.AnimDefault;
import com.sum.library.app.anim.AnimType;
import com.sum.library.utils.Logger;

/**
 * Created by Summer on 2016/9/10.
 */
public abstract class LifeFragmentActivity extends BaseActivity {

    public static boolean DEBUG = false;

    private LifeFragment mCurrentFragment;

    protected abstract int getFragmentContainerId();

    //表示FragmentManger栈最后一个做为根布局
    protected boolean needRoot() {
        return true;
    }

    private boolean isFirstAdd = true;

    static class FragmentParam {
        public AnimType anim;
        public Class<?> cls;
        public Object data;
    }

    /**
     * 添加Fragment到管理栈里，同一个实力对象只会创建一次
     * 功能实现原理FragmentTabhost相同,注意hide和detach区别
     *
     * @param cls  显示的Fragment
     * @param data 给Fragment传递数据
     */

    public void pushFragmentToBackStack(Class<?> cls, Object data, AnimType type) {
        FragmentParam param = new FragmentParam();
        param.cls = cls;
        param.anim = type;
        param.data = data;
        goToThisFragment(param);
    }

    public void pushFragmentToBackStack(Class<?> cls, Object data) {
        pushFragmentToBackStack(cls, data, AnimType.DEFAULT);
    }

    protected String getFragmentTag(FragmentParam param) {
        StringBuilder sb = new StringBuilder(param.cls.toString());
        return sb.toString();
    }

    private void goToThisFragment(FragmentParam param) {
        int containerId = getFragmentContainerId();
        Class<?> cls = param.cls;
        if (cls == null) {
            return;
        }
        try {
            String fragmentTag = getFragmentTag(param);
            FragmentManager fm = getSupportFragmentManager();
            if (DEBUG) {
                Logger.d("before operate, stack entry count: " + fm.getBackStackEntryCount());
            }
            LifeFragment fragment = (LifeFragment) fm.findFragmentByTag(fragmentTag);
            if (fragment == null) {
                fragment = (LifeFragment) cls.newInstance();
                if (DEBUG) {
                    Logger.d("newInstance " + fragmentTag);
                }
            }
            if (mCurrentFragment != null && mCurrentFragment != fragment) {
                mCurrentFragment.onLeave();
            }

            fragment.onEnter(param.data);

            FragmentTransaction ft = fm.beginTransaction();
            if (!needRoot() || !isFirstAdd) {
                //新加入的Fragment动画，栈中fragment退出动画，栈中fragment进去动画，栈定fragment推出动画
                if (param.anim != AnimType.DEFAULT) {
                    int[] anim = AnimDefault.GetAnim(param.anim);
                    ft.setCustomAnimations(anim[0], anim[1], anim[2], anim[3]);
                }
            }

            ft.replace(containerId, fragment, fragmentTag);

            mCurrentFragment = fragment;

            ft.addToBackStack(fragmentTag);

            ft.commit();

            isFirstAdd = false;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void goToFragment(Class<?> cls, Object data) {
        if (cls == null) {
            return;
        }
        LifeFragment fragment = (LifeFragment) getSupportFragmentManager().findFragmentByTag(cls.toString());
        if (fragment != null) {
            mCurrentFragment = fragment;
            fragment.onBackWithData(data);
        }
        getSupportFragmentManager().popBackStackImmediate(cls.toString(), 0);
    }

    public void popTopFragment(Object data) {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() == 1 && needRoot()) {
            this.finish();
        } else {
            fm.popBackStackImmediate();
            if (tryToUpdateCurrentAfterPop() && mCurrentFragment != null) {
                mCurrentFragment.onBackWithData(data);
            }
        }
    }

    public void popToRoot(Object data) {
        FragmentManager fm = getSupportFragmentManager();
        while (fm.getBackStackEntryCount() > 1 || !needRoot()) {
            fm.popBackStackImmediate();
        }
        popTopFragment(data);
    }

    protected void doReturnBack() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 1 && needRoot()) {
            finish();
        } else if (count == 0) {
            this.finish();
        } else {
            getSupportFragmentManager().popBackStackImmediate();
            if (tryToUpdateCurrentAfterPop() && mCurrentFragment != null) {
                mCurrentFragment.onBack();
            }
        }
    }

    private boolean tryToUpdateCurrentAfterPop() {
        FragmentManager fm = getSupportFragmentManager();
        int cnt = fm.getBackStackEntryCount();
        if (cnt > 0) {
            String name = fm.getBackStackEntryAt(cnt - 1).getName();
            Fragment fragment = fm.findFragmentByTag(name);
            if (fragment != null && fragment instanceof LifeFragment) {
                mCurrentFragment = (LifeFragment) fragment;
            }
            return true;
        }
        return false;
    }

    /**
     * process the return back logic
     * return true if back pressed event has been processed and should stay in current view
     *
     * @return
     */
    protected boolean processBackPressed() {
        return false;
    }


    @Override
    public void onBackPressed() {
        // process back for fragment
        if (processBackPressed()) {
            return;
        }

        // process back for fragment
        boolean enableBackPressed = true;
        if (mCurrentFragment != null && mCurrentFragment.isResumed()) {
            enableBackPressed = !mCurrentFragment.processBackPressed();
        }
        if (enableBackPressed) {
            doReturnBack();
        }
    }

    public void hideKeyboardForCurrentFocus() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    public void showKeyboardAtView(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public void forceShowKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

    }

    protected void exitFullScreen() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
    }
}
