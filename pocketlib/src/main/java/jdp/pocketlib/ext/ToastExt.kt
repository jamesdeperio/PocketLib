package jdp.pocketlib.ext

import android.content.Context
import android.widget.Toast

inline  fun Context.toast(message:String) = Toast.makeText(this,message,Toast.LENGTH_LONG).show()