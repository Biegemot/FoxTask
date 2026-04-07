package com.foxtask.app.data.models

data class FoxOutfit(
    val hatItemId: Int? = null,
    val glassesItemId: Int? = null,
    val maskItemId: Int? = null,
    val scarfItemId: Int? = null,
    val bandanaItemId: Int? = null,
    val cloakItemId: Int? = null,
    val furColorItemId: Int? = null,
    val backgroundThemeId: Int? = null,
    val maoriPatternItemId: Int? = null
) {
    fun getEquippedItemsCount(): Int = listOf(
        hatItemId, glassesItemId, maskItemId, scarfItemId,
        bandanaItemId, cloakItemId, furColorItemId, backgroundThemeId, maoriPatternItemId
    ).count { it != null }
}
