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

package androidx.camera.core.imagecapture

import android.graphics.ImageFormat
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.core.impl.CaptureBundle
import androidx.camera.core.impl.ImageCaptureConfig
import androidx.camera.core.impl.ImageInputConfig
import androidx.camera.testing.fakes.FakeCaptureStage
import androidx.camera.testing.fakes.FakeImageInfo
import androidx.camera.testing.fakes.FakeImageProxy

/**
 * Utility methods for testing image capture.
 */
object Utils {

    /**
     * Creates an empty [ImageCaptureConfig] so [ImagePipeline] constructor won't crash.
     */
    fun createEmptyImageCaptureConfig(): ImageCaptureConfig {
        val builder = ImageCapture.Builder().setCaptureOptionUnpacker { _, _ -> }
        builder.mutableConfig.insertOption(ImageInputConfig.OPTION_INPUT_FORMAT, ImageFormat.JPEG)
        return builder.useCaseConfig
    }

    fun createCaptureBundle(stageIds: IntArray): CaptureBundle {
        return CaptureBundle {
            stageIds.map { stageId ->
                FakeCaptureStage(stageId, null)
            }
        }
    }

    fun createFakeImage(tagBundleKey: String, stageId: Int): ImageProxy {
        val imageInfo = FakeImageInfo()
        imageInfo.setTag(tagBundleKey, stageId)
        return FakeImageProxy(imageInfo)
    }
}