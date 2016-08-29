package com.sum.library.view.widget.sticky;

/**
 * Created by Summer on 2016/8/29.
 */
public interface StickyScrollListener {

    /**
     * 当前的Head id
     *
     * @param headId
     */
    void currentHead(int headId);


    /**
     * 滚动到目标id
     *
     * @param headId
     */
    void scrollToHead(int headId);

}
