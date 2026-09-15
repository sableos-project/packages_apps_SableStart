package org.sableos.start

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.LauncherApps
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.os.UserHandle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import org.sableos.start.bridge.SableStartNative
import org.sableos.start.model.AppEntry
import org.sableos.start.platform.LauncherAppsRepository
import org.sableos.start.platform.LauncherStateStore
import org.sableos.start.platform.LiveSurfaceRepository
import org.sableos.start.ui.SableStartRoot

class SableStartActivity : ComponentActivity() {
    private var nativeHandle: Long = 0

    private lateinit var launcherAppsRepository: LauncherAppsRepository
    private lateinit var launcherStateStore: LauncherStateStore
    private lateinit var liveSurfaceRepository: LiveSurfaceRepository

    private val appsState =
        mutableStateOf<List<AppEntry>>(emptyList())

    private val pinnedState =
        mutableStateOf<List<AppEntry>>(emptyList())

    private val recentState =
        mutableStateOf<List<AppEntry>>(emptyList())

    private val liveRefreshGeneration =
        mutableStateOf(0)

    private var callbackRegistered = false

    private val launcherCallback =
        object : LauncherApps.Callback() {
            override fun onPackageRemoved(
                packageName: String,
                user: UserHandle,
            ) {
                scheduleInventoryRefresh()
            }

            override fun onPackageAdded(
                packageName: String,
                user: UserHandle,
            ) {
                scheduleInventoryRefresh()
            }

            override fun onPackageChanged(
                packageName: String,
                user: UserHandle,
            ) {
                scheduleInventoryRefresh()
            }

            override fun onPackagesAvailable(
                vararg packageNames: String,
                user: UserHandle,
                replacing: Boolean,
            ) {
                scheduleInventoryRefresh()
            }

            override fun onPackagesUnavailable(
                packageNames: Array<String>,
                user: UserHandle,
                replacing: Boolean,
            ) {
                scheduleInventoryRefresh()
            }

            override fun onPackagesSuspended(
                vararg packageNames: String,
                user: UserHandle,
            ) {
                scheduleInventoryRefresh()
            }

            override fun onPackagesUnsuspended(
                vararg packageNames: String,
                user: UserHandle,
            ) {
                scheduleInventoryRefresh()
            }
        }

    override fun onCreate(
        savedInstanceState: Bundle?,
    ) {
        super.onCreate(savedInstanceState)

        nativeHandle =
            SableStartNative.nativeCreate()

        launcherAppsRepository =
            LauncherAppsRepository(this)
        launcherStateStore =
            LauncherStateStore(this)
        liveSurfaceRepository =
            LiveSurfaceRepository(this)

        refreshInventory()

        setContent {
            SableStartRoot(
                apps = appsState.value,
                pinnedApps = pinnedState.value,
                recentApps = recentState.value,
                liveSurfaceRepository = liveSurfaceRepository,
                liveRefreshGeneration = liveRefreshGeneration.value,
                onLaunchApp = ::launchApp,
                onTogglePinned = ::togglePinned,
                onOpenAppInfo = ::openAppInfo,
                onRequestLivePermissions = ::requestLivePermissions,
            )
        }
    }

    override fun onStart() {
        super.onStart()

        if (!callbackRegistered) {
            launcherAppsRepository.registerCallback(launcherCallback)
            callbackRegistered = true
        }

        refreshInventory()
    }

    override fun onResume() {
        super.onResume()
        refreshInventory()
        liveRefreshGeneration.value += 1
    }

    override fun onStop() {
        if (callbackRegistered) {
            launcherAppsRepository.unregisterCallback(launcherCallback)
            callbackRegistered = false
        }

        super.onStop()
    }

    override fun onDestroy() {
        if (callbackRegistered) {
            launcherAppsRepository.unregisterCallback(launcherCallback)
            callbackRegistered = false
        }

        if (nativeHandle != 0L) {
            SableStartNative.nativeDestroy(
                nativeHandle,
            )

            nativeHandle = 0
        }

        super.onDestroy()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults,
        )

        if (requestCode == LIVE_PERMISSION_REQUEST_CODE) {
            liveRefreshGeneration.value += 1
        }
    }

    private fun launchApp(entry: AppEntry) {
        if (launcherAppsRepository.launch(entry)) {
            launcherStateStore.recordSuccessfulLaunch(entry)
            refreshLocalLauncherState()
        } else {
            refreshInventory()
        }
    }

    private fun togglePinned(entry: AppEntry) {
        launcherStateStore.togglePinned(entry)
        refreshLocalLauncherState()
    }

    private fun openAppInfo(entry: AppEntry) {
        if (entry.user != Process.myUserHandle()) {
            return
        }

        try {
            startActivity(
                Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:${entry.component.packageName}"),
                ),
            )
        } catch (_: ActivityNotFoundException) {
            // Fail closed. The system owns application-management UI.
        } catch (_: SecurityException) {
            // Fail closed for profile/package combinations Android does not expose.
        }
    }

    private fun requestLivePermissions() {
        val permissions =
            buildList {
                add(Manifest.permission.READ_CALENDAR)
                if (Build.VERSION.SDK_INT >= 33) {
                    add(Manifest.permission.READ_MEDIA_IMAGES)
                    add(Manifest.permission.READ_MEDIA_AUDIO)
                } else {
                    add(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }

        requestPermissions(
            permissions.toTypedArray(),
            LIVE_PERMISSION_REQUEST_CODE,
        )
    }

    private fun scheduleInventoryRefresh() {
        runOnUiThread {
            refreshInventory()
        }
    }

    private fun refreshInventory() {
        appsState.value =
            launcherAppsRepository.loadApps()
        refreshLocalLauncherState()
    }

    private fun refreshLocalLauncherState() {
        val apps = appsState.value
        pinnedState.value =
            launcherStateStore.resolvePinned(apps)
        recentState.value =
            launcherStateStore.resolveRecent(apps)
    }

    private companion object {
        const val LIVE_PERMISSION_REQUEST_CODE = 0x534D
    }
}
