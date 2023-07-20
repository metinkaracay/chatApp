package com.example.learnandroidproject.common.extensions

import android.content.Context
import android.media.MediaMetadataRetriever
import android.media.MediaMetadataRetriever.METADATA_KEY_DURATION
import android.util.Base64
import com.example.learnandroidproject.common.tryOrNull
import java.io.File

fun File.toBase64(): String? = tryOrNull { Base64.encodeToString(this.readBytes(), Base64.NO_WRAP) }

val File.uri get() = this.absolutePath.asUri()

fun File.getMediaDuration(context: Context): Long {
    if (!exists()) return 0
    val retriever = MediaMetadataRetriever()
    retriever.setDataSource(context, uri)
    val duration = retriever.extractMetadata(METADATA_KEY_DURATION)
    retriever.release()

    return duration?.toLongOrNull() ?: 0
}