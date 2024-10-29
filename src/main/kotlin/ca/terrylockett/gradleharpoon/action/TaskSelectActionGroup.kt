package ca.terrylockett.gradleharpoon.action

import ca.terrylockett.gradleharpoon.config.HarpoonRunConfiguration
import ca.terrylockett.gradleharpoon.persistence.HarpoonPersistence
import ca.terrylockett.gradleharpoon.util.HarpoonUtil
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project

class TaskSelectActionGroup(private val harpoonRunConfiguration: HarpoonRunConfiguration) : ActionGroup() {

	override fun getChildren(e: AnActionEvent?): Array<AnAction> {
		val taskNames = getTaskNames(e!!.project!!, harpoonRunConfiguration.moduleName!!)
		val actions = ArrayList<AnAction>()
		for (taskName in taskNames) {
			actions.add(TaskAction(harpoonRunConfiguration.copy(taskName = taskName)))
		}
		return actions.toTypedArray()
	}

	private fun getTaskNames(project: Project, moduleName: String): Set<String> {
		val rootGradleProject = HarpoonUtil.getRootGradleProject(project)

		if (!rootGradleProject.extensions.containsKey(moduleName)) {
			return HashSet()
		}

		val gradleExtensionData = rootGradleProject.extensions[moduleName]
		val gradleTasks = gradleExtensionData!!.tasksMap.keys

		return HashSet(gradleTasks)
	}
}


class TaskAction(private val harpoonRunConfiguration: HarpoonRunConfiguration) :
	AnAction(harpoonRunConfiguration.taskName) {
	override fun actionPerformed(e: AnActionEvent) {
		harpoonRunConfiguration.addConfigurationToIDE(e.project!!)
		HarpoonPersistence.setHarpoonTask(e.project!!, harpoonRunConfiguration)
	}
}