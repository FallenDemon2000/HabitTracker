package com.example.habittracker.core.domain.validation

import com.example.habittracker.core.domain.model.HabitError
import com.example.habittracker.core.domain.model.Habit
import com.example.habittracker.core.domain.model.HabitDraft
import com.example.habittracker.core.domain.model.HabitIcon
import com.example.habittracker.core.domain.model.HabitUpdate
import com.example.habittracker.core.domain.model.HabitIcons
import java.time.ZonedDateTime

class HabitValidator {
    fun normalizeName(name: String): String = name.trim()

    fun validateDraft(draft: HabitDraft, today: ZonedDateTime): HabitError? {
        val invalidName = validateName(draft.name)
        val invalidIcon = validateIcon(draft.icon)
        return when {
            invalidName != null -> invalidName
            invalidIcon != null -> invalidIcon
            draft.creationDate.isAfter(today) -> HabitError.CreationDateInFuture
            else -> null
        }
    }

    fun validateUpdate(update: HabitUpdate): HabitError? {
        val invalidName = validateName(update.name)
        val invalidIcon = validateIcon(update.icon)
        return when {
            invalidName != null -> invalidName
            invalidIcon != null -> invalidIcon
            else -> null
        }
    }

    fun validateCompletion(
        habit: Habit,
        date: ZonedDateTime,
        today: ZonedDateTime,
    ): HabitError? {
        return when {
            date.toLocalDate().isBefore(habit.creationDate.toLocalDate()) ->
                HabitError.CompletionDateInvalid(
                    HabitError.CompletionDateInvalid.CompletionDateReason.BEFORE_CREATION,
                )
            date.toLocalDate().isAfter(today.toLocalDate()) ->
                HabitError.CompletionDateInvalid(
                    HabitError.CompletionDateInvalid.CompletionDateReason.IN_THE_FUTURE,
                )
            !habit.schedule.isScheduled(date) ->
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

    private fun validateIcon(icon: HabitIcon): HabitError? {
        return if (HabitIcons.isKnown(icon)) null else HabitError.InvalidIcon(icon.name)
    }
}
