package ca.terrylockett.gradleharpoon;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.module.Module;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.gradle.service.task.GradleTaskManager;
import org.jetbrains.plugins.gradle.settings.GradleExtensionsSettings;
import org.jetbrains.plugins.gradle.settings.GradleSettings;
import org.jetbrains.plugins.gradle.util.GradleCommandLine;
import org.jetbrains.plugins.gradle.util.GradleModuleData;
import org.jetbrains.plugins.gradle.util.GradleTaskData;

import java.util.Arrays;

public class GetGradleProjectsTest extends AnAction {
	
	@Override
	public void actionPerformed(@NotNull AnActionEvent e) {
		
		if(null == e.getProject()) {
			System.err.println("Project was null!");
			return;
		}

		var project = e.getProject();
		GradleSettings gs = GradleSettings.getInstance(project);
		
		var list = gs.getLinkedProjectsSettings();
		var config = list.stream().findFirst().get();
		
		String basePath = gs.getProject().getBasePath();
		String baseName = Arrays.stream(basePath.split("/")).reduce((first, second) -> second).orElse(null);
		config.getModules();
		
		for(String module : config.getModules()) {
			System.out.println("module: " + module);
		}

		
		//convert module path to moduleTask name
		for(String module : config.getModules()) {
			String path = config.getExternalProjectPath();
			
			if(module.equals(path)) {
				System.out.println(":");
				continue;
			}
			System.out.println("moduleTaskName: " + module.replace(path, "").replace("/", ":"));
		}
		

		var ges = GradleExtensionsSettings.getInstance(project);
		var rp = ges.getRootGradleProject(config.getExternalProjectPath());
		var ex = rp.extensions.get(":day06");
		var ks = ex.tasksMap.keySet();
		for(var key : ks) {
			System.out.println("key: " + key);
		}
		
//		GradleModuleData gmd = new GradleModuleData(config.);

		
		
	}
}
