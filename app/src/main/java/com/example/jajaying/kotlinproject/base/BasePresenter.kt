package com.example.jajaying.kotlinproject.base

import android.icu.lang.UCharacter.GraphemeClusterBreak.V
import android.os.Build.VERSION_CODES.M
import java.lang.ref.WeakReference
import android.text.method.TextKeyListener.clear
import android.icu.lang.UCharacter.GraphemeClusterBreak.V
import android.os.Build.VERSION_CODES.M




/**
 * Created by jajaying on 2018/8/28.
 */
  open abstract class BasePresenter<M,V> {

    var mModel: M? = null

    private val mView: V? = null

    var mViewRef: WeakReference<V>? = null

    fun attachModelView(pModel: M, pView: V) {

        mViewRef = WeakReference(pView)

        this.mModel = pModel
    }


    fun getView(): V? {
        return if (isAttach()) {
            mViewRef!!.get()
        } else {
            null
        }
    }

    fun isAttach(): Boolean {
        return null != mViewRef && null != mViewRef!!.get()
    }


    fun onDettach() {
        if (null != mViewRef) {
            mViewRef!!.clear()
            mViewRef = null
        }
    }
}