# AndriodDevelopLibrary

**个人快速开发框架库**

#### AndroidStudio添加使用方法

在build.config文件添加远程库地址

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

添加远程库依赖

	dependencies {
	        implementation 'com.github.Sum-sdl:AndriodDevelopLibrary:1.0.8'
	}

