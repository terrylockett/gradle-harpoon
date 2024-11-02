package ca.terrylockett.gradleharpoon.action

import ca.terrylockett.gradleharpoon.persistence.HarpoonPersistence
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class ResetHarpoonConfigurations : AnAction() {
	override fun actionPerformed(e: AnActionEvent) {
		HarpoonPersistence.resetConfigurations(e.project!!)
	}
}