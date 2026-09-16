package com.example.habittracker.core.domain.model

sealed interface HabitError {
    data class InvalidName(val reason: Reason) : HabitError {
        enum class Reason {
            BLANK,
            NOT_TRIMMED,
            TOO_LONG,
        }
    }

    data class InvalidIcon(val iconId: String) : HabitError

    data object InvalidSchedule : HabitError

    data object CreationDateInFuture : HabitError

    data class CompletionDateInvalid(val reason: CompletionDateReason) : HabitError {
        enum class CompletionDateReason {
            BEFORE_CREATION,
            IN_THE_FUTURE,
            NOT_SCHEDULED,
        }
    }

    data class NotFound(val habitId: HabitId) : HabitError

    data class PersistenceConstraint(val operation: String) : HabitError
}