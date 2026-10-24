# Changelog

2026-10-23 16:10 — Introduce native Forge 1.7.10 and 1.12.2 ports

- Add eight-stage corn with independently growing upper halves, edible plantable kernels, wild corn acquisition, shears collection, storage blocks, and all fourteen upstream foods.
- Load the upstream recipes and additive ingredient tags through Farmer's Delight Legacy 1.0.2. Preserve crafting quantities, cutting products, cooking times, food nutrition, effects and container returns.
- Keep sweet-berry recipes and unavailable Create/Immersive Engineering integrations deferred for future dependency support. Do not invent replacement ingredients or ship MMLib classes.
- Preserve the original source and assets as references and use independent native target builds and distribution JARs.

2026-10-23 22:14 — Add optional UAB compatibility on Forge 1.7.10

- Recognize enabled UAB sweet berries, beetroot vegetables and cooked mutton through the shared Farmer's Delight resolver for original stuffing, pumpkin and taco recipes. Preserve native corn farming and biome rules.
- Follow the shared compatibility settings in farmersdelight.cfg, existing addon settings and UAB feature availability. Keep UAB optional and preserve saved registrations and other Minecraft implementations.
