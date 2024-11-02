package ca.terrylockett.gradleharpoon.config

import ca.terrylockett.gradleharpoon.util.HarpoonUtil
import com.intellij.execution.RunManager
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import org.jetbrains.plugins.gradle.service.execution.GradleExternalTaskConfigurationType
import org.jetbrains.plugins.gradle.service.execution.GradleRunConfiguration
import org.jetbrains.plugins.gradle.util.GradleUtil

const val TEST_TASK_FQN = "org.gradle.api.tasks.testing.Test";

data class HarpoonRunConfiguration(
	var rootProjectName: String,
	var bookmarkIndex: Int,
	var moduleName: String? = null,
	var taskName: String? = null,
	var isTest: Boolean = false
) {


	fun getConfigurationName(): String {
		if (":" == moduleName) {
			return "$rootProjectName [$taskName]"
		}
		return "$rootProjectName$moduleName [$taskName]"
	}

	private fun getConfigurationCmdline(): String {
		return taskName!!
	}

	private fun getConfigurationExternalPath(project: Project): String {
		val modules = ModuleManager.getInstance(project).modules

		if (":" == (moduleName)) {
			return GradleUtil.findGradleModuleData(modules[0])!!.data.linkedExternalProjectPath;
		}
		for (module in modules) {
			val gradleModuleData = GradleUtil.findGradleModuleData(module)!!.data;
			if (gradleModuleData.id == moduleName) {
				println("path for module: ${gradleModuleData.linkedExternalProjectPath}");
				return gradleModuleData.linkedExternalProjectPath;
			}
		}

		error("Could not create Configuration External Path for $moduleName")
	}

	fun addConfigurationToIDE(project: Project) {
		val runManager = RunManager.getInstance(project)
		val runnerAndConfigurationSettings =
			runManager.createConfiguration("todo", GradleExternalTaskConfigurationType::class.java)
		runnerAndConfigurationSettings.isTemporary = true

		val runConfiguration: GradleRunConfiguration =
			runnerAndConfigurationSettings.configuration as GradleRunConfiguration

		runConfiguration.name = getConfigurationName()
		runConfiguration.rawCommandLine = getConfigurationCmdline()
		runConfiguration.settings.externalProjectPath = getConfigurationExternalPath(project)

		if (isTestTask(project)) {
			runConfiguration.isRunAsTest = true
		}

		runManager.addConfiguration(runnerAndConfigurationSettings)
	}

	private fun isTestTask(project: Project): Boolean {
		val rootGradleProject = HarpoonUtil.getRootGradleProject(project)
		val gradleExtensionData = rootGradleProject.extensions[moduleName];

		val taskMap = gradleExtensionData!!.tasksMap;
		val task = taskMap[taskName];

		return TEST_TASK_FQN == task!!.typeFqn;
	}

}