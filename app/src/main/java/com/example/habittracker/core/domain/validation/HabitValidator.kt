package com.example.habittracker.core.domain.validation

import com.example.habittracker.core.domain.error.HabitError
import com.example.habittracker.core.domain.model.Habit
import com.example.habittracker.core.domain.model.HabitDraft
import com.example.habittracker.core.domain.model.HabitIconId
import com.example.habittracker.core.domain.model.HabitUpdate
import com.example.habittracker.core.domain.model.HabitIcons
import java.time.LocalDate

class HabitValidator {
    fun normalizeName(name: String): String = name.trim()

    fun validateDraft(draft: HabitDraft, today: LocalDate): HabitError? {
        val invalidName = validateName(draft.name)
        val invalidIcon = validateIcon(draft.iconId)
        return when {
            invalidName != null -> invalidName
            invalidIcon != null -> invalidIcon
            draft.creationDate.isAfter(today) -> HabitError.CreationDateInFuture
            else -> null
        }
    }

    fun validateUpdate(update: HabitUpdate): HabitError? {
        val invalidName = validateName(update.name)
        val invalidIcon = validateIcon(update.iconId)
        return when {
            invalidName != null -> invalidName
            invalidIcon != null -> invalidIcon
            else -> null
        }
    }

    fun validateCompletion(habit: Habit, date: LocalDate, today: LocalDate): HabitError? {
        return when {
            date.isBefore(habit.creationDate) ->
                HabitError.CompletionDateInvalid(
                    HabitError.CompletionDateInvalid.CompletionDateReason.BEFORE_CREATION,
                )
            date.isAfter(today) ->
                HabitError.CompletionDateInvalid(
                    HabitError.CompletionDateInvalid.CompletionDateReason.IN_THE_FUTURE,
                )
            !habit.weekdayMask.isScheduled(date) ->
                HabitError.CompletionDateInvalid(
                    HabitError.CompletionDateInvalid.CompletionDateReason.NOT_SCHEDULED,
                )
            else -> null
        }
    }

    private fun validateName(name: String): HabitError? {
        return when {
            name.isBlank() -> HabitError.InvalidName(HabitError.InvalidName.Reason.BLANK)
            name != name.trim() -> HabitError.InvalidName(HabitError.InvalidName.Reason.NOT_TRIMMED)
            name.codePointCount(0, name.length) > 50 ->
                HabitError.InvalidName(HabitError.InvalidName.Reason.TOO_LONG)
            else -> null
        }
    }

    private fun validateIcon(iconId: HabitIconId): HabitError? {
        return if (HabitIcons.isKnown(iconId)) null else HabitError.InvalidIcon(iconId.value)
    }
}
