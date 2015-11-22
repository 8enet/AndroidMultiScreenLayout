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

import android.app.Application;
import android.content.res.Configuration;

import com.zzzmode.android.library.LayoutSizeCompat;


public class AppGlobalContext extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //建议在此初始化
        LayoutSizeCompat.init(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //建议回调此方法
        LayoutSizeCompat.onConfigurationChanged(this,newConfig);
    }
}
