package ca.terrylockett.gradleharpoon.action;

import com.intellij.execution.RunManager;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.gradle.service.execution.GradleExternalTaskConfigurationType;
import org.jetbrains.plugins.gradle.service.execution.GradleRunConfiguration;
import org.jetbrains.plugins.gradle.settings.GradleSettings;

import java.util.Arrays;

public class GradleTaskAction extends AnAction {

	public static final String PERSISTENCE_KEY = "ca.terrylockett.gradleharpoon.task.";

	String module = "task: module not implemented";
	String taskName = "Task name not implemented";
	int hotkeyIndex = -1;


	public GradleTaskAction() {
		super();
	}

	public GradleTaskAction(String module, String taskName, int hotkeyIndex) {
		super(taskName);
		this.module = module;
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
		System.out.println("Task Action Triggered");
		if (null == e.getProject()) {
			System.out.println("null Task Action");
			return;
		}

		var runManager = RunManager.getInstance(e.getProject());
		var runAndConfig = runManager.createConfiguration("todo", GradleExternalTaskConfigurationType.class);
		var gradleRunConfiguration = (GradleRunConfiguration) runAndConfig.getConfiguration();

		gradleRunConfiguration.setName(getConfigurationName(module, taskName, e));
		gradleRunConfiguration.setRawCommandLine(this.taskName);
		gradleRunConfiguration.getSettings().setExternalProjectPath(this.module);

		runManager.addConfiguration(runAndConfig);

		//store task name
		var propertiesComponent = PropertiesComponent.getInstance(e.getProject());
		propertiesComponent.setValue(PERSISTENCE_KEY + hotkeyIndex, this.taskName);

	}


	public static String getConfigurationName(String modulePath, String task, AnActionEvent e) {
		var gradleSettings = GradleSettings.getInstance(e.getProject());
		var gradleProjectSettings = gradleSettings.getLinkedProjectsSettings().stream().findFirst().get();
		String path = gradleProjectSettings.getExternalProjectPath();
		
		String rootProjectName = Arrays.stream(path.split("/")).reduce((first, second) -> second).orElse(null);

		String gradlePath = "";
		if (modulePath.equals(path)) {
			gradlePath = ":";
		} else {
			gradlePath = modulePath.replace(path, "").replace("/", ":");
		}

		return rootProjectName + gradlePath + " [" + task + "]";
	}

}
