package com.example.weatherapplication.workers

import android.content.Context
import android.graphics.PixelFormat
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.example.weatherapplication.R
import com.example.weatherapplication.databinding.AlertDialogBinding

class AlertWindow(private val context: Context, private val description: String, private val title: String) {
    lateinit var binding: AlertDialogBinding
    private lateinit var customDialog: View
    private var mediaPlayer: MediaPlayer =  MediaPlayer.create(context, R.raw.weatheralert)


    fun onCreate() {
        mediaPlayer.start()// no need to call prepare(); create() does that for you
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        customDialog = inflater.inflate(R.layout.alert_dialog, null)
        binding = AlertDialogBinding.bind(customDialog)
        initView()
        val LAYOUT_FLAG: Int = Flag()
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val layoutParams: WindowManager.LayoutParams = Params(LAYOUT_FLAG)
        windowManager.addView(customDialog, layoutParams)

    }

    private fun Flag(): Int {
        val LAYOUT_FLAG: Int
        LAYOUT_FLAG = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }
        return LAYOUT_FLAG
    }

    private fun Params(LAYOUT_FLAG: Int): WindowManager.LayoutParams {
        val width = (context.resources.displayMetrics.widthPixels * 0.85).toInt()
        return WindowManager.LayoutParams(
            width,
            WindowManager.LayoutParams.WRAP_CONTENT,
            LAYOUT_FLAG,
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or WindowManager.LayoutParams.FLAG_LOCAL_FOCUS_MODE,
            PixelFormat.TRANSLUCENT
        )
    }

    private fun initView() {
        // binding.img.setImageResource(R.drawable.ic_broken_cloud)
        binding.dialogMsg.text = description
        binding.dialogSubTitle.text=title
        binding.dismissAlertBtn.setOnClickListener {
            close()
        }
    }

    private fun close() {
        try {
            (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).removeView(
                customDialog
            )
            customDialog.invalidate()
            (customDialog.parent as ViewGroup).removeAllViews()
        } catch (e: Exception) {
            Log.d("Error", e.toString())
        }
        mediaPlayer.release()

    }


}