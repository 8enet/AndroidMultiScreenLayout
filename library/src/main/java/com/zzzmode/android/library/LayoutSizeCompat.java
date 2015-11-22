/*
 * Copyright (C) 2015 zlcn2200@yeah.net . All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * 多屏幕适配
 */
package com.zzzmode.android.library;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class LayoutSizeCompat {

    private static volatile Point sScreenSize = null;
    private static volatile int sStatusBarHeight;
    private static int sOrientation = Configuration.ORIENTATION_PORTRAIT; //default

    /**
     * init component
     * @param context
     */
    public static void init(Context context) {
        if (isGonePoint(sScreenSize))
            configSize(context);
    }

    /**
     * get the physical screen size
     * @param context
     * @param size
     */
    public static void getPhysicalSize(Context context, Point size) {
        if (context == null || size == null) {
            throw new NullPointerException("args is null !");
        }
        final WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        final Display display = wm.getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealSize(size);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            try {
                size.x = ((Integer) Display.class.getMethod("getRawWidth").invoke(display));
                size.y = ((Integer) Display.class.getMethod("getRawHeight").invoke(display));
            } catch (Exception e) {
            }
        }
        if (isGonePoint(size)) {
            final DisplayMetrics metrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(metrics);
            size.x = metrics.widthPixels;
            size.y = metrics.heightPixels;
        }

    }

    /**
     * get the status bar height
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {
        int ret = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            ret = context.getResources().getDimensionPixelSize(resourceId);
        }
        return ret;
    }

    /**
     * when configuration change will call this method,e.g screen rotation
     * @param context
     * @param newConfig
     */
    public static void onConfigurationChanged(Context context, Configuration newConfig) {
        if (newConfig.orientation != sOrientation) {
            configSize(context);
        }
    }

    //config the local screensize,statusbarheight and orientation
    private static void configSize(Context context) {
        sScreenSize = new Point();
        sStatusBarHeight = getStatusHeight(context);
        getPhysicalSize(context, sScreenSize);
        if (sScreenSize.x < sScreenSize.y) {
            sOrientation = Configuration.ORIENTATION_PORTRAIT;
        } else {
            sOrientation = Configuration.ORIENTATION_LANDSCAPE;
        }
    }

    private static boolean isGonePoint(Point p) {
        return p == null || p.x == 0 || p.y == 0;
    }


    /*    ---    */

    private DesignSizeBuilder mDesignSize;

    private LayoutSizeCompat(DesignSizeBuilder designSize) {
        this.mDesignSize = designSize;
    }


    /**
     * calculate the compatible width
     * @param px orgin width pixels
     * @return
     */
    public int w(int px) {
        if (isGonePoint(sScreenSize)) {
            return px;
        } else {
            return px * sScreenSize.x / mDesignSize.designUiWidth;
        }
    }

    /**
     * calculate the compatible height
     * @param px orgin height pixels
     * @return
     */
    public int h(int px) {
        if (isGonePoint(sScreenSize)) {
            return px;
        } else {
            return px * mDesignSize.availaleHeight / mDesignSize.designUiHeight;
        }
    }

    /**
     * adjust layout size of activity
     * @param activity
     */
    public void adjustSize(Activity activity) {
        if (activity != null) {
            final Window window = activity.getWindow();
            autoCheckStatusBar(window);
            adjustSize(window.getDecorView());
        }
    }

    /**
     * adjust layout size of dialog
     * @param dialog
     */
    public void adjustSize(Dialog dialog) {
        if (dialog != null) {
            final Window window = dialog.getWindow();
            autoCheckStatusBar(window);
            adjustSize(window.getDecorView());
        }
    }

    // check status bar
    private void autoCheckStatusBar(Window window) {
        int flags = window.getAttributes().flags;
        if ((flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) == WindowManager.LayoutParams.FLAG_FULLSCREEN) {
            mDesignSize.showStatusBar(false).calHeight();
        } else {
            mDesignSize.showStatusBar(true).calHeight();
        }
    }

    /**
     * adjust layout size of view
     * @param view
     */
    public void adjustSize(View view) {
        if (view == null) {
            return;
        }

        if (isGonePoint(sScreenSize)) {
            init(view.getContext());
        }

        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params != null) {
            if (params.width > 0) {
                params.width = w(params.width);
            }
            if (params.height > 0) {
                params.height = h(params.height);
            }
            if (params instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams mParams = (ViewGroup.MarginLayoutParams) params;
                mParams.leftMargin = w(mParams.leftMargin);
                mParams.topMargin = h(mParams.topMargin);
                mParams.rightMargin = w(mParams.rightMargin);
                mParams.bottomMargin = h(mParams.bottomMargin);
            }
        }

        view.setPadding(
                w(view.getPaddingLeft()),
                h(view.getPaddingTop()),
                w(view.getPaddingRight()),
                h(view.getPaddingBottom()));

        if (view instanceof TextView) {
            TextView tv = (TextView) view;
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, w((int) tv.getTextSize()));
            tv.setCompoundDrawablePadding(w(tv.getCompoundDrawablePadding()));
            final Drawable[] cds = tv.getCompoundDrawables();
            for (Drawable d : cds) {
                Rect bounds = d == null ? null : d.getBounds();
                if (bounds != null && !bounds.isEmpty()) {
                    bounds.set(
                            w(bounds.left),
                            h(bounds.top),
                            w(bounds.right),
                            h(bounds.bottom));
                }
            }
            tv.setCompoundDrawables(cds[0], cds[1], cds[2], cds[3]);
        }

        if (view instanceof ViewGroup) {
            final ViewGroup vg = (ViewGroup) view;
            final int count = vg.getChildCount();
            for (int i = 0; i < count; i++) {
                adjustSize(vg.getChildAt(i));
            }
        }
    }


    public static class DesignSizeBuilder implements Cloneable {
        private int designUiWidth;
        private int designUiHeight;
        private boolean showStatusBar = true;
        private int availaleHeight;

        /**
         * the design ui size
         * @param width
         * @param height
         */
        public DesignSizeBuilder(int width, int height) {
            this.designUiWidth = width;
            this.designUiHeight = height;
        }

        public DesignSizeBuilder() {
        }

        /**
         * design width
         * @param width
         * @return
         */
        public DesignSizeBuilder designWidth(int width) {
            this.designUiWidth = width;
            return this;
        }

        /**
         * design height
         * @param height
         * @return
         */
        public DesignSizeBuilder designHeight(int height) {
            this.designUiHeight = height;
            return this;
        }

        /**
         * is showing status bar ?
         * @param show
         * @return
         */
        public DesignSizeBuilder showStatusBar(boolean show) {
            this.showStatusBar = show;
            return this;
        }

        /**
         * current is full screen
         * @return
         */
        public DesignSizeBuilder fullScreen() {
            return showStatusBar(false);
        }

        /**
         * builder a layout compatible container
         * @return
         */
        public LayoutSizeCompat builder() {
            if (designUiWidth <= 0 || designUiHeight <= 0) {
                throw new IllegalArgumentException("designUiWidth or designUiHeight > 0 !!!");
            }
            calHeight();
            return new LayoutSizeCompat(this);
        }

        private void calHeight() {
            availaleHeight = sScreenSize.y;
            if (showStatusBar) {
                availaleHeight = sScreenSize.y - sStatusBarHeight;
            }
        }

        @Override
        protected DesignSizeBuilder clone() {
            try {
                return (DesignSizeBuilder) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
