package com.queue.utils

import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.nio.file.FileSystems

class AudioConcatenator {

    fun combined(window: Int, client: Int): ByteArray {
        val media = FileSystems.getDefault()
                .getPath("")
                .toAbsolutePath()
                .toString() + "/src/main/resources/static/audio/"

        var files: Array<String> = emptyArray()

        val clientFiles: Array<String> = getNames(client)
        val windowFiles: Array<String> = getNames(window)

        files += media + "client.ogg"

        for (file in clientFiles) {
            files += "$media$file.ogg"
        }

        files += media + "window.ogg"

        for (file in windowFiles) {
            files += "$media$file.ogg"
        }

        val outputStream = ByteArrayOutputStream()
        for (file in files) {
            val fis = FileInputStream(file)
            val buffer = ByteArray(4096)
            var bytesRead: Int
            while (fis.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }
            fis.close()
        }
        val combinedBytes = outputStream.toByteArray()
        outputStream.close()

        return combinedBytes
    }

    private fun getNames(value: Int): Array<String> {
        var files: Array<String> = emptyArray()

        if (value in 10..19) {
            files += value.toString()

            return files
        }

        var array: Array<Int> = emptyArray()

        array += value / 1000
        array += value / 100
        array += (value / 10) % 10
        array += value % 10

        if ((array[0] * 1000) > 0) {
            files += (array[0] * 1000).toString()
        }

        if ((array[1] * 100) > 0) {
            files += (array[1] * 100).toString()
        }

        if ((array[2] * 10) > 0) {
            files += (array[2] * 10).toString()
        }

        if ((array[3] * 1) > 0) {
            files += (array[3] * 1).toString()
        }

        return files
    }
}

