package com.example.recyclerviewtest;

import android.graphics.Canvas;
import android.os.Build;
import android.util.Log;
import android.view.VelocityTracker;
import android.view.View;

import androidx.core.view.ViewCompat;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;


import java.lang.reflect.Field;

/**
 * Author：sunxiang 11133446
 * Date：2021.08.16 18:09
 * Description：拟水动效动画
 */
public class SimulatedWaterAnim {
    private static final String TAG = "SimulatedWaterAnim";

    ItemTouchHelper mItemTouchHelper = null;

    /**
     * 反射调用：用于获取用户手指速度
     */
    private VelocityTracker velocityTracker;
    private Field field;

    /**
     * 当前手指移动速度
     */
    private float velocity = 0f;

    /**
     * 是否初始化了启动动画
     */
    private boolean initStartAnim = false;

    /**
     * 是否初始化了结束动画
     */
    private boolean initEndAnim = false;

    /**
     * 缩放系数，便于缩放值精确计算
     */
    private final float coefficient = 1000f;

    /**
     * 缩放最大值
     */
    private final float sMaxScale = 1.05f; //调参

    /**
     * 用于赋值的变量，需要乘以公共系数
     */
    private float scale = sMaxScale * coefficient;

    /**
     * 结束时的缩放大小，需要乘以公共系数
     */
    private final float scaleFinal = 1f * coefficient;

    /**
     * 缩放，弹性参数
     */
    //启动动画
    private final float scaleStiffnessStart = 900f;//调参
    private final float scaleDampingRatioStart = 0.70f;//调参

    //结束动画
    private final float scaleStiffnessEnd = 400f;//调参
    private final float scaleDampingRatioEnd = 0.46f;//调参

    /**
     * 最大高度，可以认为表示阴影面积
     */
    private final float maxZ = 60f;//调参位置

    /**
     * 结束高度，分为2阶段变化
     */
    private float zFinal = 0f;

    /**
     * 高度，弹性参数
     */
    //启动动画
    private final float zStiffnessStart = 900f;//调参
    private final float zDampingRatioStart = 0.60f;//调参

    //结束动画
    private final float zStiffnessEnd = 500f;//调参
    private final float zDampingRatioEnd = 0.75f;//调参

    /**
     * 翻转最大角度
     */
    private final float maxRotationX = 10f;//调参
    private final float rotationXFinal = 0f;//调参

    /**
     * 上一次翻转角度
     */
    private float lastRotationX = 0f;

    /**
     * 翻转角度，弹性参数
     */
    //启动动画
    private final float rotationXStiffnessStart = 800f;//调参
    private final float rotationXDampingRatioStart = 0.85f;//调参

    //结束动画
    private final float rotationXStiffnessEnd = 500f;//调参
    private final float rotationXDampingRatioEnd = 0.55f;//调参

    /**
     * 弹性动画对象
     */
    private SpringAnimation springRotationX;
    private SpringAnimation springScale;
    private SpringAnimation springZ;

    public SimulatedWaterAnim(ItemTouchHelper itemTouchHelper) {
        mItemTouchHelper = itemTouchHelper;
        try {
            field = mItemTouchHelper.getClass().getDeclaredField("mVelocityTracker");
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            Log.e(TAG, "SimulatedWaterAnim: " + e.getMessage());
        }
    }

    /**
     * 绘制拖动时被拖动的item自身动画。
     * 包含：触摸阶段，拖动阶段，松手阶段
     *
     * @param c                 RecyclerView正在绘制其子级的画布
     * @param recyclerView      ItemTouchHelper附加到的RecyclerView
     * @param view              用户正在交互的view
     * @param dX                用户动作引起的水平位移量
     * @param dY                用户动作引起的垂直位移量
     * @param actionState       交互类型是 {@link#ACTION_STATE_DRAG}还是{@link#ACTION_STATE_SWIPE}.
     * @param isCurrentlyActive 如果当前正在由用户控制此视图，则为True，将动画设置回其原始状态,则为false
     */
    public void onDraw(Canvas c, RecyclerView recyclerView, View view, float dX, float dY,
                       int actionState, boolean isCurrentlyActive) {
        if (isCurrentlyActive && !initStartAnim) {
            //触摸阶段
            Log.d(TAG, "onDraw: touch_down");
            initStartAnim = true;

            //先做弹起和放大动画，一口气完成，不随进度变化
            springScale = new SpringAnimation(view, mScale);
            SpringForce springScaleY = new SpringForce();
            springScaleY.setStiffness(scaleStiffnessStart);
            springScaleY.setDampingRatio(scaleDampingRatioStart);
            springScaleY.setFinalPosition(scale);
            springScale.setSpring(springScaleY);
            springScale.start();

            //修改层叠顺序避免被盖住
            springZ = new SpringAnimation(view, DynamicAnimation.Z);
            SpringForce springZ = new SpringForce();
            springZ.setStiffness(zStiffnessStart);
            springZ.setDampingRatio(zDampingRatioStart);
            springZ.setFinalPosition(maxZ);
            this.springZ.setSpring(springZ);
            this.springZ.start();

            //刚抬手阶段不翻转，先new对象出来
            springRotationX = new SpringAnimation(view, DynamicAnimation.ROTATION_X);
            SpringForce springRX = new SpringForce();
            springRX.setStiffness(rotationXStiffnessStart);
            springRX.setDampingRatio(rotationXDampingRatioStart);
            springRX.setFinalPosition((dY < 0) ? -lastRotationX : lastRotationX);
            springRotationX.setSpring(springRX);

        } else if (!isCurrentlyActive && !initEndAnim) {
            //松手阶段
            Log.d(TAG, "onDraw: touch_up");
            initEndAnim = true;
            //正式结束时，Z轴高度再降为初始高度大小
            springZ.animateToFinalPosition(zFinal);
        }

        if (isCurrentlyActive) {
            //移动阶段

            //尝试反射速度追踪器
            if (null == velocityTracker) {
                try {
                    velocityTracker = (VelocityTracker) field.get(mItemTouchHelper);
                } catch (IllegalAccessException e) {
                    Log.e(TAG, "SimulatedWaterAnim: " + e.getMessage());
                }
            }
            //监听每帧速度变化
            if (null != velocityTracker) {
                velocityTracker.computeCurrentVelocity(100, 1500f);
                velocity = velocityTracker.getYVelocity();
            }

            float x = velocity / 1500f;//归一化速度到0-1
            //根据速度计算偏转大小
            float velFinal = decelerate(x, maxRotationX);
            Log.d(TAG, "onDraw: velocity=" + velocity + " , velFinal=" + velFinal);
            if (velFinal != lastRotationX) {
                lastRotationX = velFinal;
                //如果速度降低，不同速度对应不同翻转大小，不同翻转大小对应不同弹性参数
                if (Math.abs(lastRotationX) < 8f
                    && springRotationX.getSpring().getDampingRatio() == rotationXDampingRatioStart) {
                    springRotationX.getSpring().setDampingRatio(rotationXDampingRatioEnd);
                    springRotationX.getSpring().setStiffness(rotationXStiffnessEnd);
                } else if (Math.abs(lastRotationX) > 8f
                    && springRotationX.getSpring().getDampingRatio() == rotationXDampingRatioEnd) {
                    springRotationX.getSpring().setDampingRatio(rotationXDampingRatioStart);
                    springRotationX.getSpring().setStiffness(rotationXStiffnessStart);
                }
                springRotationX.animateToFinalPosition(lastRotationX);
            }
        }

        //上下位移，每帧都做
        view.setTranslationX(dX);
        view.setTranslationY(dY);
    }

    /**
     * Author：sunxiang 11133446
     * Date：2021.08.16 18:09
     * Description: 衰减曲线
     *
     * @param input       Float 输入的瞬时速度
     * @param coefficient Float 最大翻转系数
     * @return Float 翻转角度
     */
    private float decelerate(float input, float coefficient) {
        float factor = 1.5f;
        float velFinal = (factor == 1.0f) ?
            (1.0f - (1.0f - input) * (1.0f - input))
            :
            (float) (1.0f - Math.pow((1.0 - input), (2.0 * factor)));

        return velFinal * coefficient;
    }

    /**
     * Author：sunxiang 11133446
     * Date：2021.08.16 18:09
     * Description: 从静止状态变为拖拽或者滑动的时候会回调该方法
     *
     * @param viewHolder  ViewHolder 主View
     * @param actionState Int 状态： 0=停止 1=横滑 2=拖动
     */
    public void onSelected(RecyclerView.ViewHolder viewHolder, int actionState) {
        Log.d(TAG, "onSelected: " + actionState);
        //when 代替 if else
        switch (actionState) {
            case 2: {
                //拖拽开始。早于onDraw的触摸阶段，此时item还没有抬起。
                if (null != viewHolder) {
                    View view = viewHolder.itemView;
                    zFinal = (view != null) ? ViewCompat.getElevation(view) : 0f;
                }
                initAnimation();
                break;
            }

            case 0: {
                if(springScale != null && springRotationX != null && springZ != null) {
                    //注意此时viewHolder = null
                    //拖拽结束。早于onDraw的松手阶段，此时item还没有回到准确位置。
                    springScale.getSpring().setDampingRatio(scaleDampingRatioEnd);
                    springScale.getSpring().setStiffness(scaleStiffnessEnd);
                    springScale.animateToFinalPosition(scaleFinal);

                    springRotationX.getSpring().setDampingRatio(rotationXDampingRatioEnd);
                    springRotationX.getSpring().setStiffness(rotationXStiffnessEnd);
                    springRotationX.animateToFinalPosition(rotationXFinal);

                    //结束时，Z轴高度降为初始高度+1大小
                    springZ.getSpring().setDampingRatio(zDampingRatioEnd);
                    springZ.getSpring().setStiffness(zStiffnessEnd);
                    springZ.animateToFinalPosition(zFinal + 1f);
                }
                break;
            }
            default: {
                break;
            }
        }
    }

    /**
     * Author：sunxiang 11133446
     * Date：2021.08.16 18:09
     * Description: 当用户操作完毕某个item松手.该阶段可能早于onDraw的松手阶段。但一定晚于onSelected
     * 这里不是收尾，不要再这里做动画。
     *
     * @param view 动画主体view
     */
    public void clearView(View view) {
        Log.d(TAG, "clearView ");
        //原生代码
        view.setTranslationY(0f);
        view.setTranslationX(0f);
        //原生代码
        if (Build.VERSION.SDK_INT >= 21) {
            Object tag = view.getTag(R.id.item_touch_helper_previous_elevation);
            if (tag instanceof Float) {
                ViewCompat.setElevation(view, ((Float) tag).floatValue());
            }
            view.setTag(R.id.item_touch_helper_previous_elevation, null);
        }
    }

    /**
     * Author：sunxiang 11133446
     * Date：2021.08.16 18:09
     * Description: 标志位置位，放在动画开始时
     */
    private void initAnimation() {
        Log.d(TAG, "initAnimation ");
        //恢复全部标志位
        initStartAnim = false;
        initEndAnim = false;
        velocity = 0f;
        velocityTracker = null;
    }

    /**
     * 自定义属性，缩放属性。同时操作scaleX和scaleY，并根据缩放系数来计算。
     */
    private FloatPropertyCompat<View> mScale = new FloatPropertyCompat<View>("scale") {
        private static final String TAG = "FloatPropertyCompat";

        @Override
        public void setValue(View view, float value) {
            Log.e(TAG, "setValue: value=" + (value / coefficient));
            view.setScaleY(value / coefficient);
            view.setScaleX(value / coefficient);
        }

        @Override
        public float getValue(View view) {
            return view.getScaleY() * coefficient;
        }
    };
}
