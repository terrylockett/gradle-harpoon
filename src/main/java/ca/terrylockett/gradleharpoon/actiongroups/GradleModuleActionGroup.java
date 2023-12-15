package ca.terrylockett.gradleharpoon.actiongroups;

import ca.terrylockett.gradleharpoon.action.GradleModuleAction;
import ca.terrylockett.gradleharpoon.configurations.HarpoonConfigurationsUtil;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.gradle.settings.GradleExtensionsSettings;
import org.jetbrains.plugins.gradle.settings.GradleSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

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
		var allModulePaths = getModulePaths(e);

		ArrayList<AnAction> actionsList = new ArrayList<>();
		for (String module : allModulePaths) {
			String name = getModuleNameFromPath(e, module);
			actionsList.add(new GradleModuleAction(name, module, hotkeyIndex));
		}

		return actionsList.toArray(new AnAction[0]);
	}

	private String getModuleNameFromPath(AnActionEvent e, String modulePath) {
		String rootProjectPath = HarpoonConfigurationsUtil.getRootProjectPath(e);
		String name = "";
		if (modulePath.equals(rootProjectPath)) {
			name = Arrays.stream(rootProjectPath.split("/")).reduce((first, second) -> second).orElse(null);
		} else {
			name = Arrays.stream(modulePath.split("/")).reduce((first, second) -> second).orElse(null);
		}
		return name;
	}
	
	public static Set<String> getModulePaths(AnActionEvent e) {
		var project = e.getProject();
		GradleSettings gradleSettings = GradleSettings.getInstance(project);
		var gradleProjectSettings = gradleSettings.getLinkedProjectsSettings().stream().findFirst().get();
		var gradleExtensionSettings = GradleExtensionsSettings.getInstance(e.getProject());
		var rootGradleProject = gradleExtensionSettings.getRootGradleProject(gradleProjectSettings.getExternalProjectPath());

		return rootGradleProject.extensions.keySet();
	}

}
