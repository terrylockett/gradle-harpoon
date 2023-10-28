package ca.terrylockett.gradleharpoon.popup;

import ca.terrylockett.gradleharpoon.action.GradleTaskAction;
import com.intellij.execution.*;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.gradle.service.execution.GradleExternalTaskConfigurationType;
import org.jetbrains.plugins.gradle.service.execution.GradleRunConfiguration;

import java.util.ArrayList;
import java.util.List;

import static javax.swing.ListSelectionModel.SINGLE_SELECTION;

public class RunHotkeyPopup extends AnAction {
	
	@Override
	public void actionPerformed(@NotNull AnActionEvent e) {

		var propertiesComponent = PropertiesComponent.getInstance(e.getProject());
//		propertiesComponent.setValue("ca.terrylockett.gradleharpoon.module."+hotkeyIndex, this.module);
//		propertiesComponent.setValue("ca.terrylockett.gradleharpoon.task."+hotkeyIndex, this.taskName);

		ArrayList<String> list = new ArrayList<>();
		
		for(int i=1; i<6; i++) {
			String moduleVal = propertiesComponent.getValue("ca.terrylockett.gradleharpoon.module."+i);
			String taskVal = propertiesComponent.getValue("ca.terrylockett.gradleharpoon.task."+i);
			if(null == moduleVal || null == taskVal) {
				list.add(i+" - ");
			} else {
				list.add(moduleVal+ " " + taskVal);
			}
		}

		JBPopupFactory.getInstance()
				.createPopupChooserBuilder(list)
				.setVisibleRowCount(5)
				.setSelectionMode(SINGLE_SELECTION)
				.setItemChosenCallback(item -> {
					String[] tokens = item.split(" ");
					String configName = GradleTaskAction.getConfigurationName(tokens[0], tokens[1], e);
					System.out.println("selected config: " + configName);
					//TODO get and run this task.

					var runManager = RunManager.getInstance(e.getProject());
					var configurations = runManager.getAllConfigurationsList();
					
					var configsSettings = runManager.getConfigurationSettingsList(GradleExternalTaskConfigurationType.class);
					for(var configSettings : configsSettings) {
						if(configSettings.getName().equals(configName)){
							ProgramRunnerUtil.executeConfiguration(configSettings, ExecutorRegistry.getInstance().getExecutorById("Run"));
							break;
						}
					}
//					for(var config : configurations) {
//						if(config.getName().equals(configName)) {
//							var l = runManager.getConfigurationSettingsList(config.getType());
//							
//							ProgramRunnerUtil.executeConfiguration(runManager.getConfigurationSe, ExecutorRegistry.getInstance().getExecutorById("Run"));
//							break;
//						}
//					}
				})
				.setSelectionMode(0)
				.setTitle("Harpoon Select")
				.createPopup()
				.showInFocusCenter();
	}
}
