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

	String moduleName = "/unimplemented";
	int hotkeyIndex = -1;

	public GradleTasksActionGroup() {
	}

	public GradleTasksActionGroup(String moduleName, int hotkeyIndex) {
		this.moduleName = moduleName;
		this.hotkeyIndex = hotkeyIndex;
	}

	@Override
	public AnAction @NotNull [] getChildren(@Nullable AnActionEvent e) {

		List<String> taskNames = getTaskNames(e);
		System.out.println("tasks found count: " + taskNames.size());

		ArrayList<AnAction> actionsList = new ArrayList<>();

		for (String task : taskNames) {
			actionsList.add(new GradleTaskAction(moduleName, task, hotkeyIndex));
		}

		return actionsList.toArray(new AnAction[actionsList.size()]);
	}


	private List<String> getTaskNames(AnActionEvent event) {
		var gradleSettings = GradleSettings.getInstance(event.getProject());
		var gradleProjectSettings = gradleSettings.getLinkedProjectsSettings().stream().findFirst().get();
		var gradleExtensionSettings = GradleExtensionsSettings.getInstance(event.getProject());
		var rootGradleProject = gradleExtensionSettings.getRootGradleProject(gradleProjectSettings.getExternalProjectPath());

		List<String> taskNames = new ArrayList<>();

		String path = gradleProjectSettings.getExternalProjectPath();
		String taskName = "";


		if (moduleName.equals(path)) {
			taskName = ":";
		} else {
			taskName = moduleName.replace(path, "").replace("/", ":");
		}


		if (rootGradleProject.extensions.containsKey(taskName)) {
			var gradleExtensionData = rootGradleProject.extensions.get(taskName);
			var gradleTasks = gradleExtensionData.tasksMap.keySet();
			for (var task : gradleTasks) {
				taskNames.add(task);
			}
		}

		return taskNames;
	}

}
