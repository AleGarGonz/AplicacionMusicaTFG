package com.example.aplicacionmusicatfg.modelos

import android.media.MediaPlayer
import java.io.File

class MediaPlayerManager {
    private var mediaPlayer: MediaPlayer? = null
    private var listCanciones: List<File>? = null
    private var cancionActualIndex: Int = -1
    private var cancionArchivo:File? = null

    fun playOrPause() {
        if (listCanciones != null) {
            val archivoActual = listCanciones!![cancionActualIndex]
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer()
                mediaPlayer!!.setDataSource(archivoActual.path)
                mediaPlayer!!.setOnPreparedListener { mp ->
                    mp.start()
                }
                mediaPlayer!!.prepare()
            } else {
                if (mediaPlayer!!.isPlaying) {
                    mediaPlayer!!.pause()
                } else {
                    mediaPlayer!!.start()
                }
            }
        }
    }
    //Para un unico archivo
    fun playOrPauseOneFile() {
            val archivoActual = cancionArchivo
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer()
                mediaPlayer!!.setDataSource(archivoActual?.path)
                mediaPlayer!!.setOnPreparedListener { mp ->
                    mp.start()
                }
                mediaPlayer!!.prepare()
            } else {
                if (mediaPlayer!!.isPlaying) {
                    mediaPlayer!!.pause()
                } else {
                    mediaPlayer!!.start()
                }
            }
    }

    fun stopAndReset() {
        mediaPlayer?.apply {
            pause()
            seekTo(0)
        }
    }

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun setListAndIndex(files: List<File>, index: Int) {
        listCanciones = files
        cancionActualIndex = index
    }

    //Para un unico archivo
    fun setFile(file:File?) {
        cancionArchivo=file
    }

    fun playNext() {
        listCanciones?.let { list ->
            if (list.size > 1) {
                cancionActualIndex = (cancionActualIndex + 1) % list.size
                mediaPlayer?.stop()
                release()
                setDataSourceAndPrepare(list[cancionActualIndex])
            }
        }
    }
    fun playPrevious() {
        listCanciones?.let { list ->
            if (list.size > 1) {
                var nuevoIndice = cancionActualIndex - 1
                if (nuevoIndice < 0) {
                    nuevoIndice = list.size - 1
                }
                cancionActualIndex = nuevoIndice
                mediaPlayer?.stop()
                release()
                setDataSourceAndPrepare(list[cancionActualIndex])
            }
        }
    }

    private fun setDataSourceAndPrepare(file: File) {
        mediaPlayer?.apply {
            reset()
            setDataSource(file.path)
            prepare()
        } ?: run {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(file.path)
                setOnPreparedListener { mp ->
                    mp.start()
                }
                prepare()
            }
        }
    }

    fun getCurrentPosition(): Int {
        return mediaPlayer?.currentPosition ?: 0
    }

    fun getDuration(): Int {
        return mediaPlayer?.duration ?: 0
    }

    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
    }
}