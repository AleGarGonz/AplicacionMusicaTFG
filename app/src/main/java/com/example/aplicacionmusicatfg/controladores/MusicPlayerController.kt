package com.example.aplicacionmusicatfg.controladores

import androidx.lifecycle.ViewModel
import com.example.aplicacionmusicatfg.modelos.MediaPlayerManager
import java.io.File

class MusicPlayerController: ViewModel() {
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
        if(validarAudioLista(files)){
            mediaPlayerManager.setListAndIndex(files, index)
        }
    }
    //Para un unico archivo
    fun setFile(file:File?) {
        if(validarAudio(file)){
            mediaPlayerManager.setFile(file)
        }
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
    private fun validarAudio(AudioFile: File?): Boolean {
        return AudioFile != null &&
                AudioFile.isFile &&
                AudioFile.exists() &&
                AudioFile.length() > 0L
    }
    private fun validarAudioLista(listaArchivos: List<File?>): Boolean {
        return listaArchivos.all {
            it != null &&
                    it.isFile &&
                    it.exists() &&
                    it.length() > 0L
        }
    }
}