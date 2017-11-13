# ProgressDialogDemo
测试旋转屏幕后handler对变量的影响

---
## 2017.11.13 setOnCheckedChangeListener test
需求：点击按钮显示两个RadioButton，并根据情况check其中一个,同时onCheckedChanged()执行后续逻辑。
问题：第一次点击按钮显示RadioButton 后不会触发`onCheckedChanged()`, 之后隐藏RadioGroup，第二次点击后会触发，即使没有切换到另一个RadioButton。
跟代码执行顺序有关，看代码。

## 2017.07.24 fragment test
测试
```java
    // 显示返回按钮
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setHomeButtonEnabled(true);
...
    // 返回按钮可返回，并显示menu
    setHasOptionsMenu(true); // 如注释这行则返回按钮中看不中用
```
对actionbar上的回按钮和菜单的影响

## 2017.03.24 quick setting tile
androi N 支持第三方tile添加到快速设置面板。
写了一个QStile添加到快速设置面板，下拉点击快速设置图标，调用之前的placeCall可以实现快捷拨号。

## 2016.10.25 test placeCall api
使用android M 新增api TelecomManager.placeCall(), 实现一行代码直接拨打电话。

## 2016.09.21 java reflect
使用java反射调用android 系统隐藏api getNetworkOperatorName获得运营商名称。

## 2016.08.23 notification
现在不只是Progress的测试了，平时写的测试小功能的demo也会集成进来一部分。
增加Notification更新Extra的测试代码。
