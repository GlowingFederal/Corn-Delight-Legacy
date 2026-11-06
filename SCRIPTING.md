# Corn Delight recipe scripting

On Forge 1.7.10, install optional MineTweaker 3. On Forge 1.12.2, install optional CraftTweaker 2. Use Farmer's Delight Classic Edition's [scripting API and working examples](https://github.com/GlowingFederal/FarmersDelight-Legacy/blob/1.16.5/SCRIPTING.md). The alternative 1.12.2 Farmer's Delight implementation retains its own scripting APIs.

The addon has no additional recipe machine: use `mods.farmersdelight.CookingPot`, `CuttingBoard` and `Skillet` for its recipes, with the addon’s normal item namespace.

Add/remove edits affect the actual gameplay and NEI/JEI recipe catalogs. IDs, ingredient metadata/NBT, output counts, serving containers, heat/tool requirements and native container returns are preserved. Remove an existing ID or overlapping recipe before adding its replacement. Use the same scripts on client and server. MineTweaker reload is undoable on 1.7.10; restart for CraftTweaker 2 recipe changes on 1.12.2. Neither scripting mod nor UAB is required.

With Classic Edition, both targets resolve original modern ingredients through the core's UAB integration. Its compatibility/recipe switches and UAB's effective per-feature, dependency and update-master switches apply. Missing original inputs keep recipes deferred with startup diagnostics. No substitute ingredients or external processing machines are added.
