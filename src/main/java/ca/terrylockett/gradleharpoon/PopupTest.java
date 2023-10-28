package ca.terrylockett.gradleharpoon;

import ca.terrylockett.gradleharpoon.actiongroups.GradleModuleActionGroup;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.gradle.settings.GradleSettings;

public class PopupTest extends AnAction {
	
	@Override
	public void actionPerformed(@NotNull AnActionEvent e) {
		
		JBPopupFactory.getInstance()
				.createActionGroupPopup("Harpoon", getModulesList(e), e.getDataContext(), JBPopupFactory.ActionSelectionAid.SPEEDSEARCH, false)
				.showInFocusCenter();
	}
	
	
	private GradleModuleActionGroup getModulesList(AnActionEvent event) {
		var project = event.getProject();
		GradleSettings gs = GradleSettings.getInstance(project);

		var list = gs.getLinkedProjectsSettings();
		var config = list.stream().findFirst().get();
		
//		List<GradleModuleActionGroup> moduleActions = new ArrayList<>();
//		for(String module: config.getModules()) {
//			moduleActions.add(new TestAction(module, module, "command"));
//		}
		
		return new GradleModuleActionGroup();
	}
}
