package com.graduation.red.base

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp
import java.lang.Exception

abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {

    lateinit var binding: T

    fun isBindingInitialized() = ::binding.isInitialized

    @get:LayoutRes
    protected abstract val layoutRes: Int

    @get:StringRes
    protected open val okString: Int? = null
    protected open val yesString: Int? = null
    protected open val noString:  Int? = null

    protected abstract fun initUI(savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setActivityContentView(this)
        initUI(savedInstanceState)
    }

    open fun setActivityContentView(activity  : AppCompatActivity) {
        binding = DataBindingUtil.setContentView(this , layoutRes)
        binding.executePendingBindings()
    }

    protected fun inflateLayout(layout: Int): View? {
        return layoutInflater.inflate(
            layout,
            null,
            false
        )
    }


    fun hideKeyboard() {
        KeyboardUtils.hideKeyboard(this)
    }

    fun hideKeyboardOutSide(view: View) {
        KeyboardUtils.hideKeyBoardWhenClickOutSide(view, this)
    }

    fun hideKeyboardOutSideText(view: View) {
        KeyboardUtils.hideKeyBoardWhenClickOutSideText(view, this)
    }


    fun <VH : RecyclerView.ViewHolder> setUpRcv(
        rcv: RecyclerView,
        adapter: RecyclerView.Adapter<VH>
    ) {
        rcv.setHasFixedSize(true)
        rcv.layoutManager = LinearLayoutManager(this)
        rcv.adapter = adapter
    }

    fun <VH : RecyclerView.ViewHolder> setUpRcv(
        rcv: RecyclerView, adapter:
        RecyclerView.Adapter<VH>,
        isHasFixedSize: Boolean,
        isNestedScrollingEnabled: Boolean
    ) {
        rcv.setHasFixedSize(isHasFixedSize)
        rcv.layoutManager = LinearLayoutManager(this)
        rcv.adapter = adapter
        rcv.isNestedScrollingEnabled = isNestedScrollingEnabled
    }

    fun <VH : RecyclerView.ViewHolder> setUpRcv(
        rcv: RecyclerView,
        adapter: RecyclerView.Adapter<VH>,
        isNestedScrollingEnabled: Boolean
    ) {
        rcv.setHasFixedSize(true)
        rcv.layoutManager = LinearLayoutManager(this)
        rcv.adapter = adapter
        rcv.isNestedScrollingEnabled = isNestedScrollingEnabled
    }

    open fun clearAllBackStack() {
        val fm = supportFragmentManager
        for (i in 0 until fm.backStackEntryCount) {
            fm.popBackStack()
        }
    }


    open fun addFragment(fragment: Fragment, id: Int, addToBackStack: Boolean) {

        if (supportFragmentManager.findFragmentByTag(fragment.javaClass.simpleName) != null)
            return
        addFragment(supportFragmentManager, fragment, id, addToBackStack)
    }


    //Add Fragment by fragmentManager
    open fun addFragment(
        fragmentManager: FragmentManager,
        fragment: Fragment,
        id: Int,
        addToBackStack: Boolean
    ) {

        if (supportFragmentManager.findFragmentByTag(fragment.javaClass.simpleName) != null)
            return

        val transaction = fragmentManager.beginTransaction()

        transaction.add(id, fragment, fragment.javaClass.simpleName)

        if (addToBackStack)
            transaction.addToBackStack(fragment.javaClass.simpleName)

        transaction.commit()

    }

    open fun replaceFragment(fragment: Fragment, id: Int, addToBackStack: Boolean) {
        if (supportFragmentManager.findFragmentByTag(fragment.javaClass.simpleName) != null)
            return
        replaceFragment(supportFragmentManager, fragment, id, addToBackStack)
    }

    open fun replaceFragment(
        fragmentManager: FragmentManager,
        fragment: Fragment,
        id: Int,
        addToBackStack: Boolean
    ) {

        if (supportFragmentManager.findFragmentByTag(fragment.javaClass.simpleName) != null)
            return

        val transaction = fragmentManager.beginTransaction()


        if (addToBackStack)
            transaction.addToBackStack(fragment.javaClass.canonicalName)

        transaction.replace(id, fragment, fragment.javaClass.canonicalName)
        transaction.commit()

    }

}