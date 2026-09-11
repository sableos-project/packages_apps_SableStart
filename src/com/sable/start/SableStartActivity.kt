package org.sableos.start

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import org.sableos.start.bridge.SableStartNative
import org.sableos.start.platform.LauncherAppsRepository
import org.sableos.start.ui.SableStartRoot

class SableStartActivity : ComponentActivity() {

    private var nativeHandle: Long = 0

    override fun onCreate(
        savedInstanceState: Bundle?,
    ) {
        super.onCreate(savedInstanceState)

        nativeHandle =
            SableStartNative.nativeCreate()

        val repository =
            LauncherAppsRepository(this)

        setContent {
            SableStartRoot(
                repository = repository,
            )
        }
    }

    override fun onDestroy() {
        if (nativeHandle != 0L) {
            SableStartNative.nativeDestroy(
                nativeHandle,
            )

            nativeHandle = 0
        }

        super.onDestroy()
    }
}
