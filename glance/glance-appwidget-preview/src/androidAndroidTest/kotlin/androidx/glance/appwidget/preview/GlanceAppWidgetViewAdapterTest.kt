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

package androidx.glance.appwidget.preview

import androidx.glance.appwidget.preview.test.R
import android.app.Activity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.test.filters.MediumTest
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@MediumTest
class GlanceAppWidgetViewAdapterTest {
    @Suppress("DEPRECATION")
    @get:Rule
    val activityTestRule = androidx.test.rule.ActivityTestRule(TestActivity::class.java)

    private lateinit var glanceAppWidgetViewAdapter: GlanceAppWidgetViewAdapter

    @Before
    fun setup() {
        glanceAppWidgetViewAdapter =
            activityTestRule.activity.findViewById(R.id.glance_appwidget_view_adapter)
    }

    private fun initAndInflate(
        className: String,
        methodName: String,
    ) {
        activityTestRule.runOnUiThread {
            glanceAppWidgetViewAdapter.init(className, methodName)
            glanceAppWidgetViewAdapter.requestLayout()
        }
    }

    private inline fun <reified T> ViewGroup.getChildOfType(count: Int = 0): T? {
        var i = 0
        (0 until childCount).forEach {
            val child = getChildAt(it)
            if (child is T) {
                if (i == count) {
                    return child
                }
                i += 1
            }
        }
        return null
    }

    private fun viewNotFoundMsg(viewTypeName: String, composableName: String) =
        "Could not find the $viewTypeName View matching $composableName"

    @Test
    fun testFirstGlancePreview() {
        initAndInflate(
            "androidx.glance.appwidget.preview.GlanceAppWidgetPreviewsKt",
            "FirstGlancePreview")

        activityTestRule.runOnUiThread {
            val rootComposable = glanceAppWidgetViewAdapter.getChildAt(0) as ViewGroup
            val linearLayoutColumn = rootComposable.getChildOfType<LinearLayout>()
            assertNotNull(viewNotFoundMsg("LinearLayout", "Column"), linearLayoutColumn)
            val textView = linearLayoutColumn!!.getChildOfType<TextView>()
            assertNotNull(viewNotFoundMsg("TextView", "Text"), textView)
            val linearLayoutRow = linearLayoutColumn.getChildOfType<LinearLayout>()
            assertNotNull(viewNotFoundMsg("LinearLayout", "Row"), linearLayoutRow)
            // Depending on the API version Button might be wrapped in the RelativeLayout
            val button1 =
                linearLayoutRow!!.getChildOfType<Button>()
                    ?: linearLayoutRow.getChildOfType<RelativeLayout>()!!.getChildOfType<Button>()
            val button2 =
                linearLayoutRow.getChildOfType<Button>(1)
                    ?: linearLayoutRow.getChildOfType<RelativeLayout>(1)!!.getChildOfType<Button>()
            assertNotNull(viewNotFoundMsg("Button", "Button"), button1)
            assertNotNull(viewNotFoundMsg("Button", "Button"), button2)
        }
    }

    companion object {
        class TestActivity : Activity() {
            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.glance_appwidget_adapter_test)
            }
        }
    }
}