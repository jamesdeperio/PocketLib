package jdp.pocketlib.manager
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

class UserPrefManager( val context: Context) {
    var prefList:MutableMap<String,SharedPreferences> = HashMap()
    @Synchronized
    inline fun <reified  T> set( key:String,value: T): UserPrefManager {
        if (prefList.none { it.key==key+"|"+T::class.java.simpleName }) {
            prefList[key+"|"+T::class.java.simpleName] = context.getSharedPreferences(T::class.java.simpleName, Context.MODE_PRIVATE)
        }
        val prefsEditor:SharedPreferences.Editor = prefList[key+"|"+T::class.java.simpleName]!!.edit()
        prefsEditor.putString(key,  Gson().toJson(value))
        prefsEditor.apply()
        return this
    }
    @Synchronized
    inline fun <reified T> get(key: String):T {
        val json = prefList[key+"|"+T::class.java.simpleName]!!.getString(key, "")!!
        return Gson().fromJson<T>(json, T::class.java)
    }
}