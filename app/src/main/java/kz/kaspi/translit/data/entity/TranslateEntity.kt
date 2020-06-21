package kz.kaspi.translit.data.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

// 1 step create entity - model
@Entity(tableName = "translate_table")
@Parcelize
data class TranslateEntity(
    @PrimaryKey(autoGenerate = true) val id: Long?,
    @ColumnInfo(name = "cyrillic") val cyrillic: String,
    @ColumnInfo(name = "latin") val latin: String
) : Parcelable