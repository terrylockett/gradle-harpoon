package ca.terrylockett.gradleharpoon.configurations;

import com.intellij.execution.ExecutorRegistry;
import com.intellij.execution.ProgramRunnerUtil;
import com.intellij.execution.RunManager;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.plugins.gradle.service.execution.GradleExternalTaskConfigurationType;
import org.jetbrains.plugins.gradle.service.execution.GradleRunConfiguration;
import org.jetbrains.plugins.gradle.settings.GradleSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HarpoonConfigurationsUtil {

	public static final String TASK_PERSISTENCE_KEY = "ca.terrylockett.gradleharpoon.task.";
	public static final String MODULE_PERSISTENCE_KEY = "ca.terrylockett.gradleharpoon.module.";

	private HarpoonConfigurationsUtil(){}
	
	public static List<String> getList(AnActionEvent e) {
		var propertiesComponent = PropertiesComponent.getInstance(e.getProject());

		ArrayList<String> savedConfigurations = new ArrayList<>();
		for (int i = 1; i < 6; i++) {
			String moduleVal = propertiesComponent.getValue(MODULE_PERSISTENCE_KEY + i);
			String taskVal = propertiesComponent.getValue(TASK_PERSISTENCE_KEY + i);

			if (null == moduleVal || null == taskVal) {
				savedConfigurations.add(i + "  - ");
			} else {
				String gradlePath = HarpoonConfigurationsUtil.getConfigurationName(moduleVal, taskVal, e);
				savedConfigurations.add(i+"  " + gradlePath);
			}
		}

		return savedConfigurations;
	}
	
	public static void runConfig(String configName, AnActionEvent e) {
		var runManager = RunManager.getInstance(e.getProject());
		var configurations = runManager.getAllConfigurationsList();

		var configsSettings = runManager.getConfigurationSettingsList(GradleExternalTaskConfigurationType.class);
		for (var configSettings : configsSettings) {
			if (configSettings.getName().equals(configName)) {
				ProgramRunnerUtil.executeConfiguration(configSettings, ExecutorRegistry.getInstance().getExecutorById("Run"));
				break;
			}
		}
	}

	public static void addConfigurationToIDE(AnActionEvent e, String modulePath, String taskName) {
		var runManager = RunManager.getInstance(e.getProject());
		var runAndConfig = runManager.createConfiguration("todo", GradleExternalTaskConfigurationType.class);
		var gradleRunConfiguration = (GradleRunConfiguration) runAndConfig.getConfiguration();

		gradleRunConfiguration.setName(HarpoonConfigurationsUtil.getConfigurationName(modulePath, taskName, e));
		gradleRunConfiguration.setRawCommandLine(taskName);
		gradleRunConfiguration.getSettings().setExternalProjectPath(modulePath);
		if("test".equals(taskName)) {
			//TODO probably is a better way to know if something is a "test" task.
			gradleRunConfiguration.setRunAsTest(true);
		}
		
		runManager.addConfiguration(runAndConfig);
	}

	public static void resetConfigurations(AnActionEvent e) {

		var propertiesComponent = PropertiesComponent.getInstance(e.getProject());
		for (int i = 1; i < 6; i++) {
			propertiesComponent.unsetValue(MODULE_PERSISTENCE_KEY + i);
			propertiesComponent.unsetValue(TASK_PERSISTENCE_KEY + i);
		}
	}
	
	public static void setHarpoonTask(AnActionEvent e, String task, int hotkeyIndex) {
		var propertiesComponent = PropertiesComponent.getInstance(e.getProject());
		propertiesComponent.setValue(TASK_PERSISTENCE_KEY + hotkeyIndex, task);
	}
	
	public static void setHarpoonModule(AnActionEvent e, String modulePath, int hotkeyIndex) {
		var propertiesComponent = PropertiesComponent.getInstance(e.getProject());
		propertiesComponent.setValue(MODULE_PERSISTENCE_KEY + hotkeyIndex, modulePath);
	}
	
	//
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
	
	public static String getRootProjectPath(AnActionEvent e) {
		var gradleSettings = GradleSettings.getInstance(e.getProject());
		var gradleProjectSettings = gradleSettings.getLinkedProjectsSettings().stream().findFirst().get();
		String path = gradleProjectSettings.getExternalProjectPath();

		return path;
	}


	
}
