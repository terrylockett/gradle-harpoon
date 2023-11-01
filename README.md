# Gradle Config Harpoon

## Overview
IntelliJ plugin to bookmark and run gradle tasks.

Intended for Multi-project Gradle repos with a lot of subprojects, where scrolling through tasks in the Gradle window is annoying.

## Installation
Two options:
### Pre Build Plugin:
1. Download plugin zip from newest release
2. `Settings-> plugins -> Install Plugin from Disk` with the Zip
### Build form Source
1. Clone project
2. `./gradlew buildPlugin`
3. `Settings-> plugins -> Install Plugin from Disk` with the zip in build/distributions/


## Usage
Use the following Actions ( Tools -> Gradle Harpoon ) :
- `Edit Harpoon Config` - Used to bookmark a gradle config.
- `Run Harpoon Config` - Used to run a config.
- `Reset Harpoon Configs` - Resets bookmarked configs.

Adding keymaps for the Actions is recommended.