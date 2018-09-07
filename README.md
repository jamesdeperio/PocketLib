![alt text](https://github.com/jamesdeperio/PocketLib/blob/master/pocketlib.png "PocketLib")

[![Release](https://jitpack.io/v/jamesdeperio/PocketLib.svg)](https://jitpack.io/#jamesdeperio/PocketLib)
[![GitHub release](https://img.shields.io/github/release/jamesdeperio/PocketLib.svg)](https://GitHub.com/jamesdeperio/PocketLib/releases/)
[![GitHub tag](https://img.shields.io/github/tag/jamesdeperio/PocketLib.svg)](https://GitHub.com/jamesdeperio/PocketLib/tags/)
[![GitHub commits](https://img.shields.io/github/commits-since/jamesdeperio/PocketLib/v2.0.3.svg)](https://GitHub.com/jamesdeperio/PocketLib/commit/)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.2.61-green.svg?style=flat-square)](http://kotlinlang.org)
[![Build Status](https://img.shields.io/travis/jamesdeperio/PocketLib.svg?style=flat-square)](https://travis-ci.org/jamesdeperio/PocketLib)
[![HitCount](http://hits.dwyl.io/jamesdeperio/PocketLib.svg)](http://hits.dwyl.io/jamesdeperio/PocketLib)
[![License](https://img.shields.io/badge/License%20-Apache%202-337ab7.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Maintenance](https://img.shields.io/badge/Maintained%3F-yes-green.svg)](https://GitHub.com/jamesdeperio/PocketLib/graphs/commit-activity)
___
### [READ FULL DOCUMENTATION](https://jamesdeperio.github.io/pocketlib/) 
### [DEMO APK](https://github.com/jamesdeperio/CodePocketBuilderDemo/blob/master/app-debug.apk)
___
## CHANGELOG
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
#### v2.0.1
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
