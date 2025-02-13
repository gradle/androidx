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

package androidx.privacysandbox.tools.core.generator

import androidx.privacysandbox.tools.core.model.AnnotatedInterface
import androidx.privacysandbox.tools.core.model.ParsedApi
import com.squareup.kotlinpoet.CodeBlock

class ClientBinderCodeConverter(api: ParsedApi) : BinderCodeConverter(api) {
    override fun convertToInterfaceModelCode(
        annotatedInterface: AnnotatedInterface,
        expression: String
    ): CodeBlock = CodeBlock.of("%T(%L)", annotatedInterface.clientProxyNameSpec(), expression)

    override fun convertToInterfaceBinderCode(
        annotatedInterface: AnnotatedInterface,
        expression: String
    ): CodeBlock =
        CodeBlock.of(
            "(%L as %T).remote", expression, annotatedInterface.clientProxyNameSpec()
        )
}