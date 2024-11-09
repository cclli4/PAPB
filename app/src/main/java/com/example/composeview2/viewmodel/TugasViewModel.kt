package com.example.composeview2.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.composeview2.local.Tugas
import com.example.composeview2.local.TugasRepository
import kotlinx.coroutines.launch

class TugasViewModel(application: Application) : AndroidViewModel(application) {

    private val tugasRepository: TugasRepository = TugasRepository(application)
    val allTugas: LiveData<List<Tugas>> = tugasRepository.tugasList

    fun addTugas(tugas: Tugas) {
        viewModelScope.launch {
            tugasRepository.insertTugas(tugas)
        }
    }

    fun deleteTugas(tugas: Tugas) {
        viewModelScope.launch {
            tugasRepository.deleteTugas(tugas)
        }
    }

    fun toggleCompletion(tugas: Tugas) {
        viewModelScope.launch {
            val updatedTugas = tugas.copy(completed = !tugas.completed)
            tugasRepository.updateTugas(updatedTugas)
        }
    }
}
