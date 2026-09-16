# HabitTracker plan

## Current status
- Layered architecture refactor is in place: domain use cases are split into per-file `*UseCase` classes, ViewModels are kept in individual screen files, and screen-level composables inject their own ViewModel instances.
- Koin modules are split into `dataModule.kt`, `domainModule.kt`, and `presentationModule.kt`.
- The app compiles successfully with the debug Kotlin compile task.

## Refinements in progress
- Remove remaining default arguments from screen-level production composables and rely on explicit values in preview/test setup.
- Clean up any remaining full package references by importing concrete types directly.
- Rename helper view components to `*View` naming conventions for consistency with the screen conventions.

## Next step
- Keep the screen layer lean and explicit, passing only the values each screen genuinely renders.
- If additional UI work is requested, expand the screen state and use cases in small, targeted changes rather than broad rewrites.
