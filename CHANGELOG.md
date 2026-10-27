# Changelog

2026-10-23 16:10 â€” Introduce native Forge 1.7.10 and 1.12.2 ports

- Add eight-stage corn with independently growing upper halves, edible plantable kernels, wild corn acquisition, shears collection, storage blocks, and all fourteen upstream foods.
- Load the upstream recipes and additive ingredient tags through Farmer's Delight Legacy 1.0.2. Preserve crafting quantities, cutting products, cooking times, food nutrition, effects and container returns.
- Keep sweet-berry recipes and unavailable Create/Immersive Engineering integrations deferred for future dependency support. Do not invent replacement ingredients or ship MMLib classes.
- Preserve the original source and assets as references and use independent native target builds and distribution JARs.

2026-10-23 22:14 â€” Add optional UAB compatibility on Forge 1.7.10

- Recognize enabled UAB sweet berries, beetroot vegetables and cooked mutton through the shared Farmer's Delight resolver for original stuffing, pumpkin and taco recipes. Preserve native corn farming and biome rules.
- Follow the shared compatibility settings in farmersdelight.cfg, existing addon settings and UAB feature availability. Keep UAB optional and preserve saved registrations and other Minecraft implementations.

2026-10-24 12:17 — Support either Farmer's Delight implementation on Forge 1.12.2

- Select an isolated integration for our port or ukmojb's Farmer's Delight Legacy 1.1.7+, using one addon JAR. Keep Farmer's Delight mandatory and UAB optional; the two Farmer's Delight mods cannot coexist.
- Connect recipes to the selected implementation's actual cooking, cutting, grilling and JEI recipe lists. Preserve food effects, containers, crop or serving behavior, item identities and existing unavailable-ingredient gates.
- Leave Forge 1.7.10 gameplay and its dependency path unchanged.
- Preserve the original artwork for corn crates and kernel bags with either Farmer's Delight texture layout.

2026-10-24 16:51 — Release 1.0.1

- Update addon metadata and release distributions to 1.0.1 for Forge 1.7.10 and 1.12.2.
- Track version inputs when generating resources so internal mod metadata stays aligned with the release filename.
