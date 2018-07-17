
#### 个人开发库 [https://jitpack.io]

- 1 [快速开发的基础功能库](https://github.com/Sum-sdl/AndriodDevelopLibrary) --> [Demo Apk 点击下载](https://raw.githubusercontent.com/Sum-sdl/AndriodDevelopLibrary/master/sample/other/sample-debug.apk)
- 2 [RecyclerView通用适配器库][1]

[1]: https://github.com/Sum-sdl/RvAdapter  "RecyclerView通用适配器库" 


 添加远程库地址和远程库依赖
 
	allprojects {
		repositories {
			maven { url 'https://jitpack.io' }
		}
	}
	
	dependencies {
       implementation 'com.github.Sum-sdl:AndriodDevelopLibrary:2.0.1'
    }


#### 1. 基础的功能

> **结构模块**
>
- [Google结构框架封装->ViewModel+LiveData(App->ViewModel->DataRepository)](https://github.com/Sum-sdl/AndriodDevelopLibrary/tree/master/sample/src/main/java/jetpack/demo/framework)
- Fragment的管理类[FragmentCacheManager](https://github.com/Sum-sdl/AndriodDevelopLibrary/blob/master/library/src/main/java/com/sum/library/framework/FragmentCacheManager.java)
用来管理多个Fragment的切换操作（如App主页的底部按钮）
- RecyclerView的通用适配器[RvAdapter](https://github.com/Sum-sdl/RvAdapter)

> **UI组件模块**
>
- 底部滑动出来的通用浮层的[AbstractBottomSheetFragment](https://github.com/Sum-sdl/AndriodDevelopLibrary/blob/master/library/src/main/java/com/sum/library/view/sheet/AbstractBottomSheetFragment.java)
- 底部滑动通用浮层案例[BottomSheetView](https://github.com/Sum-sdl/AndriodDevelopLibrary/blob/master/library/src/main/java/com/sum/library/view/sheet/BottomSheetView.java)
支持时间，日期选择，单选滚动功能
- 统一的空UI展示[PubEmptyView](https://github.com/Sum-sdl/AndriodDevelopLibrary/blob/master/library/src/main/java/com/sum/library/view/widget/PubEmptyView.java),类似ViewStub的功能实现,调用setVisibility()的时候才会初始化View的内容
- 统一的标题展示[PubTitleView](https://github.com/Sum-sdl/AndriodDevelopLibrary/blob/master/library/src/main/java/com/sum/library/view/widget/PubTitleView.kt)

> **第三方集成模块**
>
-  集成Retrofit网络请求框架（未添加RxJava）[BaseDynamicInterceptor](https://github.com/Sum-sdl/AndriodDevelopLibrary/blob/master/library/src/main/java/com/sum/library/net/token/BaseDynamicInterceptor.java)公共参数添加
-  集成[Fresco](https://www.fresco-cn.org/docs/scaling.html#_)图片框架

> **Activity功能模块**
>
- [图片预览功能（支持手势）](https://github.com/Sum-sdl/AndriodDevelopLibrary/blob/master/library/src/main/java/com/sum/library/ui/image/AppImageUtils.java)
- [自定义相册选择功能（支持预览，多选，拍照）](https://github.com/Sum-sdl/AndriodDevelopLibrary/blob/master/library/src/main/java/com/sum/library/ui/image/AppImageUtils.java)
- [图片裁剪功能](https://github.com/Sum-sdl/AndriodDevelopLibrary/blob/master/library/src/main/java/com/sum/library/ui/image/AppImageUtils.java)

> **部分功能图片**
>
<div align="center">
<img src="https://github.com/Sum-sdl/AndriodDevelopLibrary/blob/master/sample/demoUi/img.jpg" height="330" width="190" >
<img src="https://github.com/Sum-sdl/AndriodDevelopLibrary/blob/master/sample/demoUi/bt_1.jpg" height="330" width="190" >
<img src="https://github.com/Sum-sdl/AndriodDevelopLibrary/blob/master/sample/demoUi/bt_2.jpg" height="330" width="190" >
<img src="https://github.com/Sum-sdl/AndriodDevelopLibrary/blob/master/sample/demoUi/bt_3.jpg" height="330" width="190" >
</div>
<div align="center">
<img src="https://github.com/Sum-sdl/AndriodDevelopLibrary/blob/master/sample/demoUi/img1.jpg" height="330" width="190" >
<img src="https://github.com/Sum-sdl/AndriodDevelopLibrary/blob/master/sample/demoUi/img3.jpg" height="330" width="190" >
<img src="https://github.com/Sum-sdl/AndriodDevelopLibrary/blob/master/sample/demoUi/img4.jpg" height="330" width="190" >
<img src="https://github.com/Sum-sdl/AndriodDevelopLibrary/blob/master/sample/demoUi/img5.jpg" height="330" width="190" >
</div>

> **JetPack框架结构**
>
<img src="https://github.com/Sum-sdl/AndriodDevelopLibrary/blob/master/sample/demoUi/architecture.png">


> **Demo测试Apk下载二维码**
>
<img src="https://github.com/Sum-sdl/AndriodDevelopLibrary/blob/master/sample/demoUi/download.png">