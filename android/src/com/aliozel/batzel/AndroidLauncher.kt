package com.aliozel.batzel

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout

import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView


class AndroidLauncher : AndroidApplication() {
    protected var adView: AdView? = null
    var layout: RelativeLayout? = null
    var gameView: View? = null
    var builder: AdRequest.Builder? = null
    var adParams: RelativeLayout.LayoutParams? = null
    var visiblity: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layout = RelativeLayout(this)
        val config = AndroidApplicationConfiguration()
        initialize(HomePage(),config)

        gameView = initializeForView(HomePage(),config)

        layout!!.addView(gameView)

        adView = AdView(this)

        adView!!.adListener = object : AdListener()
        {
            override fun onAdLoaded() {
                visiblity = adView!!.getVisibility();
				adView!!.setVisibility(AdView.GONE);
				adView!!.setVisibility(visiblity);
                //adView!!.loadAd(AdRequest.Builder().build())
            }
        }

        adView!!.adSize = AdSize.SMART_BANNER
        adView!!.adUnitId = "ca-app-pub-3940256099942544/6300978111"
        builder = AdRequest.Builder()

        adParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        )

        adParams!!.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        layout!!.addView(adView, adParams)
        adView!!.loadAd(builder!!.build())

        setContentView(layout)

    }
}