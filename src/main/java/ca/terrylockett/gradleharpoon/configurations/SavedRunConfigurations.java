package ca.terrylockett.gradleharpoon.configurations;

import ca.terrylockett.gradleharpoon.action.GradleModuleAction;
import ca.terrylockett.gradleharpoon.action.GradleTaskAction;
import com.intellij.execution.ExecutorRegistry;
import com.intellij.execution.ProgramRunnerUtil;
import com.intellij.execution.RunManager;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.plugins.gradle.service.execution.GradleExternalTaskConfigurationType;

import java.util.ArrayList;
import java.util.List;

public class SavedRunConfigurations {

	private SavedRunConfigurations(){}
	
	
	public static List<String> getList(AnActionEvent e) {
		var propertiesComponent = PropertiesComponent.getInstance(e.getProject());

		ArrayList<String> savedConfigurations = new ArrayList<>();
		for (int i = 1; i < 6; i++) {
			String moduleVal = propertiesComponent.getValue(GradleModuleAction.PERSISTENCE_KEY + i);
			String taskVal = propertiesComponent.getValue(GradleTaskAction.PERSISTENCE_KEY + i);

			if (null == moduleVal || null == taskVal) {
				savedConfigurations.add(i + "  - ");
			} else {
				String gradlePath = GradleTaskAction.getConfigurationName(moduleVal, taskVal, e);
				savedConfigurations.add(i+"  " + gradlePath);
			}
		}

		return savedConfigurations;
	}
	
	public static void executeRunConfig(String configName, AnActionEvent e) {
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
	
}
