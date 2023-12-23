package com.lighthouse.beep.core.common.utils.file

import android.content.Context
import java.io.File
import java.io.IOException
import java.util.UUID

object BeepFileUtils {

    fun createTempFile(
        context: Context,
        fileName: String = UUID.randomUUID().toString(),
    ): File {
        return File(context.cacheDir, fileName).apply {
            createNewFile()
        }
    }

    fun clearTemp(context: Context): Boolean {
        return context.cacheDir.deleteRecursively()
    }

    fun clearDir(dir: File) {
        if (dir.isDirectory) {
            dir.deleteRecursively()
        }
    }

    fun createFile(
        context: Context,
        dir: BeepDir,
        name: String = UUID.randomUUID().toString(),
    ): File {
        val path = "${context.filesDir}${File.separator}${dir.dir}${File.separator}$name"
        val file = File(path)
        val parentDir = file.parentFile
        if (parentDir?.exists() == false) {
            val created = parentDir.mkdirs()
            if (!created) {
                throw IOException("파일 생성에 실패 했습니다.")
            }
        }
        return file.apply {
            createNewFile()
        }
    }
}
