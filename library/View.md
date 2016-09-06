
# Custom View

## 基础篇

#### 1.View中的坐标系统

**View的坐标系统是相对应父控件而言的**

```
view.getTop() //获取该View 左上角 距离父View 顶部 的距离
view.getLeft() //获取该View 左上角 距离父View 左侧 的距离
view.getBottom() //获取该View 右下角 距离父View 顶部 的距离
view.getRight() //获取该View 右下角 距离父View 左侧 的距离
```
**MotionEvent中的Get和GetRaw的区别**

```
event.getX() getY() //触摸点相对与其所在组件坐标系的坐标
event.getRawX() //触摸点相对于屏幕坐标
```

#### 2.角度和弧度的换算关系
* 一周对应角度360度 对应弧度2π弧度
* 180度 = π弧度


公式                  | 例子
----------------------|---------------------
**弧度 = 角度xπ/180** | 2π ＝ 360 x π / 180 
**角度 = 弧度x180/π** | 360 ＝ 2π x 180 / π

#### 3.View的绘制流程

1.  测量View大小(onMeasure)
    * 在onMeasure方法中去除宽高的相关数据 
    * MeasureSpec.getSize(wms) 取出宽度的确切数值
    * MeasureSpec.getModel(wms) 取出宽度的测量模式
    
测量模式 | 二进制值|描述
---|---|---
UNSPECIFIED| 00|默认值，父控件没有给子view任何限制，子View可以设置为任意大小。
EXACTLY| 01|父控件已经确切的指定了子View的大小（match_parent/具体值）。
AT_MOST| 10|子View具体大小没有尺寸限制，但是存在上限，上限一般为父View大小(warp_content)。

**注：对View的宽高进行修改，不要调用super.onMeasure(),最后调用setMeasuredDimension()这个函数**




2.  确定View的大小(onSizeChanged)

    **确定View大小的时候使用系统提供的onSizeChanged回调函数** 

3.  确定View在布局中的位置(onLayout)

    **用于确定子View的位置，在自定义ViewGroup中循环取出各个子View，然后计算子View的坐标值，然后调用子view.layout(l,t,r,b)设置位置;**

4.  绘制内容(onDraw)


#### 4.Canvas使用


| 操作类型       | 相关API                                    | 备注                                       |
| ---------- | ---------------------------------------- | ---------------------------------------- |
| 绘制颜色       | drawColor, drawRGB, drawARGB             | 使用单一颜色填充整个画布                             |
| 绘制基本形状     | drawPoint, drawPoints, drawLine, drawLines, drawRect, drawRoundRect, drawOval, drawCircle, drawArc | 依次为 点、线、矩形、圆角矩形、椭圆、圆、圆弧                  |
| 绘制图片       | drawBitmap, drawPicture                  | 绘制位图和图片                                  |
| 绘制文本       | drawText,    drawPosText, drawTextOnPath | 依次为 绘制文字、绘制文字时指定每个文字位置、根据路径绘制文字          |
| 绘制路径       | drawPath                                 | 绘制路径，绘制贝塞尔曲线时也需要用到该函数                    |
| 顶点操作       | drawVertices, drawBitmapMesh             | 通过对顶点操作可以使图像形变，drawVertices直接对画布作用、 drawBitmapMesh只对绘制的Bitmap作用 |
| 画布剪裁       | clipPath,    clipRect                    | 设置画布的显示区域                                |
| 画布快照       | save, restore, saveLayerXxx, restoreToCount, getSaveCount | 依次为 保存当前状态、 回滚到上一次保存的状态、 保存图层状态、 回滚到指定状态、 获取保存次数 |
| 画布变换       | translate, scale, rotate, skew           | 依次为 位移、缩放、 旋转、错切                         |
| Matrix(矩阵) | getMatrix, setMatrix, concat             | 实际画布的位移，缩放等操作的都是图像矩阵Matrix，只不过Matrix比较难以理解和使用，故封装了一些常用的方法。 |

* **画布位移（translate）**

    translate是画布坐标系的移动，可以为图形绘制选择一个合适的坐标系。 请注意，位移是基于当前位置移动，而不是每次基于屏幕左上角的(0,0)点移动
    
* **画布缩放（scale）**

    scale 是画布的大小缩放，注意的是，每次缩放基于上次缩放的大小

* **画布旋转（rotate）**

    rotate 是画布的旋转，注意的是，每次缩放基于上次角度进行选择

* **画布快照(save)和画布回滚(restore)**
    
    save保持当前画布状态，restore返回上个画布状态

* **画布错切(skew)**

* **Path 贝塞尔曲线**

  + lineTo(x,y) :从上一个点开始，到达指定点x,y(默认初始化为(0,0)点)
  + moveTo(x,y) :下一次操作的起始点
  + setLastPoint(x,y) :设置上一次最后的点
  + close() :最后一个点跟第一点不重合，链接两个，形成封闭图形


贝塞尔曲线原理
+ 数据点:确定曲线起始结束点
+ 控制点:确定曲线的弯曲程度

**一阶曲线:没有控制点（lineTo）**

**二阶曲线:对应方法(quadTo)**

**三阶曲线:对应方法(cubicTo)**