# Building and testing

Run Gradle from the repository folder. Gradle uses Java 17 or newer, the mod
compiles for Temurin Java 8, and ForgeGradle's Mavenizer preparation needs
Java 25. The wrapper fixes Gradle at 9.6.1. CI pins the compiler baselines;
a newer Temurin patch release of the required Java major can be used locally.

The required core jar is downloaded from the IAF GitHub release and checked
against its SHA-256 before use. Supply the two BYG test jars listed in
`gradle/verification-dependencies.json` under `run-dependencies/artifacts`.
They are local test dependencies, not redistributed assets.

## Normal build

```text
gradlew clean check build javadoc verifyReleaseChecksums
gradlew prepareEclipse verifyEclipseProductionClasspath
```

The check tasks validate catalog output, textures, locale key sets, metadata,
namespace ownership, repository hygiene, workflow pins and Maven coordinates.
The release output is one main jar, one sources jar and one Javadoc jar.

The nested Eclipse workspace uses the checkout's folder name as its project
name. Production resources go to `bin/main`; tests and probes have separate
outputs and are excluded from ordinary launches.

## Updating the catalog

```text
gradlew updateFurnitureCatalog
```

Only this explicit command rewrites catalog-owned files. Normal builds
regenerate into a temporary build directory and compare the committed output.
Wood IDs and texture mappings are in `gradle/furniture-catalog.json`; translated
furniture labels are in `gradle/furniture-labels.json`.

## Runtime probes

```text
gradlew runtimeProbeJar
```

The resulting `build/runtime-test/iafbyg-runtime-probe.jar` is test-only and
must never be included in a player distribution. It can run alongside packaged
IAF, BYG and add-on jars in a disposable Forge server. Set
`-Diafbyg.probe.phase3=true` to create the older padded-bench fixture. Without
that flag it verifies the Phase 4 catalog, crafting and saved furniture, then
shuts the test server down.

The client probe checks all Creative inventory models when started with
`-Diafbyg.probe.client=true`, then closes the test client.
For a development run, use `gradlew runClientProbe -PrunDirectory=run-client-probe`.
Supply `-PbygRuntimeJar=run-dependencies/artifacts/BYG_1.12.2_1.9.jar` to test the
community-fixed jar instead. Ordinary `runClient` launches never load the probe.

## Publishing

This candidate stays local for hands-on testing. Publication remains disabled
while `curseforge_project_id` is `UNASSIGNED`; a project number and a reviewed
release workflow are required before an upload is possible.
