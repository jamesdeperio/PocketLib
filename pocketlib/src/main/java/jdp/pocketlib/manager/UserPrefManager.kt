package jdp.pocketlib.manager
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import jdp.pocketlib.util.AESUtil

class UserPrefManager( val context: Context, keyValue: String) {
    var prefList:MutableMap<String,SharedPreferences> = HashMap()
    var aes = AESUtil(keyValue)

    inline fun <reified  T> set( key:String,value: T): UserPrefManager {
        if (prefList.none { it.key==key+"|"+T::class.java.simpleName }) {
            prefList[key+"|"+T::class.java.simpleName] = context.getSharedPreferences(T::class.java.simpleName, Context.MODE_PRIVATE)
        }
        val prefsEditor:SharedPreferences.Editor = prefList[key+"|"+T::class.java.simpleName]!!.edit()
        val data:String= aes.encrypt(Gson().toJson(value).toByteArray())
        prefsEditor.putString(key,data )
        prefsEditor.apply()
        return this
    }

    inline fun <reified T> get(key: String):T {
        val json = prefList[key+"|"+T::class.java.simpleName]!!.getString(key, "")!!
        return Gson().fromJson<T>(aes.decrypt(json), T::class.java)
    }
}