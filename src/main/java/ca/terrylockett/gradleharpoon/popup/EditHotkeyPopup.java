package ca.terrylockett.gradleharpoon.popup;

import ca.terrylockett.gradleharpoon.actiongroups.GradleModuleActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.JBPopupListener;
import com.intellij.openapi.ui.popup.LightweightWindowEvent;
import com.intellij.ui.ColoredListCellRenderer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.gradle.settings.GradleSettings;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.ListSelectionModel.SINGLE_SELECTION;

public class EditHotkeyPopup extends AnAction {
	
	@Override
	public void actionPerformed(@NotNull AnActionEvent e) {

//		JBPopupFactory.getInstance()
//				.createActionGroupPopup("Harpoon", getModulesList(e), e.getDataContext(), JBPopupFactory.ActionSelectionAid.NUMBERING, false)
//				.showInFocusCenter();

		List<String> list = new ArrayList<>();
		list.add("1. asd");
		list.add("2. dog");
		list.add("3. dog");
		list.add("4. dog");
		list.add("5. dog");
		
		JBPopupFactory.getInstance()
				.createPopupChooserBuilder(list) 
				.setVisibleRowCount(5)
				.setSelectionMode(SINGLE_SELECTION)
				.setItemChosenCallback(item -> {
					JBPopupFactory.getInstance()
							.createActionGroupPopup("Harpoon", getModulesList(e, item), e.getDataContext(), JBPopupFactory.ActionSelectionAid.SPEEDSEARCH, false)
							.showInFocusCenter();
				})
				.setSelectionMode(0)
				.setTitle("Edit Harpoon Hotkeys")
				.createPopup()
				.showInFocusCenter();
	}

	private GradleModuleActionGroup getModulesList(AnActionEvent event, String selectionValue) {
		
		String indexStr = selectionValue.substring(0,1);
		int index = Integer.parseInt(indexStr);
		
		
//		var project = event.getProject();
//		GradleSettings gs = GradleSettings.getInstance(project);
//
//		var list = gs.getLinkedProjectsSettings();
//		var config = list.stream().findFirst().get();

//		List<GradleModuleActionGroup> moduleActions = new ArrayList<>();
//		for(String module: config.getModules()) {
//			moduleActions.add(new TestAction(module, module, "command"));
//		}

		return new GradleModuleActionGroup(index);
	}
	
	
}
