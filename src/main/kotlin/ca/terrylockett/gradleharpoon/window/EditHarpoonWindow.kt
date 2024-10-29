package ca.terrylockett.gradleharpoon.window

import ca.terrylockett.gradleharpoon.action.ModuleSelectActionGroup
import ca.terrylockett.gradleharpoon.config.HarpoonRunConfiguration
import ca.terrylockett.gradleharpoon.persistence.HARPOON_BOOKMARK_COUNT
import ca.terrylockett.gradleharpoon.persistence.HarpoonPersistence
import ca.terrylockett.gradleharpoon.util.HarpoonUtil
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.ui.popup.JBPopupFactory.ActionSelectionAid.SPEEDSEARCH
import javax.swing.ListSelectionModel.SINGLE_SELECTION


class EditHarpoonWindow : AnAction() {

	override fun actionPerformed(e: AnActionEvent) {
		val harpoonEntries = Array(HARPOON_BOOKMARK_COUNT) { i -> "${i + 1}: -" }
		val persistedConfigs = HarpoonPersistence.getHarpoonConfigs(e.project!!)
		for ((i, configName) in persistedConfigs.withIndex()) {
			configName?.let { harpoonEntries[i] = "${i + 1}: $it" }
		}
		val rootProjectName = HarpoonUtil.getRootProjectName(e.project!!)

		JBPopupFactory.getInstance().createPopupChooserBuilder(harpoonEntries.asList())
			.setVisibleRowCount(HARPOON_BOOKMARK_COUNT).setSelectionMode(SINGLE_SELECTION).setItemChosenCallback {
				val harpoonRunConfiguration = HarpoonRunConfiguration(
					rootProjectName = rootProjectName, bookmarkIndex = getSelectedItemIndex(it)
				)
				val actionGroup = ModuleSelectActionGroup(harpoonRunConfiguration)
				JBPopupFactory.getInstance().createActionGroupPopup(
					"Harpoon", actionGroup, e.dataContext, SPEEDSEARCH, true
				).showInFocusCenter()
			}.setSelectionMode(SINGLE_SELECTION).setTitle("Edit Harpoon Hotkeys").createPopup()
			.showCenteredInCurrentWindow(e.project!!);
	}

	private fun getSelectedItemIndex(item: String): Int {
		return item.substring(0, 1).toInt()
	}
}
