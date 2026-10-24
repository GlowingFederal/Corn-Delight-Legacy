# Corn Delight Legacy

Corn farming and cooking for **Minecraft Forge 1.7.10 and 1.12.2**. Requires **Farmer's Delight Legacy 1.0.2 or newer**, including its normal Mixin bootstrap dependency. MMLib is not required by the native ports.

Find wild corn in temperate overworld biomes, harvest kernels, and plant them on farmland. Corn grows through eight stages and produces a second, independently growing half after the lower stalk reaches stage four. Harvest mature halves for corn and kernels; the lower stalk can grow a replacement upper half. Shears collect wild corn, which can also be processed on the Cutting Board.

Includes cornbread, tortillas, tacos, popcorn, grilled and boiled corn, corn soup, creamed corn and creamy corn drink, plus corn crates and kernel bags. Cooking, cutting and grilling use Farmer's Delight Legacy equipment. Source smoker recipes use the legacy furnace; campfire food recipes use FD grilling.

Recipes requiring sweet berries (cornbread stuffing and the cornbread stuffed-pumpkin variant) remain deferred until the real prerequisite is available. Modern Create and Immersive Engineering recipe serializers are retained in the source catalog but have no legacy adapters yet. No replacements are registered. Modern shipwreck and village-house chest tables and vanilla composters do not exist in these targets; wild generation provides the upstream seed source. MMLib's external TFC nutrition, water, decay and heating metadata are not implemented.

`config/corn_delight.cfg` controls `general.genWildCorn` and the positive cluster-attempt interval `general.chance` (default 30). Generation stays inside the generated chunk.

## Build

The original modern source remains under `src/`, with its build configuration preserved under `reference/`. Native source is in `versions/1.7.10` and `versions/1.12.2`; `common/` contains platform-neutral food data. Recipes, tags, textures and models are derived from the original resources at build time.

Build Farmer's Delight Legacy 1.0.2 in the adjacent `FarmersDelight-Legacy` checkout first. The addon compiles against its development JAR without packaging any FD classes. Use JDK 21 for the coordinator/1.12.2 and JDK 25 for the existing GTNH 1.7.10 tooling; game runtime is Java 8. The launcher recognizes `JAVA21_HOME`, `JAVA25_HOME`, and the usual installed JDK directories.

Run `gradlew.bat buildAll` with the established shared Gradle cache. Verified independent distributions are staged in `build/release/` as `corn_delight-1.0.0+mc1.7.10.jar` and `corn_delight-1.0.0+mc1.12.2.jar`.

Original mod by MCMOD Modding Group / Zheng Syameimaru, under the included MIT license. Native behavior is adapted to each legacy API. The exact MMLib 1.1.1 artifact referenced by the original checkout is unavailable in its published 1.16.5 source branch; tall-crop parity with that specific artifact remains unconfirmed.
