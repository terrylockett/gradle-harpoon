package ca.terrylockett.gradleharpoon.action;

import ca.terrylockett.gradleharpoon.actiongroups.GradleTasksActionGroup;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import org.jetbrains.annotations.NotNull;

public class GradleModuleAction extends AnAction {

	public static final String PERSISTENCE_KEY = "ca.terrylockett.gradleharpoon.module.";

	String rawCommand = "unset raw command";
	String projectPath = "unset project path";
	String taskName = "unset taskName";
	int hotkeyIndex = -1;
	
	public GradleModuleAction() {
		super();
	}

	public GradleModuleAction(String name, String taskName, String rawCommand, String projectPath, int hotkeyIndex) {
		super(name);
		this.rawCommand = rawCommand;
		this.projectPath = projectPath;
		this.taskName = taskName;
		this.hotkeyIndex = hotkeyIndex;
	}

	@Override
	public void update(@NotNull AnActionEvent event) {
		// Using the event, evaluate the context,
		// and enable or disable the action.
	}

	@Override
	public void actionPerformed(@NotNull AnActionEvent e) {

		JBPopupFactory.getInstance()
				.createActionGroupPopup("Harpoon - task", new GradleTasksActionGroup(projectPath, hotkeyIndex), e.getDataContext(), JBPopupFactory.ActionSelectionAid.SPEEDSEARCH, false)
				.showInFocusCenter();

		var propertiesComponent = PropertiesComponent.getInstance(e.getProject());
		propertiesComponent.setValue(PERSISTENCE_KEY + hotkeyIndex, this.projectPath);
	}
}
