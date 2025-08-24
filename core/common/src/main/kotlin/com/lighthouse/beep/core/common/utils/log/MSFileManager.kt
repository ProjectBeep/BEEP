package com.lighthouse.beep.core.common.utils.log

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.os.Build
import java.io.File
import java.io.FileOutputStream

class MSFileManager(
    private val log: MSLogger,
) {

    companion object {
        private const val ROOT_TEMP_DIR = "/storage/emulated/0/DCIM/Camera/tmp/"
    }

    private val rootTempDir = File(ROOT_TEMP_DIR)

    private fun getRootTempDir(): File {
        if (!rootTempDir.isDirectory) {
            rootTempDir.mkdirs()
        }

        return rootTempDir
    }
    
    private fun createNoMediaFile(dir: File) {
        try {
            val noMediaFile = File(dir, ".nomedia")
            if (!noMediaFile.exists()) {
                noMediaFile.createNewFile()
                log.d("createNoMediaFile success: ${noMediaFile.absolutePath}")
            }
        } catch (e: Exception) {
            log.e("createNoMediaFile failed: ${e.message}")
        }
    }

    fun saveBitmap(
        bitmap: Bitmap?,
        fileName: String,
        format: CompressFormat
    ) {
        if (bitmap == null) {
            log.e("saveBitmap bitmap is Null!")
            return
        }
        val ext = getFormatExt(format)
        if (ext == null) {
            log.e("saveBitmap invalid format")
            return
        }
        try {
            val dir = getRootTempDir()
            if (!dir.exists()) {
                log.e("saveBitmap directory creation failed")
                return
            }
            createNoMediaFile(dir)

            val outFileName = "${dir.canonicalPath}/${fileName}.${ext}"
            val out = FileOutputStream(outFileName)
            bitmap.compress(format, 100, out)
            out.close()
            log.d("saveBitmap success : $outFileName")
        } catch (e: Exception) {
            log.e("saveBitmap failed : ${e.message}")
        }
    }

    private fun getFormatExt(format: CompressFormat): String? {
        return when (format) {
            CompressFormat.JPEG -> "jpg"
            CompressFormat.PNG -> "png"
            CompressFormat.WEBP -> "webp"
            else -> when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && format == CompressFormat.WEBP_LOSSY -> "webp"
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && format == CompressFormat.WEBP_LOSSLESS -> "webp"
                else -> null
            }
        }
    }
}