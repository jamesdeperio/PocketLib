# PocketLib
[![Release](https://jitpack.io/v/jamesdeperio/PocketLib.svg)](https://jitpack.io/#jamesdeperio/PocketLib)
[![License](https://img.shields.io/badge/License%20-Apache%202-337ab7.svg)](https://www.apache.org/licenses/LICENSE-2.0)

## TODO
* sample app for better understanding on how it works
## How to?

### Gradle
```groovy
dependencies {
    implementation 'com.github.jamesdeperio:PocketLib:2.0'
}
```
- better if you will consider using [CodePocketBuilder](https://github.com/jamesdeperio/CodePocketBuilder)
___
| Base Class        |
|:------------------------------------------------:|
|BaseActivity|
|BaseFragment|
|BaseSwipeFragment|
|PocketAdapter|
|PocketViewHolder|
* PocketAdapter Usage:
``` kotlin
 class YOURADAPTER : PocketAdapter() {
  var stringList: MutableList<String> = ArrayList()
   override fun getItemCount(): Int = stringList.size
 }
```
* PocketViewHolder Usage:
``` kotlin
 class YOURViewHolder( private val adapter: YOURADAPTER) : PocketViewHolder() {
    override fun onBindViewHolder(view: View, position: Int) {
    }
}
```

___
|Builder|
|:------------------------------------------------:|
|PageBuilder (for PocketViewPager)|
* PageBuilder Usage:
``` kotlin
   PageBuilder.build { 
            setViewPager(VIEW_PAGER)
            setFragmentManager(FRAGMENT_MANAGER)
            setTabLayout(TAB_LAYOUT)
            setPageTransformer(TRANSFORMER) // can be null
            addPage("tab1",YourFragment())
            addPage("tab2",YourFragment())
            addPage("tab3",YourFragment())
        }
```
___
|Util|
|:------------------------------------------------:|
|Navigate|
|CleanView|
* Navigate Usage:
```kotlin
//from activity / replace fragment
       Navigate.using(fragmentManager = fragmentManager!!)
                .change(layoutID = R.id.fragment_container)
                .to(fragmentToChange = YOUR_FRAGMENT)
                .withBackStack(isBackstackEnabled = false)
                .commit()
//from fragment with backstack / using BaseSwipeFragment
      Navigate.using(fragmentManager = fragmentManager!!)
                .change(layoutID = R.id.fragment_container)
                .from(currentFragment = fragment)
                .to(fragmentToChange = YOUR_FRAGMENT)
                .withBackStack(isBackstackEnabled = true)
                .commit()
```
___
|View|
|:------------------------------------------------:|
|PocketDialog|
|PocketSpinnerDialog<T>|
|PocketHorizontalScrollView|
|PocketNestedScrollView|
|PocketScrollView|
|PocketTabLayout|
|PocketViewPager|
|PocketRecyclerView|
 * PocketDialog Usage :
```kotlin
  // DEFAULT DESIGN AND LOTTIE ANIMATION:
  // PocketDialog.Type.DIALOG_NO_INTERNET_CONNECTION
  // PocketDialog.Type.DIALOG_WARNING
  // PocketDialog.Type.DIALOG_REWARD
  // PocketDialog.Type.DIALOG_BASIC
  // PocketDialog.Type.DIALOG_ERROR
  // PocketDialog.Type.DIALOG_SUCCESS
  // PocketDialog.Type.DIALOG_LOADER
  var dialog = PocketDialog(context = context,type = PocketDialog.Type.DIALOG_WARNING,isFullScreen = false).apply {
      //  okAction (customized ok button)
       // cancelAction (customized cance button)
     //   title (customized title textview)
     //   description (customized description textview)
     //   view (customized dialog view)
     //   lottie (customized lottie animation)
    }
  
   dialog.show()
```
  
* PocketSpinnerDialog<T> Usage :
```kotlin
    data class Person (var id:Int=0,  var name:String? = null, var address:String? = null )   
     
    var dialog= PocketSpinnerDialog<Person>(context = context,isFullScreen = false).apply {
            this.listener= object :PocketSpinnerDialog.Listener<Person> {
                override fun onItemSelected(selectedObject: Person, selectedItem: String, selectedIndex: Int) {
                    //display selected item to textview or button
                }
            }
            val person1 = Person(id = 1,name = "ABC",address = null)
            this.addItem(itemObject = person1,itemString = person1.name!!)
            val person2 = Person(id = 1,name = "XYZ",address = null)
            this.addItem(itemObject = person2,itemString = person2.name!!)
        }
    
    dialog.show()
```
___
|LayoutManager|
|:------------------------------------------------:|
|PocketGridLayout|
|PocketLinearLayout|
|PocketStaggeredLayout|
* Usage: set to recyclerview.layoutManager
___
## TODO:
* cards template
___
Thanks to the author of external Library used:
* [org.jetbrains.kotlin:kotlin-stdlib-jdk7](https://github.com/JetBrains/kotlin/tree/master/libraries/stdlib)
* [com.android.support:appcompat-v7](https://developer.android.com/topic/libraries/support-library/)
* [com.android.support:design](https://developer.android.com/topic/libraries/support-library/)
* [com.android.support:recyclerview-v7](https://developer.android.com/topic/libraries/support-library/)
* [com.airbnb.android:lottie](https://github.com/airbnb/lottie-android)
* [me.yokeyword:swipebackfragment](https://github.com/YoKeyword/SwipeBackFragment)
