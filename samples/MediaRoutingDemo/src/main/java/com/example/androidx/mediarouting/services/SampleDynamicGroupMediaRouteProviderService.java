/*
 * Copyright 2022 The Android Open Source Project
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

package com.example.androidx.mediarouting.services;

import androidx.annotation.NonNull;
import androidx.mediarouter.media.MediaRouteProvider;
import androidx.mediarouter.media.MediaRouteProviderService;

import com.example.androidx.mediarouting.providers.SampleDynamicGroupMediaRouteProvider;

/**
 * Demonstrates how to register a custom media route provider service
 * using the support library.
 *
 * @see SampleDynamicGroupMediaRouteProvider
 */
public class SampleDynamicGroupMediaRouteProviderService extends MediaRouteProviderService {
    @NonNull
    @Override
    public MediaRouteProvider onCreateMediaRouteProvider() {
        return new SampleDynamicGroupMediaRouteProvider(this);
    }
}
