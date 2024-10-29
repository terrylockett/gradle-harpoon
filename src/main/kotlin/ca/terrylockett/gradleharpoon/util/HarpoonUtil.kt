package ca.terrylockett.gradleharpoon.util

import com.intellij.execution.ExecutorRegistry
import com.intellij.execution.ProgramRunnerUtil
import com.intellij.execution.RunManager
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import org.jetbrains.plugins.gradle.service.execution.GradleExternalTaskConfigurationType
import org.jetbrains.plugins.gradle.settings.GradleExtensionsSettings
import org.jetbrains.plugins.gradle.settings.GradleSettings

class HarpoonUtil {
	companion object {

		fun getRootProjectName(project: Project): String {
			val gradleSettings = GradleSettings.getInstance(project);
			val gradleProjectSettings = gradleSettings.linkedProjectsSettings.stream().findFirst().get();
			return gradleProjectSettings.externalProjectPath.split("/").last()
		}

		fun getRootGradleProject(project: Project): GradleExtensionsSettings.GradleProject {
			val gradleSettings = GradleSettings.getInstance(project);
			val gradleProjectSettings = gradleSettings.linkedProjectsSettings.stream().findFirst().get();
			val gradleExtensionSettings = GradleExtensionsSettings.getInstance(project);
			val rootGradleProject =
				gradleExtensionSettings.getRootGradleProject(gradleProjectSettings.externalProjectPath);

			return rootGradleProject!!
		}

		fun executeRunConfiguration(configName: String, e: AnActionEvent) {
			val runManager = RunManager.getInstance(e.project!!)
			val configSettings =
				runManager.getConfigurationSettingsList(GradleExternalTaskConfigurationType::class.java)
			for (configSetting in configSettings) {
				if (configSetting.name == configName) {
					ExecutorRegistry.getInstance().getExecutorById("Run")
						?.let { ProgramRunnerUtil.executeConfiguration(configSetting, it) }
					break
				}
			}
		}

	}
}