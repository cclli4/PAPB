package com.example.composeview2.local

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "tugas_table")
@Parcelize
data class Tugas(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "namaMatkul")
    val namaMatkul: String,

    @ColumnInfo(name = "detailTugas")
    val detailTugas: String,

    @ColumnInfo(name = "completed")
    var completed: Boolean = false,

    val image: Bitmap? = null
) : Parcelable
