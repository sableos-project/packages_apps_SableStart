package org.sableos.start.model

import android.content.ComponentName
import android.graphics.Bitmap
import android.os.UserHandle

data class AppEntry(
    val label: String,
    val component: ComponentName,
    val user: UserHandle,
    val profileSerial: Long,
    val icon: Bitmap?,
) {
    val stableId: String
        get() = "$profileSerial|${component.flattenToString()}"
}
