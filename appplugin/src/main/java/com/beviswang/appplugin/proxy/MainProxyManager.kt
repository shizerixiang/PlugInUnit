package com.beviswang.appplugin.proxy

import android.os.Bundle
import android.widget.Toast
import com.beviswang.appplugin.R
import com.beviswang.appplugin.getResourcesView
import com.beviswang.appplugin.pResources
import com.beviswang.pluginlib.BaseProxyActivity
import com.beviswang.pluginlib.IProxyManager
import kotlinx.android.synthetic.main.activity_main_proxy.*

class MainProxyManager : IProxyManager {
    private lateinit var mProxyActivity: BaseProxyActivity

    override fun setProxyActivity(activity: BaseProxyActivity) {
        mProxyActivity = activity
    }

    override fun onProxyCreated(savedInstanceState: Bundle?) {
        mProxyActivity.setContentView(getResourcesView(R.layout.activity_main_proxy, mProxyActivity))
        mProxyActivity.mImg.setImageDrawable(pResources.getDrawable(R.drawable.cover, null))
        mProxyActivity.mImg.setOnClickListener {
            Toast.makeText(mProxyActivity, "被点击了！", Toast.LENGTH_LONG).show()
        }
        mProxyActivity.mTxt.setOnClickListener {
            mProxyActivity.startPluginActivity("com.beviswang.appplugin.proxy.NextProxyManager")
        }
    }
}