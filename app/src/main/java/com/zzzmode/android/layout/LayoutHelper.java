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

package com.zzzmode.android.layout;


import com.zzzmode.android.library.LayoutSizeCompat;

public final class LayoutHelper {
    //这个对象没有必要每次new，初始化标注的ui尺寸几个即可
    public static final LayoutSizeCompat Layout1080x1920 = new LayoutSizeCompat.DesignSizeBuilder(1080,1920).builder();

    public static final LayoutSizeCompat Layout768x1280NoTitle = new LayoutSizeCompat.DesignSizeBuilder(768,1280).fullScreen().builder();

    //...
}
