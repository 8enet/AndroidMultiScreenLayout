## Android 多屏幕适配
[![Build Status](https://api.travis-ci.org/8enet/AndroidMultiScreenLayout.svg?branch=master)](https://api.travis-ci.org/8enet/AndroidMultiScreenLayout)  [![Software License](https://img.shields.io/badge/license-Apache%202.0-brightgreen.svg)](LICENSE)


###使用方法:
首先布局尺寸单位必须是px，也就是设计师给的标注是多少就在布局里下多少的，然后在`onCreat()`的`setContentView()`之后调用`new LayoutSizeCompat.DesignSizeBuilder(1080,1920).builder().adjustSize(this);` 即可适配多种屏幕。


```java
LayoutSizeCompat.init(this);   
new LayoutSizeCompat.DesignSizeBuilder(1080,1920).builder().adjustSize(this);
```
其中`init()`方法可以只需要调用一次。

