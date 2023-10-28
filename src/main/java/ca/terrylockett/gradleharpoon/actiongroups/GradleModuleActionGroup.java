package ca.terrylockett.gradleharpoon.actiongroups;

import ca.terrylockett.gradleharpoon.action.GradleModuleAction;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.gradle.settings.GradleSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GradleModuleActionGroup extends ActionGroup {

	int hotkeyIndex = -1;

	public GradleModuleActionGroup() {
		super();
	}

	public GradleModuleActionGroup(int hotkeyIndex) {
		super();
		this.hotkeyIndex = hotkeyIndex;
	}


	@Override
	public AnAction @NotNull [] getChildren(@Nullable AnActionEvent e) {
		var list = getModulePaths(e);

		ArrayList<AnAction> actionsList = new ArrayList<>();
		for (String module : list) {
			String name = "";
			String taskName = "";
			String rawCommand = "test";
			String projectPath = "";


			taskName = module + " [" + rawCommand + "]";
			name = taskName;
			projectPath = module;

			actionsList.add(new GradleModuleAction(name, taskName, rawCommand, projectPath, hotkeyIndex));
		}

		return actionsList.toArray(new AnAction[actionsList.size()]);
	}

	private List<String> getModulePaths(AnActionEvent e) {
		var project = e.getProject();
		GradleSettings gs = GradleSettings.getInstance(project);

		var list = gs.getLinkedProjectsSettings();
		var config = list.stream().findFirst().get();

		String basePath = gs.getProject().getBasePath();
		String baseName = Arrays.stream(basePath.split("/")).reduce((first, second) -> second).orElse(null);
		config.getModules();

		List<String> returnList = new ArrayList<>();
		for (String module : config.getModules()) {
			returnList.add(module);
		}

		return returnList;
	}

}
