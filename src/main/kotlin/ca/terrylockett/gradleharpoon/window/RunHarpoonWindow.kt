package ca.terrylockett.gradleharpoon.window

import ca.terrylockett.gradleharpoon.persistence.HARPOON_BOOKMARK_COUNT
import ca.terrylockett.gradleharpoon.persistence.HarpoonPersistence
import ca.terrylockett.gradleharpoon.util.HarpoonUtil
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.popup.JBPopupFactory
import javax.swing.ListSelectionModel.SINGLE_SELECTION

class RunHarpoonWindow : AnAction() {

	override fun actionPerformed(e: AnActionEvent) {
		val persistedConfigs = HarpoonPersistence.getHarpoonConfigs(e.project!!)
		val harpoonEntries = Array(HARPOON_BOOKMARK_COUNT) { i -> "${i + 1}: -" }

		for ((i, configName) in persistedConfigs.withIndex()) {
			configName?.let { harpoonEntries[i] = "${i + 1}: $it" }
		}

		val runConfigCallback: (String) -> Unit = {
			val configName = it.substring(3)
			println(configName)
			HarpoonUtil.executeRunConfiguration(configName, e)
		}

		JBPopupFactory.getInstance()
			.createPopupChooserBuilder(harpoonEntries.asList())
			.setVisibleRowCount(HARPOON_BOOKMARK_COUNT)
			.setSelectionMode(SINGLE_SELECTION)
			.setItemChosenCallback(runConfigCallback)
			.setSelectionMode(SINGLE_SELECTION)
			.setTitle("Harpoon Select")
			.createPopup()
			.showCenteredInCurrentWindow(e.project!!);
	}

}