![alt text](https://github.com/jamesdeperio/PocketLib/blob/master/pocketlib.png "PocketLib")

[![Release](https://jitpack.io/v/jamesdeperio/PocketLib.svg)](https://jitpack.io/#jamesdeperio/PocketLib)
[![GitHub release](https://img.shields.io/github/release/jamesdeperio/PocketLib.svg)](https://GitHub.com/jamesdeperio/PocketLib/releases/)
[![GitHub tag](https://img.shields.io/github/tag/jamesdeperio/PocketLib.svg)](https://GitHub.com/jamesdeperio/PocketLib/tags/)
[![GitHub commits](https://img.shields.io/github/commits-since/jamesdeperio/PocketLib/2.0.0.svg)](https://GitHub.com/jamesdeperio/PocketLib/commit/)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.2.71-green.svg?style=flat-square)](http://kotlinlang.org)
[![Build Status](https://img.shields.io/travis/jamesdeperio/PocketLib.svg?style=flat-square)](https://travis-ci.org/jamesdeperio/PocketLib)
[![License](https://img.shields.io/badge/License%20-Apache%202-337ab7.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Maintenance](https://img.shields.io/badge/Maintained%3F-yes-green.svg)](https://GitHub.com/jamesdeperio/PocketLib/graphs/commit-activity)
[![Open Source Helpers](https://www.codetriage.com/jamesdeperio/pocketlib/badges/users.svg)](https://www.codetriage.com/jamesdeperio/pocketlib)
[![HitCount](http://hits.dwyl.io/jamesdeperio/PocketLib.svg)](http://hits.dwyl.io/jamesdeperio/PocketLib)
[![Jitpack Downloads](https://jitpack.io/v/jamesdeperio/PocketLib/month.svg)](https://jitpack.io/#jamesdeperio/PocketLib)

[![Throughput Graph](https://graphs.waffle.io/jamesdeperio/PocketLib/throughput.svg)](https://waffle.io/jamesdeperio/PocketLib/metrics/throughput)
[![Waffle.io - Columns and their card count](https://badge.waffle.io/jamesdeperio/PocketLib.svg?columns=all)](https://waffle.io/jamesdeperio/PocketLib)

___
### [READ FULL DOCUMENTATION FOR V2.x.x](https://jamesdeperio.github.io/pocketlib/) 
### TODO : DOCUMENTATION FOR V3.x.x
### [DEMO APK](https://github.com/jamesdeperio/CodePocketBuilderDemo/blob/master/app-debug.apk)
___
## SAMPLE SCREENSHOTS
* **EASY TO CREATE MULTIPLE VIEWHOLDER**
___
![alt text](https://github.com/jamesdeperio/PocketLib/blob/master/multipleviewholder.png "multipleviewholder")
___
* **Spinner**
___
![alt text](https://github.com/jamesdeperio/PocketLib/blob/master/pocketspinner.png "pocketspinner")
___
* **Customizable Dialog**
___
![alt text](https://github.com/jamesdeperio/PocketLib/blob/master/dialog.png "pocketdialog")
___
## CHANGELOG
###  3.1.0
* Added: FragmentManager.requestPermissions()
* Added: NFCActivity
* Added: FragmentManager.useMultiSelectGallery()
* Added: FragmentManager.useGallery()
* Added: FragmentManager.useCamera()
* Updated: Dialog.setAnimation()
* Updated: Dialog.TYPE.STATIC_IMAGE
* Updated: Dialog.image
###  3.0.0
 ##### MAJOR UPDATES
* Refactored: the widgets and layout manager
  --- removed the prefix "Pocket"
* Improved: Spinner Dialog
* Improved: Spinner Widget
* Added: DateRangePicker
* Added: DateRangePickerFragmentDialog
* Added: UserPrefManager
   --- data stored are encrypted 
* Added: PageListAdapter (same implementation as Adapter)
* Added: GaugeView
* Added: TopSheetDialog
* Added: Utilities
``` kotlin  
// DEVICE EXT
getIPAddress(): String?
getCPUTemperature(): Float
String.getAvailablelStorageSize(): String
String.getTotalStorageSize(): String
Context.getRamInfo(): String
Context.isNetworkConnectionAvailable(): Boolean
WindowManager.getScreenSize(): Point
Activity.lockOrientation()
Context.setVolume(volume:Int)
View.enableFullscreen()
Activity.unlockOrientation() 
Activity.unlockOrientation() 
Activity.unlockOrientation() 
Activity.unlockOrientation() 
```  
``` kotlin  
// EVALUATION EXT
String.isNumber():Boolean
String.hasNumber():Boolean 
String.isDouble():Boolean 
String.isInteger():Boolean 
String.isTimeBetween(argStartTime: String, argEndTime: String): Boolean  //FORMAT "HH:mm:ss"
Date.isTimeBetween(argStartTime: Date, argEndTime: Date): Boolean
```
``` kotlin  
// FRAGMENT MANAGER EXT
FragmentManager.add(allowStateLoss:Boolean=false,isAnimationEnabled: Boolean = true,properties: FragmentTransaction.() -> Unit) 
FragmentManager.replace(allowStateLoss:Boolean=false,isAnimationEnabled: Boolean = true,properties: FragmentTransaction.() -> Unit)
FragmentManager.createViewPager(properties: PageBuilder.() -> Unit) 
```
``` kotlin  
// IMAGE EXT
 Bitmap.encodeToBase64String(compressFormat: Bitmap.CompressFormat=Bitmap.CompressFormat.PNG, quality: Int=80): String
String.toBitmap(): Bitmap
```
``` kotlin  
// LOG EXT
Any.VERBOSE(message: String)
Any.DEBUG(message: String)
Any.INFO(message: String)
Any.WARNING(message: String)
Any.ERROR(message: String) 
Any.WTF(message: String)
```
``` kotlin  
// ROOTED EXT
String.installAPK(): Boolean
shutdownDevice(): Boolean 
rebootDevice(): Boolean 
```

#### 2.2.0
- ADDED A VIEW: PocketSpinner
- UPDATED: changed button style in PocketDialog
- ADDED: setSpinnerView(?) method in PocketSpinnerDialog
#### v2.1.1
- ADDED: isDialogShowing() method for PocketDialog
- ADDED: PocketDialog.Type.DIALOG_SEARCH
- FIXED: action button margin
- FIXED: viewSeparator should not be visible by default in PocketDialog.Loader
- IMPROVED: reduced method count
- UPDATED: PocketDialog.Type.ERROR changed default lottieview animation
- UPDATED: gradle dependency to v3.2.1
- UPDATED: maven dependency to v2.1
- MERGED: PR of [@marjorietiozon](https://github.com/marjorietiozon) :<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- ADDED: default blank value for title in PageBuilder.addPage()<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- IMPROVED: optimized code for PageBuilder.setupWithViewPager
#### v2.1.0
* UPDATED: kotlin dependency to v1.2.71
* UPDATED: gradle dependency to v3.2.0
* UPDATED: lottie dependency to v2.7.0
* UPDATED: android support dependency to v28.0.0
* ADDED: rxjava dependency v2.2.2
* ADDED: rxandroid dependency v2.1.0
* ADDED: EventPublisher class use to listen in a channel.
* CHANGES: 
- To use BaseSwipeFragment include **'me.yokeyword:swipebackfragment:0.4.0'** as dependency
- To use EventPublisher include **rxjava & rxandroid** as dependency
- To use PocketDialog include **'com.airbnb.android:lottie:2.7.0'** as dependency
#### v2.0.3
* FIXED: PocketSpinnerDialog error in inflating PocketRecyclerView
* FIXED: PocketDialog.Type.DIALOG_NO_INTERNET_CONNECTION default description
* FIXED: PocketDialog.Type.DIALOG_NO_INTERNET_CONNECTION default lottieview repeat count
* UPDATED: PocketDialog setCancelable to false by default
* UPDATED: PocketDialog.Type.Loading changed dialog to public
* UPDATED: PocketSpinnerDialog support lambda for listener method
* UPDATED: changed listener to setOnItemSelectedListener
* ADDED: new method (setButtonAsSpinner) to assign onclicklistener and view to display text in PocketSpinnerDialog.onItemSelected
* UPDATED: add view parameter to PocketSpinnerDialog.Listener.onItemSelected
#### ~~v2.0.2 (do not used)~~
* Fixed: PocketDialog.Type.NoInternetConnection default lottieview
#### v2.0.1
* updated: kotlin dependency
* exclude 'META-INF/app_release.kotlin_module'
#### v2.0.0
* version 2.x of codepocket (renamed)
* base class for activity,fragment,adapter,viewholder
* pagebuider for view pager
* navigate util
* custom view
* added feature in 2.x:
* pocketdialog
* pocketspinnerdialog
* -Removed: retrofit module has been moved to separate library
___
## DEPENDENCIES
Thanks to the author of external Library used:
* [org.jetbrains.kotlin:kotlin-stdlib-jdk7](https://github.com/JetBrains/kotlin/tree/master/libraries/stdlib)
* [com.android.support:appcompat-v7](https://developer.android.com/topic/libraries/support-library/)
* [com.android.support:design](https://developer.android.com/topic/libraries/support-library/)
* [com.android.support:recyclerview-v7](https://developer.android.com/topic/libraries/support-library/)
* [me.yokeyword:swipebackfragment](https://github.com/YoKeyword/SwipeBackFragment/)
* [com.airbnb.android:lottie](https://github.com/airbnb/lottie-android)


### LICENSE
```
Copyright 2018 The PocketLib Contributors

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
