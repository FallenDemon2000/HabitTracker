# HabitTracker

Single-module Jetpack Compose app. Use this file as a map to the project resources. Load the linked resources only when they are relevant; do not duplicate their contents here.

## Product and UI references

- Requirements and behavior: [`specs/habit-tracker-design.md`](specs/habit-tracker-design.md)
- UI mock-ups and design reference: [`specs/Habit Tracker — Design System & Screens.html`](specs/Habit%20Tracker%20%E2%80%94%20Design%20System%20%26%20Screens.html)
- Project fonts: [`specs/Fonts/`](specs/Fonts/)
- Android source: [`app/src/main/`](app/src/main/)

When creating or changing UI, consult the HTML mock-ups first, then use the requirements document for behavior and edge cases. Use the bundled Inter and Manrope fonts from `specs/Fonts/` when implementing the design. Always lazy-load from the specs when needed.

## Agents

Custom agents for specialized tasks:

- **`ai-retrospective`** — Runs at the end of a stable milestone or session to extract generalizable Android learnings from work done and propose new skills or skill updates. It should analyze commits, file changes, architecture decisions, and project guidance while filtering out project-specific domain detail. Read `.agents/agents/ai-retrospective.agent.md` for detailed instructions.
- **`android-development`** — Implements Android features as a senior Android developer using Kotlin, Jetpack Compose, ViewModels, MVVM, clean architecture, coroutines, Flow, and modern Android development practices. Use it when implementing or significantly modifying Android app features.

---

## Applicable skills

For Android work, before working on a layer, **always load the corresponding skill first**, and use the relevant available `android-*` skills (available in .agents directory), especially:

| Layer | Skill to load |
|-------|---------------|
| Presentation / MVI | `android-presentation-mvi` |
| Compose screen architecture | `android-compose-architecture` |
| Compose UI components | `android-compose-components` |
| Data layer (repos, data sources) | `android-data-layer` |
| Dependency injection (Koin) | `android-di-koin` |
| Navigation | `android-navigation` |
| Error handling / Result types | `android-error-handling` |
| Background work / WorkManager | `android-background` |
| Coroutines | `kotlin-coroutines` |
| Flows | `kotlin-flows` |
| Version catalog / Gradle | `android-version-catalog` |
| Testing | `android-testing` |

In case you don't find a skill for a specific task, create a new skill for it and add it to this list (if not already present).
Also apply any other available Android-prefixed skill when its subject matches the task. Keep project-specific requirements and visual details in the referenced resources so they can be loaded on demand.

## Rules

- **Single module, layered packages.** Use the same package structure as the `android-module-structure` skill (core, feature, etc.) but as packages within `:app`, not separate modules.
- **No tests unless asked.** Do not write tests unless the user explicitly requests them.
- **Lazy mockup loading.** When building UI, read the HTML mockup file for that specific screen only — do not read the entire file upfront.

### Git Rules
- **Git hygiene.** `git add` every new file immediately after creating it. Create meaningful, modular commits at logical checkpoints — don't batch everything into one giant commit.
- **Commit often.** Make small, meaningful commits at logical checkpoints. Avoid batching everything into one giant commit.
- **Required commit behavior.** After any requested code, config, test, or documentation change, create a commit immediately after the work is done and validated. Use this commit format: `<prefix>: <infinite verb> + <description>`                                                                                                                                                                                                                   ┃
    - `dev` for production code changes
    - `test` for test code changes
    - `conf` for configuration changes (Gradle, project config, dependency catalog, etc.)
    - `doc` for documentation and code-adjacent guidance changes
    - Example: `dev: add habit list screen`, `conf: update koin dependencies`, `doc: clarify app architecture guidance`
