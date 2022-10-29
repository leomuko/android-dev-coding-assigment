package com.ensibuuko.android_dev_coding_assigment.util

import okhttp3.RequestBody
import org.json.JSONObject

class Helpers {

    companion object{
        fun createJsonRequestBody(vararg params: Pair<String, Any>) =
            RequestBody.create(
                okhttp3.MediaType.parse("application/json; charset=utf-8"),
                JSONObject(mapOf(*params)).toString())
    }
}