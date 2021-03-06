package com.flutter.android

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import io.flutter.embedding.android.FlutterFragment
import io.flutter.embedding.android.FlutterView
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.plugin.common.MethodChannel
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    var TAG = MainActivity::class.java.name

    lateinit var mFlutterEngine: FlutterEngine;
    lateinit var mFlutterView: FlutterView;
    lateinit var mFlutterChannel: MethodChannel;
    lateinit var mMethodCallHandler: MethodChannel.MethodCallHandler;
    var mFlutterConfig: FlutterConfig = FlutterConfig()
    lateinit var mFragmentManager: FragmentManager
    var mFlutterFragment: FlutterFragment? = null
    val FLUTTER_FRAGMENT = "FLUTTER_FRAGMENT"
    lateinit var context: Context;

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        context = this.application.applicationContext

        //Create Flutter Engine.
        mFlutterEngine = FlutterEngine(context)
        mFlutterEngine
            .dartExecutor
            .executeDartEntrypoint(
                DartExecutor.DartEntrypoint.createDefault()
            )
        //Add FlutterEngine to cache.
        FlutterEngineCache
            .getInstance()
            .put("FLUTTER_ENGINE", mFlutterEngine)
        //Create Flutter Method Channel.
        mFlutterChannel = MethodChannel(mFlutterEngine.dartExecutor, "app")
        //Create Flutter Fragment
        mFragmentManager = supportFragmentManager
        mFlutterFragment = mFragmentManager.findFragmentByTag(FLUTTER_FRAGMENT) as FlutterFragment?
        if (mFlutterFragment == null) {
            mFlutterFragment =
                FlutterFragment.withCachedEngine("FLUTTER_ENGINE").transparencyMode(FlutterView.TransparencyMode.transparent).build()
        }
        mMethodCallHandler = MethodChannel.MethodCallHandler { methodCall, result ->
            when (methodCall.method) {
                FlutterConstants.CHANNEL_METHOD_NAVIGATION -> {
                    val jsonObject = JSONObject(methodCall.arguments as String)
                    if (jsonObject.has("navigation")) {
                        when (jsonObject.get("navigation")) {
                            FlutterConstants.NAVIGATION_CLOSE -> {
                                if (mFragmentManager.findFragmentByTag(FLUTTER_FRAGMENT) != null) {
                                    mFragmentManager.beginTransaction().remove(mFragmentManager.findFragmentByTag(FLUTTER_FRAGMENT)!!).commit()
                                }
                            }
                        }
                    }
                    result.success(methodCall.method + methodCall.arguments)
                }
                FlutterConstants.CHANNEL_METHOD_EVENTS -> {
                    val jsonObject = JSONObject(methodCall.arguments as String)
                    if (jsonObject.has("event")) {
                        when (jsonObject.getString("event")) {
                            else -> {
                            }
                        }
                    }
                }
                else -> result.notImplemented()
            }
        }
        mFlutterChannel.setMethodCallHandler(mMethodCallHandler)

        button_1.setOnClickListener {
            mFlutterChannel.invokeMethod(
                FlutterConstants.CHANNEL_METHOD_CONFIG,
                "{\"flavor\": \"QA\", \"accessToken\": \"4247058463afbef1c1e1d0f52a4f5f51004f94ee7941cdd73c3cb6465600798a\"}",
                object : MethodChannel.Result {
                    override fun success(result: Any?) {
                        Log.d("setFlutterConfig", result.toString())
                    }

                    override fun error(
                        code: String,
                        message: String?,
                        details: Any?
                    ) {
                        Log.e("setFlutterConfig", message.toString())
                    }

                    override fun notImplemented() {
                        Log.e("setFlutterConfig", "Not Implemented")
                    }
                })
            mFlutterChannel.invokeMethod(
                FlutterConstants.CHANNEL_METHOD_DATA,
                "{\"post_id\": \"9112\"}",
                object : MethodChannel.Result {
                    override fun success(result: Any?) {
                        Log.d("setFlutterData", result.toString())
                    }

                    override fun error(
                        code: String,
                        message: String?,
                        details: Any?
                    ) {
                        Log.e("setFlutterData", message.toString())
                    }

                    override fun notImplemented() {
                        Log.e("setFlutterData", "Not Implemented")
                    }
                })
            mFlutterChannel.invokeMethod(
                FlutterConstants.CHANNEL_METHOD_PAGE,
                "PAGE_POST_THREAD",
                object : MethodChannel.Result {
                    override fun success(result: Any?) {
                        Log.d("Flutter Channel", result.toString())
                    }

                    override fun error(
                        code: String,
                        message: String?,
                        details: Any?
                    ) {
                        Log.e("Flutter Channel", message.toString())
                    }

                    override fun notImplemented() {
                        Log.e("Flutter Route", "Not Implemented")
                    }
                })
            //Open Flutter Fragment.
            mFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, mFlutterFragment as Fragment, FLUTTER_FRAGMENT)
                .commit();
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        //Close Flutter Fragment
        if (mFragmentManager.findFragmentByTag(FLUTTER_FRAGMENT) != null) {
            mFragmentManager.beginTransaction().remove(mFragmentManager.findFragmentByTag(FLUTTER_FRAGMENT)!!).commit()
            return
        }
        super.onBackPressed()
    }

    /**
     * FlutterView Initialization Methods.
     */

    //Add FlutterView to dialog view.
//            if (flutterView.parent != null) {
//                (flutterView.parent as ViewGroup).removeView(flutterView)
//            }
//
//            val dialog = AlertDialog.Builder(this)
//                .setView(flutterView)
//                .show()

    //Add FlutterView to activity layout hierarchy.
//            val layout = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
//            addContentView(flutterView, layout)

    //Add Flutter Fragment.
//            val tx = supportFragmentManager.beginTransaction()
//            val flutterFragment = Flutter.createFragment("page_transparent")
//            tx.replace(R.id.fragment_container, flutterFragment)
//            tx.commit()

    //Add FlutterView to ViewStub.
    //        if (flutterView.parent != null) {
//            (flutterView.parent as ViewGroup).removeView(flutterView)
//        }
//        view_flutter_container.addView(flutterView)

    /*
     * Flutter Wrapper
     */
//            mFlutterChannel.invokeMethod(
//                FlutterConstants.CHANNEL_METHOD_PAGE,
//                "PAGE_POST_THREAD",
//                object : Result {
//                    override fun success(result: Any?) {
//                        Log.d("Flutter Channel", result.toString())
//                    }
//
//                    override fun error(code: String, message: String?, details: Any?) {
//                        Log.e("Flutter Channel", message.toString())
//                    }
//
//                    override fun notImplemented() {
//                        Log.e("Flutter Channel", "Not Implemented")
//                    }
//                })
//            if (mFlutterView.parent != null) {
//                (mFlutterView.parent as ViewGroup).removeView(mFlutterView)
//            }
//            val frameLayoutParams: FrameLayout.LayoutParams
//            frameLayoutParams = FrameLayout.LayoutParams(
//                FrameLayout.LayoutParams.MATCH_PARENT,
//                FrameLayout.LayoutParams.MATCH_PARENT)
//            this.addContentView(mFlutterView, frameLayoutParams);
//            mFlutterWrapper.flutterConfig = Gson().toJson(mFlutterConfig)
//            val jsonObject = JSONObject()
//            jsonObject.put("post_id", 17932)
//            mFlutterWrapper.setFlutterData(jsonObject.toString())
//            mFlutterWrapper.initFlutterView(this, "PAGE_POST_THREAD")
//        if (mFlutterView.parent != null) {
//            (mFlutterView.parent as ViewGroup).removeView(mFlutterView)
//        }
//        mFlutterWrapper.hideFlutterView()
//        return

//    override fun onBackPressed() {
//        Log.d(TAG, "onBackPressed")
//        Log.d(TAG, "Flutter Back Status: ${mFlutterWrapper.flutterBackStatus}")
//
//        //Send back event to FlutterView if active and has routes that can be popped.
//        if (mFlutterWrapper.flutterBackStatus) {
//            mFlutterWrapper.popRoute()
//            return
//        }
//
//        //Hide FlutterView if visible. Usually means FlutterView cannot be popped and is at the top navigation level.
//        if (mFlutterWrapper.flutterViewVisible) {
//            mFlutterWrapper.hideFlutterView()
//            return
//        }
//
//        Log.d(TAG, "super.onBackPressed")
//        super.onBackPressed()
//    }

    /*
     * Flutter Wrapper v2
     */
//        mFlutterWrapper = FlutterWrapperV2().init(this@MainActivity)
//        setFlutterConfig()
//        mFlutterWrapper.methodCallHandler = MethodChannel.MethodCallHandler { methodCall, result ->
//            when (methodCall.method) {
//                FlutterConstants.CHANNEL_METHOD_NAVIGATION -> {
//                    val jsonObject = JSONObject(methodCall.arguments as String)
//                    if (jsonObject.has("navigation")) {
//                        when (jsonObject.get("navigation")) {
//                            FlutterConstants.NAVIGATION_CLOSE -> {
//                                mFlutterWrapper.hideFlutter()
//                            }
//                        }
//                    }
//                    result.success(methodCall.method + methodCall.arguments)
//                }
//                FlutterConstants.CHANNEL_METHOD_EVENTS -> {
//                    val jsonObject = JSONObject(methodCall.arguments as String)
//                    if (jsonObject.has("event")) {
//                        when (jsonObject.getString("event")) {
//                            else -> {
//                            }
//                        }
//                    }
//                }
//                else -> result.notImplemented()
//            }
//        }
//    private fun setFlutterConfig() {
//        mFlutterWrapper.flutterConfig = "{\"flavor\": \"QA\", \"accessToken\": \"4247058463afbef1c1e1d0f52a4f5f51004f94ee7941cdd73c3cb6465600798a\"}"
//    }
//        if (mFlutterWrapper.isFlutterVisible) {
//            mFlutterWrapper.sendBackEvent()
//            return
//        }
//            mFlutterWrapper.setFlutterData("{\"post_id\": \"9112\"}")
//            mFlutterWrapper.showFlutter(FlutterConstants.PAGE_POST_THREAD)
    /*
     * Flutter Embedding v2
     */
//        //Create Flutter Engine.
//        mFlutterEngine = FlutterEngine(context)
//        mFlutterEngine
//            .dartExecutor
//            .executeDartEntrypoint(
//                DartEntrypoint.createDefault()
//            )
//        //Add FlutterEngine to cache.
//        FlutterEngineCache
//            .getInstance()
//            .put("FLUTTER_ENGINE", mFlutterEngine)
//        //Create Flutter Method Channel.
//        mFlutterChannel = MethodChannel(mFlutterEngine.dartExecutor, "app")
//        //Create Flutter Fragment
//        mFragmentManager = supportFragmentManager
//        mFlutterFragment = mFragmentManager.findFragmentByTag(FLUTTER_FRAGMENT) as FlutterFragment?
//        if (mFlutterFragment == null) {
//            mFlutterFragment = FlutterFragment.withCachedEngine("FLUTTER_ENGINE").build()
//        }

    /*
     * Flutter Embedding v2 Fragment
     */
    //    lateinit var mFlutterEngine: FlutterEngine;
//    lateinit var mFlutterWrapper: FlutterWrapper
//    lateinit var mFlutterView: FlutterView;
//    lateinit var mFlutterChannel: MethodChannel;
//    lateinit var mMethodCallHandler: MethodChannel.MethodCallHandler;
//    var mFlutterConfig: FlutterConfig = FlutterConfig()
//    lateinit var mFragmentManager: FragmentManager
//    var mFlutterFragment: FlutterFragment? = null
//    val FLUTTER_FRAGMENT = "FLUTTER_FRAGMENT"
//        //Create Flutter Engine.
//        mFlutterEngine = FlutterEngine(context)
//        mFlutterEngine
//            .dartExecutor
//            .executeDartEntrypoint(
//                DartEntrypoint.createDefault()
//            )
//        //Add FlutterEngine to cache.
//        FlutterEngineCache
//            .getInstance()
//            .put("FLUTTER_ENGINE", mFlutterEngine)
//        //Create Flutter Method Channel.
//        mFlutterChannel = MethodChannel(mFlutterEngine.dartExecutor, "app")
//        //Create Flutter Fragment
//        mFragmentManager = supportFragmentManager
//        mFlutterFragment = mFragmentManager.findFragmentByTag(FLUTTER_FRAGMENT) as FlutterFragment?
//        if (mFlutterFragment == null) {
//            mFlutterFragment = FlutterFragment.createDefault()
//        }
//          //Open Flutter Fragment.
//            mFragmentManager
//                .beginTransaction()
//                .add(R.id.fragment_container, mFlutterFragment as Fragment, FLUTTER_FRAGMENT)
//                .commit();
//         //Close Flutter Fragment
//        if (mFragmentManager.findFragmentByTag(FLUTTER_FRAGMENT) != null) {
//            mFragmentManager.beginTransaction().remove(mFragmentManager.findFragmentByTag(FLUTTER_FRAGMENT)!!).commit()
//            return
//        }
}
