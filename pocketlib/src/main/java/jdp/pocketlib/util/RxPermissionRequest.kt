package jdp.pocketlib.util


import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import jdp.pocketlib.base.BaseFragment
import jdp.pocketlib.manager.Bus
import jdp.pocketlib.manager.EventBusManager



@SuppressLint("ValidFragment")
private class TempPermissionFragment(private val eventBusManager: EventBusManager,  vararg val permissions: String) :BaseFragment() {
    override fun onInitialization(savedInstanceState: Bundle?) {
        requestPermissions(permissions,1)
    }
    override fun onViewDidLoad(savedInstanceState: Bundle?) {}
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        (0 until grantResults.size).forEach { i ->
            permissions.filter {  (permissions[i]==it) }
                .forEach { eventBusManager.sendEvent(RxPermissionRequest.PERMISSION_REQUEST,PermissionResult(it,true)) }
        }
    //    eventBusManager.doOnCompl
    }
}
 data class PermissionResult( var name:String="", var result:Boolean=false)
object RxPermissionRequest {
    const val PERMISSION_REQUEST="PERMISSION_REQUEST"
    fun requestPermission(vararg permissions:String,fragmentManager:FragmentManager,callback:(bitmap:PermissionResult)-> Unit) {
        val eventBusManager= EventBusManager(Bus.PublishSubject)
        val tempFragment :Fragment= TempPermissionFragment(eventBusManager, *permissions)
        fragmentManager.beginTransaction()
            .add(tempFragment, tempFragment.javaClass.simpleName)
            .commit()
        eventBusManager.subscribeReceiver(PERMISSION_REQUEST) {
            callback(it as PermissionResult)
        }
    }
}