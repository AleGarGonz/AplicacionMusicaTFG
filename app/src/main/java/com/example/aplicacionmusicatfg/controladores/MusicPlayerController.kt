package com.example.aplicacionmusicatfg.controladores

import com.example.aplicacionmusicatfg.modelos.MediaPlayerManager
import java.io.File

class MusicPlayerController {
    private val mediaPlayerManager = MediaPlayerManager()

    fun playOrPause() {
        mediaPlayerManager.playOrPause()
    }
    //Para un unico archivo
    fun playOrPauseOneFile() {
        mediaPlayerManager.playOrPauseOneFile()
    }

    fun stopAndReset() {
        mediaPlayerManager.stopAndReset()
    }

    fun playNext() {
        mediaPlayerManager.playNext()
    }

    fun playPrevious() {
        mediaPlayerManager.playPrevious()
    }

    fun release() {
        mediaPlayerManager.release()
    }

    fun setListAndIndex(files: List<File>, index: Int) {
        mediaPlayerManager.setListAndIndex(files, index)
    }
    //Para un unico archivo
    fun setFile(file:File?) {
        mediaPlayerManager.setFile(file)
    }

    fun getCurrentPosition(): Int {
        return mediaPlayerManager.getCurrentPosition()
    }
    fun getDuration(): Int {
        return mediaPlayerManager.getDuration()
    }

    fun seekTo(position: Int) {
        mediaPlayerManager.seekTo(position)
    }
}