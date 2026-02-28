package com.ledger.app.utils.common

import android.content.Context
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object FileUtils {
    private const val DATE_TIME_FORMAT = "yyyyMMdd_HHmmss"

    fun getExportDir(context: Context): File {
        val exportDir = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), Constants.EXPORT_DIR)
        if (!exportDir.exists()) {
            exportDir.mkdirs()
        }
        return exportDir
    }

    fun getBackupDir(context: Context): File {
        val backupDir = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), Constants.BACKUP_DIR)
        if (!backupDir.exists()) {
            backupDir.mkdirs()
        }
        return backupDir
    }

    fun createExportFile(context: Context, extension: String): File {
        val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT))
        val fileName = "ledger_export_$timestamp.$extension"
        return File(getExportDir(context), fileName)
    }

    fun createBackupFile(context: Context): File {
        val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT))
        val fileName = "ledger_backup_$timestamp.json"
        return File(getBackupDir(context), fileName)
    }

    fun writeToFile(file: File, content: String): Boolean {
        return try {
            FileOutputStream(file).use {outputStream ->
                outputStream.write(content.toByteArray())
                true
            }
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    fun readFromFile(file: File): String? {
        return try {
            file.readText()
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun getFileList(dir: File): List<File> {
        return dir.listFiles()?.toList() ?: emptyList()
    }

    fun deleteFile(file: File): Boolean {
        return if (file.exists()) {
            file.delete()
        } else {
            false
        }
    }
}
