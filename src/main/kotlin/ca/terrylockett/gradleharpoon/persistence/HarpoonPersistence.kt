package ca.terrylockett.gradleharpoon.persistence

import ca.terrylockett.gradleharpoon.config.HarpoonRunConfiguration
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.project.Project

const val HARPOON_PERSISTENCE_KEY = "ca.terrylockett.gradleharpoon"
const val HARPOON_BOOKMARK_COUNT = 5

class HarpoonPersistence {
	companion object {
		fun setHarpoonTask(project: Project, harpoonRunConfiguration: HarpoonRunConfiguration) {
			val persistenceKey = "$HARPOON_PERSISTENCE_KEY${harpoonRunConfiguration.bookmarkIndex}"
			PropertiesComponent.getInstance(project)
				.setValue(persistenceKey, harpoonRunConfiguration.getConfigurationName())
		}

		fun resetConfigurations(project: Project) {
			val propertiesComponent = PropertiesComponent.getInstance(project)
			for (i in 1..HARPOON_BOOKMARK_COUNT) {
				propertiesComponent.unsetValue("$HARPOON_PERSISTENCE_KEY$i");
			}
		}

		fun getHarpoonConfigs(project: Project): Array<String?> {
			val propertiesComponent = PropertiesComponent.getInstance(project)
			return Array<String?>(HARPOON_BOOKMARK_COUNT) {
				propertiesComponent.getValue("$HARPOON_PERSISTENCE_KEY${it + 1}")
			}
		}
	}

}