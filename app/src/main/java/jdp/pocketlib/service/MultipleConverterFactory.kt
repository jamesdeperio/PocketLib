package jdp.pocketlib.service

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type
import java.util.*

class MultipleConverterFactory(private val mFactoryMap: Map<Class<*>, Converter.Factory>) : Converter.Factory() {

    override fun responseBodyConverter(type: Type?, annotations: Array<Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *>? {
        for (annotation in annotations!!) {
            val factory = mFactoryMap[annotation.annotationClass.javaObjectType]
            if (factory != null) {
                return factory.responseBodyConverter(type!!, annotations, retrofit!!)
            }
        }
        val jsonFactory = mFactoryMap[Gson::class.java]
        return jsonFactory?.responseBodyConverter(type!!, annotations, retrofit!!)
    }

    internal class Builder {
        private var mFactoryMap: MutableMap<Class<*>, Converter.Factory> = LinkedHashMap()
        fun add(factoryType: Class<out Annotation>?, factory: Converter.Factory?): Builder {
            if (factoryType == null) throw NullPointerException("factoryType is null")
            if (factory == null) throw NullPointerException("factory is null")
            mFactoryMap[factoryType] = factory
            return this
        }

        fun build(): MultipleConverterFactory {
            return MultipleConverterFactory(mFactoryMap)
        }

    }
}