package com.example.uaspads

import com.example.uaspads.TaskInfo
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.reflect.Type

class TaskInfoMoshiAdapterFactory : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        if (type == TaskInfo::class.java) {
            val moshi = Moshi.Builder().build()
            val typeAdapter = moshi.adapter<TaskInfo>(TaskInfo::class.java)
            return Converter<ResponseBody, TaskInfo> { body ->
                try {
                    typeAdapter.fromJson(body.source())
                } catch (e: JsonDataException) {
                    throw RuntimeException(e)
                }
            }
        }
        return null
    }

    override fun requestBodyConverter(type: Type, parameterAnnotations: Array<out Annotation>, fieldAnnotations: Array<out Annotation>, retrofit: Retrofit): Converter<TaskInfo, RequestBody>? {
        if (type == TaskInfo::class.java) {
            val moshi = Moshi.Builder().build()
            val typeAdapter = moshi.adapter<TaskInfo>(TaskInfo::class.java)
            return Converter<TaskInfo, RequestBody> { value ->
                try {
                    RequestBody.create("application/json".toMediaTypeOrNull(), typeAdapter.toJson(value))
                } catch (e: JsonDataException) {
                    throw RuntimeException(e)
                }
            }
        }
        return null
    }

    companion object {
        @JvmStatic
        fun create(): TaskInfoMoshiAdapterFactory {
            return TaskInfoMoshiAdapterFactory()
        }
    }
}