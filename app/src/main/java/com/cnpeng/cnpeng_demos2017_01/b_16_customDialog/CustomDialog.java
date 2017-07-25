package com.cnpeng.cnpeng_demos2017_01.b_16_customDialog;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;

import com.cnpeng.cnpeng_demos2017_01.R;
import com.cnpeng.cnpeng_demos2017_01.databinding.CustomDialogAntBinding;

/**
 * 作者：CnPeng
 * <p>
 * 时间：2017/7/25:上午11:58
 * <p>
 * 说明：dialog的通用类，展示和关闭dialog只通过该类就可以实现，避免每次都写一堆控制大小，宽高、背景的代码
 * <p>
 * 1、标题、确认按钮、取消按钮默认都不展示，只有调用相应的设置方法之后才会展示
 * 2、只有传入的点击监听不为null时，点击按钮的同时会关闭dialog
 * <p>
 * 当前已经支持：更改标题，更改消息，更改按钮描述及其点击事件，更改主体内容的背景，更改外层灰色背景的灰度比率，更改尺寸
 * <p>
 * 还需增加：更改字号，更改标题背景，传入颜色值和角度值自动生成背景，增加对LV条目的支持，增加最大高度的控制
 */

class CustomDialog {

    private final Context                context;
    private final AlertDialog            dialog;             //dialog对象
    private       View                   dialogView;         //dialogView 
    private       CustomDialogAntBinding dialogBinding;

    public CustomDialog(Context context) {
        this.context = context;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        dialog = builder.create();

        initDialogView();
    }

    private void initDialogView() {
        LayoutInflater inflater = LayoutInflater.from(context);
        dialogBinding = DataBindingUtil.inflate(inflater, R.layout.custom_dialog_ant, null, false);
        dialogView = dialogBinding.getRoot();
        dialog.setView(dialogView);     //设置view
        setLayout(0, 0);          //设置宽高
        setBackGroundDrawableResource(0);
    }

    /**
     * 展示dialog
     */
    public void showDialog() {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    /**
     * 关闭dialog
     */
    public void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    /**
     * 返回dialog的view对象
     */
    public View getDialogView() {
        return dialogView;
    }

    /**
     * 返回dialog对象
     */
    public Dialog getDialogObj() {
        return dialog;
    }

    /**
     * 设置dialog的宽高信息
     */
    public void setLayout(final int width, final int height) {
        final Window window = dialog.getWindow();
        if (null != window) {
            Display display = window.getWindowManager().getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            final int windowWidth = metrics.widthPixels;

            dialogView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop,
                                           int oldRight, int oldBottom) {
                    int finalWidth = width == 0 ? (int) (windowWidth * 0.76) : width;
                    int finalHeight = height == 0 ? FrameLayout.LayoutParams.WRAP_CONTENT : width;
                    window.setLayout(finalWidth, finalHeight);
                }
            });
        }
    }

    /**
     * 设置dialog的背景--传入资源id
     */
    public void setBackGroundDrawableResource(int drawableResId) {
        Window window = dialog.getWindow();
        if (null != window) {
            if (0 == drawableResId) {
                drawableResId = R.drawable.shape_bk_dialog_ant;
            }
            window.setBackgroundDrawableResource(drawableResId);
        }
    }

    /**
     * 设置背景图--传入drawable对象
     */
    public void setBackGroundDrawable(Drawable drawable) {
        Window window = dialog.getWindow();
        if (null != window) {
            if (null == drawable) {
                drawable = context.getResources().getDrawable(R.drawable.shape_bk_dialog_ant);
            }
            window.setBackgroundDrawable(drawable);
        }
    }

    /**
     * 设置确定按钮的问题及其点击事件
     */
    public void setPostiveButton(String des, final AntDialogClickListener clickListener) {
        dialogBinding.setIsConfirmBtShow(true);
        dialogBinding.tvConfirmBT.setText(des);
        if (null != clickListener) {
            dialogBinding.tvConfirmBT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissDialog();
                    clickListener.onClick(v);
                }
            });
        }
    }

    public void setPostiveButton(int strResId, final AntDialogClickListener clickListener) {
        dialogBinding.setIsConfirmBtShow(true);
        dialogBinding.tvConfirmBT.setText(context.getResources().getString(strResId));
        if (null != clickListener) {
            dialogBinding.tvConfirmBT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissDialog();
                    clickListener.onClick(v);
                }
            });
        }
    }

    /**
     * 取消按钮的点击事件
     */
    public void setNegativeButton(String des, final AntDialogClickListener clickListener) {
        dialogBinding.setIsCancleBtShow(true);
        dialogBinding.tvCancleBT.setText(des);
        if (null != clickListener) {
            dialogBinding.tvCancleBT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissDialog();
                    clickListener.onClick(v);
                }
            });
        }
    }

    public void setNegativeButton(int strResId, final AntDialogClickListener clickListener) {
        dialogBinding.setIsCancleBtShow(true);
        dialogBinding.tvCancleBT.setText(context.getResources().getString(strResId));
        if (null != clickListener) {
            dialogBinding.tvCancleBT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissDialog();
                    clickListener.onClick(v);
                }
            });
        }
    }

    /**
     * 设置标题
     */
    public void setTitle(String title) {
        dialogBinding.setIsTitleShow(true);
        dialogBinding.tvTitle.setText(title);
    }

    public void setTitle(int strResId) {
        dialogBinding.setIsTitleShow(true);
        dialogBinding.tvTitle.setText(context.getResources().getString(strResId));
    }

    /**
     * 设置消息体
     */
    public void setMessage(String message) {
        dialogBinding.setIsMsgShow(true);
        dialogBinding.tvMsg.setText(message);
    }

    public void setMessage(int strResId) {
        dialogBinding.setIsMsgShow(true);
        dialogBinding.tvMsg.setText(context.getResources().getString(strResId));
    }

    /**
     * 修改Dialog阴影区域的灰度百分比
     * <p>
     * 取值 0-1.
     */
    public void setDim(float rate) {
        Window window = dialog.getWindow();
        if (null != window) {
            if (rate < 0) {
                rate = 0;
            } else if (rate > 1) {
                rate = 1;
            }
            window.setDimAmount(rate);
        }
    }

    /**
     * 点击非内容区域是否可以关闭
     */
    public void setCancelable(boolean bool) {
        dialog.setCancelable(bool);
    }

    /**
     * 对外暴露点击事件的自定义接口
     */
    public interface AntDialogClickListener {
        void onClick(View view);
    }
}
