package me.shouheng.icamerasample

import android.app.Application
import me.shouheng.icamera.config.ConfigurationProvider
import me.shouheng.icamerasample.activity.MainActivity
import me.shouheng.uix.common.bean.TextStyleBean
import me.shouheng.uix.pages.CrashReportActivity
import me.shouheng.utils.app.ResUtils
import me.shouheng.utils.permission.Permission
import me.shouheng.utils.permission.PermissionUtils
import me.shouheng.utils.stability.CrashHelper
import me.shouheng.utils.stability.L
import me.shouheng.utils.store.PathUtils
import me.shouheng.vmlib.VMLib
import java.io.File

/**
 * App
 *
 * @author <a href="mailto:shouheng2015@gmail.com">WngShhng</a>
 * @version 2019-12-28 15:47
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        application = this
        // initialize the vmlib
        VMLib.onCreate(this)
        // log
        L.getConfig().setLogSwitch(BuildConfig.DEBUG)
        configCrashHelper(application)
        // set iCamera log switch
        ConfigurationProvider.get().isDebug = BuildConfig.DEBUG
    }

    companion object {
        private lateinit var application: Application
        fun app(): Application = application
        fun configCrashHelper(application: Application) {
            // 配置崩溃工具，文件存储在：data/data/package_name/files/crash 下面
            if (PermissionUtils.hasPermissions(Permission.STORAGE)) {
                CrashHelper.init(application,
                    File(PathUtils.getExternalAppFilesPath(), "crash")
                ) { crashInfo, _ ->
                    CrashReportActivity.Companion.Builder(application)
                        .setRestartActivity(MainActivity::class.java)
                        .setMessage(crashInfo)
                        .setImage(R.drawable.uix_crash_error_image)
                        .setTitle(ResUtils.getString(R.string.main_common_crash_happened))
                        .setButtonStyle(TextStyleBean().apply {
                            textSize = 18f
                        })
                        .launch()
                }
            }
        }
    }
}