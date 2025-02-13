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

package androidx.credentials.playservices

import androidx.credentials.playservices.controllers.CreatePublicKeyCredential.PublicKeyCredentialControllerUtility
import com.google.android.gms.common.ConnectionResult
import org.json.JSONArray
import org.json.JSONObject

class TestUtils {
    companion object {

        fun b64Encode(byteArray: ByteArray): String {
            return PublicKeyCredentialControllerUtility.b64Encode(byteArray)
        }

        fun b64Decode(b64String: String): ByteArray {
            return PublicKeyCredentialControllerUtility.b64Decode(b64String)
        }

        fun isAlgSupported(alg: Int): Boolean {
            return PublicKeyCredentialControllerUtility.checkAlgSupported(alg)
        }

        /**
         * // TODO remove this or modify so it has forced checks.
         * Given a superset and a subset json, this figures out if the subset can be found
         * within the superset by recursively checking for values that exist in the subset
         * also existing in the superset in the same format. Note this means that the superset
         * is always equal to or larger than the subset json but should contain every key and
         * **sub-value** pair found in the subset. I.e. for example:
         * ```
         * storeA = {a : {b : 2,  c : 2, d : 2, e : {x : 3, y : 3, z : 3} }, q: 5}
         * storeB = {a : {b : 2, d : 2, e : {x : 3} }, q : 5}
         * requiredKeys = {a:{b:true, d:true, {e:{x:true}}, q:true}
         * isSubsetJson(storeA, storeB, requiredKeys) //-> true
         * ```
         *
         * Note this is lax on json arrays and their inner objects, that can be extended if
         * required.
         *
         * @param superset the superset json that should have all the keys and values and subvalues
         * that the subset contains as well as new keys and values/subvalues the subset does not
         * contain
         * @param subset the subset json that should have equal to or less keys and subvalues than
         * the superset
         * @return a boolean indicating if the subset was truly a subset of the superset it was
         * tested with
         */
        fun isSubsetJson(superset: JSONObject, subset: JSONObject): Boolean {
            val keys = subset.keys()
            for (key in keys) {
                if (!superset.has(key)) {
                    return false
                }
                val values = subset.get(key)
                val superValues = superset.get(key)

                if (values::class.java != superValues::class.java) {
                    return false
                }
                if (values is JSONObject) {
                    if (!isSubsetJson(superValues as JSONObject, values)) {
                        return false
                    }
                } else if (values is JSONArray) {
                    val subSet = jsonArrayToStringSet(values)
                    val superSet = jsonArrayToStringSet(superValues as JSONArray)
                    if (!superSet.containsAll(subSet)) {
                        return false
                    }
                    // TODO("For specific sequences, place into a treeset (sorted by specific
                    // TODO("required identifiers) and compare subset to superset"))
                } else {
                    if (!values.equals(superValues)) {
                        return false
                    }
                }
            }
            return true
        }

        private fun jsonArrayToStringSet(values: JSONArray): Set<String> {
            val setValue = LinkedHashSet<String>()
            val len = values.length()
            for (i in 0 until len) {
                setValue.add(values[i].toString())
            }
            return setValue
        }

        @JvmStatic
        val ConnectionResultFailureCases = arrayListOf(
            ConnectionResult.UNKNOWN, ConnectionResult.API_DISABLED, ConnectionResult.CANCELED,
            ConnectionResult.API_DISABLED_FOR_CONNECTION, ConnectionResult.API_UNAVAILABLE,
            ConnectionResult.DEVELOPER_ERROR, ConnectionResult.INTERNAL_ERROR,
            ConnectionResult.INTERRUPTED, ConnectionResult.INVALID_ACCOUNT,
            ConnectionResult.LICENSE_CHECK_FAILED, ConnectionResult.NETWORK_ERROR,
            ConnectionResult.RESOLUTION_ACTIVITY_NOT_FOUND, ConnectionResult.RESOLUTION_REQUIRED,
            ConnectionResult.RESTRICTED_PROFILE, ConnectionResult.SERVICE_DISABLED,
            ConnectionResult.SERVICE_INVALID, ConnectionResult.SERVICE_MISSING,
            ConnectionResult.SERVICE_MISSING_PERMISSION, ConnectionResult.SERVICE_UPDATING,
            ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED, ConnectionResult.SIGN_IN_FAILED,
            ConnectionResult.SIGN_IN_REQUIRED, ConnectionResult.TIMEOUT)
    }
}