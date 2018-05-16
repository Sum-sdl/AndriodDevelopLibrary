
#### 个人开发库 [https://jitpack.io]

- 1 [快速开发的基础功能库](https://github.com/Sum-sdl/AndriodDevelopLibrary)
- 2 [RecyclerView通用适配器库](https://github.com/Sum-sdl/RvAdapter)

 在build.config文件添加远程库地址

	allprojects {
		repositories {
			maven { url 'https://jitpack.io' }
		}
	}

添加远程库依赖

	dependencies {
	        implementation 'com.github.Sum-sdl:AndriodDevelopLibrary:1.1.0'
	}


#### 1. 基础的功能

> **网络模块**
>
-  集成Retrofit网络请求框架（未添加RxJava）[BaseDynamicInterceptor](https://github.com/Sum-sdl/AndriodDevelopLibrary/blob/master/library/src/main/java/com/sum/library/net/token/BaseDynamicInterceptor.java)公共参数添加
-  集成[Fresco](https://www.fresco-cn.org/docs/scaling.html#_)图片框架

> **功能模块**
>
- [集成好的图片功能（预览，手势，裁剪）](https://github.com/Sum-sdl/AndriodDevelopLibrary/blob/master/library/src/main/java/com/sum/library/ui/image/AppImageUtils.java)
- [相册选择功能（类似微信支持多选）](https://github.com/Sum-sdl/AndriodDevelopLibrary/blob/master/library/src/main/java/com/sum/library/ui/image/AppImageUtils.java)

> **组件模块**
>
- 底部滑动出来的通用浮层的[AbstractBottomSheetFragment](https://github.com/Sum-sdl/AndriodDevelopLibrary/blob/master/library/src/main/java/com/sum/library/view/sheet/AbstractBottomSheetFragment.java)
- 底部滑动通用浮层案例[BottomSheetView](https://github.com/Sum-sdl/AndriodDevelopLibrary/blob/master/library/src/main/java/com/sum/library/view/sheet/BottomSheetView.java)
支持时间，日期选择，单选滚动功能

> **结构模块**
>
- [FragmentActivity+Fragment结构](https://github.com/Sum-sdl/AndriodDevelopLibrary/blob/master/library/src/main/java/com/sum/library/app/sum/LifeFragmentActivity.java)
- Fragment的管理类[FragmentCacheManager](https://github.com/Sum-sdl/AndriodDevelopLibrary/blob/master/library/src/main/java/com/sum/library/framework/FragmentCacheManager.java)
用来管理多个Fragment的切换操作（如App主页的底部按钮）
- RecyclerView的通用适配器[RvAdapter](https://github.com/Sum-sdl/RvAdapter)

> **部分功能图片**
>
![image](https://github.com/Sum-sdl/AndriodDevelopLibrary/blob/master/sample/demoUi/bottom_sheet.jpg)
![image](https://github.com/Sum-sdl/AndriodDevelopLibrary/blob/master/sample/demoUi/dic.jpg)
