# Gradle Harpoon


<!-- Plugin description -->
IntelliJ plugin to bookmark and run gradle tasks.
<!-- Plugin description end -->


## Installation
Two options:
### Pre Built Plugin:
1. Download plugin zip from the latest [Release](https://github.com/terrylockett/gradle-harpoon/releases/).
2. `Settings-> plugins -> Install Plugin from Disk` with the Zip
### Build form Source
1. Clone project
2. `./gradlew buildPlugin`
3. `Settings-> plugins -> Install Plugin from Disk` with the zip in `build/distributions/`
> [!NOTE] 
> Requires Java 17


## Usage
Add keymaps for the following Actions (or use: Tools -> Gradle Harpoon ) :
- `Edit Harpoon Config` - Used to bookmark a gradle config.
- `Run Harpoon Config` - Used to run a config.
- `Reset Harpoon Configs` - Resets bookmarked configs.


> [!NOTE]  
> If you are using IdeaVim the action names are:
> - `ca.terrylockett.gradleharpoon.window.EditHarpoonWindow`
> - `ca.terrylockett.gradleharpoon.window.RunHarpoonWindow`
> - `ca.terrylockett.gradleharpoon.action.ResetHarpoonConfigurations`