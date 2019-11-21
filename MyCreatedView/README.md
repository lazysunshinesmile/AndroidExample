# 自定义View 实现

1.  `onMeasure`方法奇怪的地方
   
   ```java
onMeasure(int widthMeasureSpec, int heightMeasureSpec)
   ```

   该方法会多次调用，不能将初始化参数放到该方法中。

   Acitvity的`onCreate` -> 自定义View的初始化，--> 自定义View的`onMeasure`,`onLayout`,`onDraw`。

   - 遇到的问题：在`onMeasure`中初始化一个`ListView<TextView>`，然后在其他方法中去获取，然后修改TextView的值。修改不生效

   - 代码样例：
   
     ```java
      @Override
         protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
             super.onMeasure(widthMeasureSpec, heightMeasureSpec);
             textViews = new ArrayList<>();
             for(int i = 0; i <4; i++) {
                 TextView textView = new TextView(getContext());
                 textView.setText("aaaa");
                 textViews.add(textView);
                 final  int finalI = i;
                 textView.setOnClickListener(new OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         Log.d("xiangsun", "onClick: i:" + finalI);
                         Log.d("xiangsun", "onClick: v:" + v);
                         if(finalI <3) {
                             textViews.get(finalI).setText("cccc");
                             Log.d("xiangsun", "onClick: v2" + textViews.get(finalI));
                         }
                     }
                 });
                 Log.d("xiangsun", "onMeasure: v:" + textView + ", size:" + textViews.size());
                 addView(textView);
             }
      }
     ```

   - 打印的日志
   
     ```log
     15:39:12.131  D/xiangsun: onMeasure: v:android.widget.TextView{a57e39a VFED..C.. ......ID 0,0-0,0}, size:1
     15:39:12.134  D/xiangsun: onMeasure: v:android.widget.TextView{1e104cb VFED..C.. ......ID 0,0-0,0}, size:2
     15:39:12.137  D/xiangsun: onMeasure: v:android.widget.TextView{c4714a8 VFED..C.. ......ID 0,0-0,0}, size:3
     15:39:12.139  D/xiangsun: onMeasure: v:android.widget.TextView{47f2bc1 VFED..C.. ......ID 0,0-0,0}, size:4
     15:39:12.163  D/xiangsun: onMeasure: v:android.widget.TextView{1c09f66 VFED..C.. ......ID 0,0-0,0}, size:1
     15:39:12.165  D/xiangsun: onMeasure: v:android.widget.TextView{e7922a7 VFED..C.. ......ID 0,0-0,0}, size:2
     15:39:12.168  D/xiangsun: onMeasure: v:android.widget.TextView{64cd354 VFED..C.. ......ID 0,0-0,0}, size:3
     15:39:12.170  D/xiangsun: onMeasure: v:android.widget.TextView{e11c2fd VFED..C.. ......ID 0,0-0,0}, size:4
     15:39:15.641  D/xiangsun: onClick: i:0
     15:39:15.642  D/xiangsun: onClick: v:android.widget.TextView{a57e39a VFED..C.. ...PH... 0,0-32,19}
     15:39:15.644  D/xiangsun: onClick: v2android.widget.TextView{1c09f66 VFED..C.. ......ID 128,0-128,0}
     15:39:15.669  D/xiangsun: onMeasure: v:android.widget.TextView{fbcf7d8 VFED..C.. ......ID 0,0-0,0}, size:1
     15:39:15.674  D/xiangsun: onMeasure: v:android.widget.TextView{98731 VFED..C.. ......ID 0,0-0,0}, size:2
     15:39:15.679  D/xiangsun: onMeasure: v:android.widget.TextView{c25ae16 VFED..C.. ......ID 0,0-0,0}, size:3
  15:39:15.683  D/xiangsun: onMeasure: v:android.widget.TextView{21a0397 VFED..C.. ......ID 0,0-0,0}, size:4
     ```
   
     从日志看出，`OnMeasure` 执行了两次，且`textViews`还是4个，说明对象不一样了，点击了第一个`TextView`，可以看出来，点击的是第一次初始化的`TextView`。但从`textViews`获取的`TextView`是第二次初始化的`TextView`，此时改变这个控件的值，也是不可见的。从日志看出来，当修改了`TextView`的值之后，又再次执行了`onMeasure`。
     
   - 解决办法：
   
     在初始化中实现初始化。在`Activity`执行`onCreate`的时候，自定义`View`的`onMeasure`方法并没有执行。所以在onCreate中执行`myView.setContent()`，是可以在`onMeasure`等方法中获取`content`，但无法在初始化中获取`content`。
   
2.  在`attrs.xml`文件中定义属性名称，和属性类型。然后在`layout...xml`中使用:

   - 原本android的属性直接写android:id
   - 自定义的view根据上面你自己定义的命名空间

   ![1574323910976](https://github.com/lazysunshinesmile/AndroidExample/blob/master/MyCreatedView/1574323910976.png)

3. 在java文件中使用：

   要注意你在attrs.xml中定义的名称：

   ![1574324109544](/home/Sun/.config/Typora/typora-user-images/1574324109544.png)

   然后在java文件中使用：

   ```
    		//BaseTabView就是你在上面定义的名称
    		TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.BaseTabView);
    		//这是用的android 属性，所以名称是BaseTabView_android_textSize
    		mTextSize = ta.getDimensionPixelSize(R.styleable.BaseTabView_android_textSize, 60);
   
           //获取选择后的字体大小，这个是自定义的属性，名称是BaseTabView_textSizeAfterClick
           mTextSizeAfterClick = ta.getDimensionPixelSize(R.styleable.BaseTabView_textSizeAfterClick, (int) mTextSize);
   ```

   
