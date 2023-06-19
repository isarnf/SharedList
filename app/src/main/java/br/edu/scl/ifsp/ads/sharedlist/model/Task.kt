package br.edu.scl.ifsp.ads.sharedlist.model

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int? = -1,
    @NonNull var title: String = "",
    @NonNull var description: String = "",
    @NonNull var creationUser: String = "",
    @NonNull var creationDate: String = "",
    @NonNull var dueDate: String = "",
    @NonNull var isCompleted: Boolean = false,
    @NonNull var completionUser: String = "",
): Parcelable

