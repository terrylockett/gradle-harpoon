package ca.terrylockett.gradleharpoon.action

import ca.terrylockett.gradleharpoon.config.HarpoonRunConfiguration
import ca.terrylockett.gradleharpoon.util.HarpoonUtil
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.JBPopupFactory

class ModuleSelectActionGroup(private val harpoonRunConfiguration: HarpoonRunConfiguration) : ActionGroup(){

	override fun getChildren(e: AnActionEvent?): Array<AnAction> {
		val moduleNames = getProjectModuleNames(e!!.project!!)
		
		val actions = ArrayList<AnAction>()
		for (moduleName in moduleNames) {
			actions.add(ModuleAction(harpoonRunConfiguration.copy(moduleName = moduleName)))
		}

		return actions.toTypedArray()
	}

	private fun getProjectModuleNames(project: Project): Set<String> {
		val rootGradleProject = HarpoonUtil.getRootGradleProject(project)
		return rootGradleProject.extensions.keys
	}
}


class ModuleAction(private val harpoonRunConfiguration: HarpoonRunConfiguration) : AnAction(harpoonRunConfiguration.moduleName) {

	override fun actionPerformed(e: AnActionEvent) {
		JBPopupFactory.getInstance()
			.createActionGroupPopup(
				"Harpoon - Task",
				TaskSelectActionGroup(harpoonRunConfiguration),
				e.dataContext,
				JBPopupFactory.ActionSelectionAid.SPEEDSEARCH,
				false
			)
			.showInFocusCenter()
	}
}

