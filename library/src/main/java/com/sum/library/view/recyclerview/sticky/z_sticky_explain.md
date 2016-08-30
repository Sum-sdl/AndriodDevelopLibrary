# 流程

**通过recyclerView的addItemDecoration()为每个Item添加修饰**

* RecyclerView.ItemDecoration 装饰接口说明
> 每一项Item显示调用的方法过程： getItemOffsets -> onDraw() -> onDrawOver()
> getItemOffsets() Rect定义了每一项的偏移量，使得Canvas绘制的View占据其偏移的位置
> onDrawOver() 实现为每一项添加悬浮在View之上的装饰
> onDraw() 实现为每一项添加在View下方的装饰

* 实现过程(RecyclerView每一项出现都执行以下流程)
    1. getItemOffsets()判断是否存在HeadView,存在的话为HeadView占据相应的大小
    2. onDrawOver() 遍历RecyclerView中所有的子View,并判断对应的View是否需要HeadView
         * hasStickyHeader() 判断对应位置的View是否需要HeadView
         * hasNewHeader() 判断对应位置是否需要一个新的HeadView
         * 存在HeadView的时候处理:
            