# LiveSuperGift

### 发现

属性动画（Property Animation）对以下属性操作时，不会调用onDraw重绘,节省重绘的资源消耗。

* translationX、translationY   
* rotation、rotationX、rotationY  
* scaleX、scaleY
* x、y
* alpha

自己写的setter方法，那一定要记得调用invalidate()，否则视图不会更新的，动画也不会动的！

