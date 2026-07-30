# TechCraft: Additions Development Rules

## Project identity

TechCraft: Additions is a NeoForge addon that integrates major TechCraft
systems through original endgame content.

The flagship feature is the Dark World Engine multiblock and its installable
Sculk Resonance Engine core.

## Safety

- Never modify the user's live Minecraft instance.
- Never access or modify world saves unless explicitly instructed.
- Never overwrite existing TechCraft KubeJS or FTB Quests work.
- Never modify third-party JAR files.
- Use Git branches and small commits.
- Back up files before migrations.

## Accuracy

- Verify registry IDs from the installed mod versions.
- Verify APIs from source, documentation or inspected classes.
- Do not invent class names or API methods.
- Mark unverified assumptions clearly.
- Prefer a working minimal implementation over speculative complexity.

## Architecture

- Keep rendering separate from server-side machine logic.
- Keep multiblock validation separate from teleportation logic.
- Do not perform expensive structure scans every game tick.
- Persist machine state safely through block entity data.
- Keep client-only code out of dedicated-server execution paths.
- Avoid hard dependencies unless direct code linkage requires them.
- Prefer recipes and data-driven integration where appropriate.

## Workflow

- Build after each meaningful implementation stage.
- Run tests or development launches where practical.
- Include relevant logs when reporting failure.
- Summarise files changed and outstanding risks after each task.
