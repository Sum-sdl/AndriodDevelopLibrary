package com.sum.library.domain;

/**
 * Created by Summer on 2016/9/9.
 * 界面代理类
 */
public interface ContextView {

    /**
     * @param type 定义的不同需要从界面获取的数据
     * @return 返回界面中有的数据
     */
    Object getValue(int type);


    /**
     * 将业务层根据不同type显示到界面或者业务逻辑
     *
     * @param type 定义不同的类型表示业务层获取的不同数据
     * @param obj  业务层生产的数据
     */
    void showValue(int type, Object obj);

}
