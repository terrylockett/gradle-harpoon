package ca.terrylockett.gradleharpoon.action;

import ca.terrylockett.gradleharpoon.actiongroups.GradleTasksActionGroup;
import ca.terrylockett.gradleharpoon.configurations.HarpoonConfigurationsUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import org.jetbrains.annotations.NotNull;

public class GradleModuleAction extends AnAction {

	String projectPath = "";
	int hotkeyIndex = -1;

	public GradleModuleAction() {
		super();
	}

	public GradleModuleAction(String name, String projectPath, int hotkeyIndex) {
		super(name);
		this.projectPath = projectPath;
		this.hotkeyIndex = hotkeyIndex;
	}


	@Override
	public void actionPerformed(@NotNull AnActionEvent e) {
		JBPopupFactory.getInstance()
				.createActionGroupPopup("Harpoon - task", new GradleTasksActionGroup(projectPath, hotkeyIndex), e.getDataContext(), JBPopupFactory.ActionSelectionAid.SPEEDSEARCH, false)
				.showInFocusCenter();

		HarpoonConfigurationsUtil.setHarpoonModule(e, this.projectPath, hotkeyIndex);
	}
}
