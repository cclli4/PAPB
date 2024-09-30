package com.example.composeview2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import com.example.composeview2.ui.theme.COMPOSEVIEW2Theme

class ListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            COMPOSEVIEW2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DataListScreen()
                }
            }
        }
    }
}

@Composable
fun DataListScreen() {
    val db = FirebaseFirestore.getInstance()
    var dataList by remember { mutableStateOf(listOf<JadwalKuliahModel>()) }

    LaunchedEffect(Unit) {
        db.collection("jadwal-kuliah")
            .get()
            .addOnSuccessListener { result ->
                val items = result.documents.map { document ->
                    JadwalKuliahModel(
                        hari = document.getString("hari") ?: "",
                        jamMulai = document.getString("jam mulai") ?: "",
                        jamSelesai = document.getString("jam selesai") ?: "",
                        matkul = document.getString("matkul") ?: "",
                        ruang = document.getString("ruang") ?: ""
                    )
                }
                dataList = items
            }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(dataList) { data ->
            JadwalKuliahCard(data)
        }
    }
}

@Composable
fun JadwalKuliahCard(data: JadwalKuliahModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("hari: ${data.hari}", style = MaterialTheme.typography.bodyMedium)
            Text("jam mulai: ${data.jamMulai}", style = MaterialTheme.typography.bodyMedium)
            Text("jam selesai: ${data.jamSelesai}", style = MaterialTheme.typography.bodyMedium)
            Text("mata kuliah: ${data.matkul}", style = MaterialTheme.typography.bodyMedium)
            Text("ruang: ${data.ruang}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

// Data Model class to handle the updated fields
data class JadwalKuliahModel(
    val hari: String,
    val jamMulai: String,
    val jamSelesai: String,
    val matkul: String,
    val ruang: String
)
