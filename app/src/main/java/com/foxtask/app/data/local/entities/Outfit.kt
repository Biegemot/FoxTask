package com.foxtask.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "outfit")
data class Outfit(
    @PrimaryKey val userId: Int = 1,
    val hatItemId: Int? = null,
    val glassesItemId: Int? = null,
    val maskItemId: Int? = null,
    val scarfItemId: Int? = null,
    val bandanaItemId: Int? = null,
    val cloakItemId: Int? = null,
    val furColorItemId: Int? = null,
    val backgroundThemeId: Int? = null,
    val maoriPatternItemId: Int? = null
)
