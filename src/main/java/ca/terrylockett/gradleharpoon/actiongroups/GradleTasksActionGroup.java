package ca.terrylockett.gradleharpoon.actiongroups;

import ca.terrylockett.gradleharpoon.action.GradleTaskAction;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.gradle.settings.GradleExtensionsSettings;
import org.jetbrains.plugins.gradle.settings.GradleSettings;

import java.util.ArrayList;
import java.util.List;

public class GradleTasksActionGroup extends ActionGroup {

	String moduleName = "";
	int hotkeyIndex = -1;

	public GradleTasksActionGroup() {}

	public GradleTasksActionGroup(String moduleName, int hotkeyIndex) {
		this.moduleName = moduleName;
		this.hotkeyIndex = hotkeyIndex;
	}

	@Override
	public AnAction @NotNull [] getChildren(@Nullable AnActionEvent e) {
		
		List<String> taskNames = getTaskNames(e);
		List<AnAction> actionsList = new ArrayList<>();
		for (String taskName : taskNames) {
			actionsList.add(new GradleTaskAction(moduleName, taskName, hotkeyIndex));
		}

		return actionsList.toArray(new AnAction[0]);
	}


	private List<String> getTaskNames(AnActionEvent e) {
		var gradleSettings = GradleSettings.getInstance(e.getProject());
		var gradleProjectSettings = gradleSettings.getLinkedProjectsSettings().stream().findFirst().get();
		var gradleExtensionSettings = GradleExtensionsSettings.getInstance(e.getProject());
		var rootGradleProject = gradleExtensionSettings.getRootGradleProject(gradleProjectSettings.getExternalProjectPath());
		
		String gradleModuleName = getModuleGradlePath(e, moduleName);
		List<String> taskNames = new ArrayList<>();

		if (rootGradleProject.extensions.containsKey(gradleModuleName)) {
			var gradleExtensionData = rootGradleProject.extensions.get(gradleModuleName);
			var gradleTasks = gradleExtensionData.tasksMap.keySet();
			taskNames.addAll(gradleTasks);
		}

		return taskNames;
	}
	
	private String getModuleGradlePath(AnActionEvent e, String modulePath) {
		var gradleSettings = GradleSettings.getInstance(e.getProject());
		var gradleProjectSettings = gradleSettings.getLinkedProjectsSettings().stream().findFirst().get();
		String path = gradleProjectSettings.getExternalProjectPath();
		
		String gradleModuleName;
		if (modulePath.equals(path)) {
			gradleModuleName = ":";
		} else {
			gradleModuleName = modulePath.replace(path, "").replace("/", ":");
		}
		
		return gradleModuleName;
	}

}
