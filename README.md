## Android 多屏幕适配
[![Build Status](https://api.travis-ci.org/8enet/AndroidMultiScreenLayout.svg?branch=master)](https://api.travis-ci.org/8enet/AndroidMultiScreenLayout)  [![Software License](https://img.shields.io/badge/license-Apache%202.0-brightgreen.svg)](LICENSE)


### 使用方法:
首先布局尺寸单位必须是px，也就是设计师给的标注是多少就在布局里下多少的，然后在`onCreat()`的`setContentView()`之后调用`new LayoutSizeCompat.DesignSizeBuilder(1080,1920).builder().adjustSize(this);` 即可适配多种屏幕。


```java
LayoutSizeCompat.init(this);   
new LayoutSizeCompat.DesignSizeBuilder(1080,1920).builder().adjustSize(this);
```
其中`init()`方法可以只需要调用一次，简单用法，可以查看app module.


### 注意事项:
* 必要要在`setContentView()` 之类的方法后面调用,否则不起作用的
* 布局单位要用px,如果是xml布局要是px单位,java代码布局默认就是px单位的
* 如果是ListView之类的可回收子view的需要在`LayoutInflater.inflate()` 创建view之后调用`adjustSize()` 方法进行适配
* 不要多次调用`adjustSize()` 方法,否则会导致布局显示不正常,一般在`onCreat()` 之后调用一次即可
* TextView 可以使用`android:includeFontPadding="false"` 或者 `setIncludeFontPadding(false)` 取消文字的一点上下边距
* 调用`adjustSize()` 会遍历整个ViewTree,所有建议尽量不要嵌套过多的view,如果可以,最好使用代码生成布局而不是使用xml,使用java代码创建的布局要比xml快
* 因为Google推荐使用dp作为布局单位,但是用px也并不影响界面显示
