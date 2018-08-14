package jdp.pocketlib.service

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type
import java.util.*

class MultipleConverterFactory(private val factory: Map<Class<*>, Converter.Factory>) : Converter.Factory() {

    override fun responseBodyConverter(type: Type?, annotations: Array<Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *>? {
        annotations!!
                .map { factory[it.annotationClass.javaObjectType] }
                .filter { it!= null}
                .forEach {
                return it!!.responseBodyConverter(type!!, annotations, retrofit!!)
                }
        return  factory[JSONFormat::class.java]!!.responseBodyConverter(type!!, annotations, retrofit!!)
    }
    open class Builder {
        private var factoryMap: MutableMap<Class<*>, Converter.Factory> = LinkedHashMap()

        fun addCustomConverterFactory(factoryType: Class<out Annotation>, factory: Converter.Factory): Builder {
            factoryMap[factoryType] = factory
            return this
        }

        fun setXMLConverterFactory( factory: Converter.Factory): Builder {
            factoryMap[XMLFormat::class.java] = factory
            return this
        }

        fun setJSONConverterFactory( factory: Converter.Factory): Builder {
            factoryMap[JSONFormat::class.java] = factory
            return this
        }

        open fun build(): MultipleConverterFactory {
            return MultipleConverterFactory(factoryMap)
        }

    }
}