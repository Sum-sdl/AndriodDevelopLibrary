# View 实例化流程以及相关属性说明


### 1.LayoutInflate

**所有View的实例化都是在LayoutInflater类中完成创建和添加的ParentView中**

inflate(...)如果参数中带**有父View并且没有attach到父View**，在子View创建的时候会创建LayoutParams设置给该子View,
如果没有,则在xml设置的rootView(如:LinearLayout作为xml的跟布局)的布局属性attr无效,
在addView的时候就会创建一个默认的LayoutParmas，rootView的子View有效


### 2.LayoutParams

LayoutParams规定了一个View能够在布局中绘制的区域的大小，以及和parent的margin参数，超出区域大小的View不做绘制

**父View在addView的时候必须给添加的子View必须有LayoutParmas或者addView的时候设置一个LayoutParmas** (见源码)


### 3.相关说明

View的大小即getWidth getMeasureWidth 跟LayoutParams没有关系任何关系，默认情况下两者的值是一致的。

View的大小有 **setMeasuredDimension(w,h)** 方法决定
LayoutParams是xml解析的时候由xml中的属性决定,LayoutParams只是负责绘制的区域