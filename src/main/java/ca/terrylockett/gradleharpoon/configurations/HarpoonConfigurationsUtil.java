package ca.terrylockett.gradleharpoon.configurations;

import com.intellij.execution.ExecutorRegistry;
import com.intellij.execution.ProgramRunnerUtil;
import com.intellij.execution.RunManager;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.module.ModuleManager;
import org.jetbrains.plugins.gradle.service.execution.GradleExternalTaskConfigurationType;
import org.jetbrains.plugins.gradle.service.execution.GradleRunConfiguration;
import org.jetbrains.plugins.gradle.settings.GradleExtensionsSettings;
import org.jetbrains.plugins.gradle.settings.GradleSettings;
import org.jetbrains.plugins.gradle.util.GradleUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ca.terrylockett.gradleharpoon.actiongroups.GradleTasksActionGroup.getModuleGradlePath;

public class HarpoonConfigurationsUtil {

	public static final String TASK_PERSISTENCE_KEY = "ca.terrylockett.gradleharpoon.task.";
	public static final String MODULE_PERSISTENCE_KEY = "ca.terrylockett.gradleharpoon.module.";

	public static final int NUMBER_OF_BOOKMARKS = 5;
	
	private static final String TEST_TASK_FQN = "org.gradle.api.tasks.testing.Test";
	
	
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
		runAndConfig.setTemporary(true);
		
		var gradleRunConfiguration = (GradleRunConfiguration) runAndConfig.getConfiguration();
		gradleRunConfiguration.setName(HarpoonConfigurationsUtil.getConfigurationName(modulePath, taskName, e));
		gradleRunConfiguration.setRawCommandLine(taskName);

		var modules = ModuleManager.getInstance(e.getProject()).getModules();
		String runConfigPath = "";

		if(modulePath.equals(":")) {
			runConfigPath = GradleUtil.findGradleModuleData(modules[0]).getData().getLinkedExternalProjectPath();
		}

		for(var module : modules) {
			var gradleModuleData = GradleUtil.findGradleModuleData(module).getData();
			if(gradleModuleData.getId().equals(modulePath)) {
				runConfigPath = gradleModuleData.getLinkedExternalProjectPath();
				System.out.println("path for module: " + runConfigPath);
				break;
			}
		}

		gradleRunConfiguration.getSettings().setExternalProjectPath(runConfigPath);
		
		if(isTestTask(e, modulePath, taskName)) {
			gradleRunConfiguration.setRunAsTest(true);
		}
		
		runManager.addConfiguration(runAndConfig);
	}

	
	private static boolean isTestTask(AnActionEvent e, String modulePath, String taskName) {
		var gradleSettings = GradleSettings.getInstance(e.getProject());
		var gradleProjectSettings = gradleSettings.getLinkedProjectsSettings().stream().findFirst().get();
		var gradleExtensionSettings = GradleExtensionsSettings.getInstance(e.getProject());
		var rootGradleProject = gradleExtensionSettings.getRootGradleProject(gradleProjectSettings.getExternalProjectPath());

		String gradleModuleName = getModuleGradlePath(e, modulePath);
		var gradleExtensionData = rootGradleProject.extensions.get(gradleModuleName);
		var taskMap = gradleExtensionData.tasksMap;
		
		if(!taskMap.containsKey(taskName)){
			return false;
		}
		
		var task = taskMap.get(taskName);
		
		return TEST_TASK_FQN.equals(task.typeFqn);
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
