package com.example.habittracker.data.local

import androidx.annotation.DrawableRes
import com.example.habittracker.core.domain.model.HabitIcon

data class HabitIconResourceIds(
    @param:DrawableRes val run: Int,
    @param:DrawableRes val read: Int,
    @param:DrawableRes val water: Int,
    @param:DrawableRes val meditate: Int,
    @param:DrawableRes val sleep: Int,
    @param:DrawableRes val code: Int,
    @param:DrawableRes val music: Int,
    @param:DrawableRes val cook: Int,
    @param:DrawableRes val journal: Int,
    @param:DrawableRes val gym: Int,
    @param:DrawableRes val yoga: Int,
    @param:DrawableRes val walk: Int,
    @param:DrawableRes val cycle: Int,
    @param:DrawableRes val study: Int,
    @param:DrawableRes val noPhone: Int,
    @param:DrawableRes val vitamins: Int,
    @param:DrawableRes val language: Int,
    @param:DrawableRes val gratitude: Int,
    @param:DrawableRes val health: Int,
    @param:DrawableRes val organize: Int,
)

fun @receiver:DrawableRes Int.toHabitIcon(resources: HabitIconResourceIds): HabitIcon {
    return when (this) {
        resources.run -> HabitIcon.RUN
        resources.read -> HabitIcon.READ
        resources.water -> HabitIcon.WATER
        resources.meditate -> HabitIcon.MEDITATE
        resources.sleep -> HabitIcon.SLEEP
        resources.code -> HabitIcon.CODE
        resources.music -> HabitIcon.MUSIC
        resources.cook -> HabitIcon.COOK
        resources.journal -> HabitIcon.JOURNAL
        resources.gym -> HabitIcon.GYM
        resources.yoga -> HabitIcon.YOGA
        resources.walk -> HabitIcon.WALK
        resources.cycle -> HabitIcon.CYCLE
        resources.study -> HabitIcon.STUDY
        resources.noPhone -> HabitIcon.NO_PHONE
        resources.vitamins -> HabitIcon.VITAMINS
        resources.language -> HabitIcon.LANGUAGE
        resources.gratitude -> HabitIcon.GRATITUDE
        resources.health -> HabitIcon.HEALTH
        resources.organize -> HabitIcon.ORGANIZE
        else -> error("Unknown habit drawable resource ID: $this")
    }
}
