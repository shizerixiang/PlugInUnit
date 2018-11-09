package com.beviswang.appplugin.proxy

import android.os.Bundle
import android.widget.Toast
import com.beviswang.appplugin.R
import com.beviswang.appplugin.getResourcesView
import com.beviswang.pluginlib.BaseProxyActivity
import com.beviswang.pluginlib.IProxyManager
import kotlinx.android.synthetic.main.activity_next_proxy.*

class NextProxyManager : IProxyManager {
    private lateinit var mProxyActivity: BaseProxyActivity

    override fun setProxyActivity(activity: BaseProxyActivity) {
        mProxyActivity = activity
    }

    override fun onProxyCreated(savedInstanceState: Bundle?) {
        mProxyActivity.setContentView(getResourcesView(R.layout.activity_next_proxy, mProxyActivity))
        mProxyActivity.mTxt.setOnClickListener {
            Toast.makeText(mProxyActivity, "你碰到了我哦！変態！！！", Toast.LENGTH_LONG).show()
        }
    }
}