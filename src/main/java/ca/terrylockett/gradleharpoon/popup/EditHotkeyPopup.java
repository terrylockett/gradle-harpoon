package ca.terrylockett.gradleharpoon.popup;

import ca.terrylockett.gradleharpoon.actiongroups.GradleTasksActionGroup;
import ca.terrylockett.gradleharpoon.configurations.HarpoonConfigurationsUtil;
import ca.terrylockett.gradleharpoon.actiongroups.GradleModuleActionGroup;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import static ca.terrylockett.gradleharpoon.configurations.HarpoonConfigurationsUtil.NUMBER_OF_BOOKMARKS;
import static javax.swing.ListSelectionModel.SINGLE_SELECTION;

public class EditHotkeyPopup extends AnAction {

	@Override
	public void actionPerformed(@NotNull AnActionEvent e) {

		List<String> harpoonEntries = HarpoonConfigurationsUtil.getList(e);

		JBPopupFactory.getInstance()
				.createPopupChooserBuilder(harpoonEntries)
				.setVisibleRowCount(NUMBER_OF_BOOKMARKS)
				.setSelectionMode(SINGLE_SELECTION)
				.setItemChosenCallback(item -> JBPopupFactory.getInstance()
						.createActionGroupPopup("Harpoon", getNextPopupEntries(item, e), e.getDataContext(),
								JBPopupFactory.ActionSelectionAid.SPEEDSEARCH, false)
						.showInFocusCenter())
				.setSelectionMode(SINGLE_SELECTION)
				.setTitle("Edit Harpoon Hotkeys")
				.createPopup()
				.showInFocusCenter();
	}


	private ActionGroup getNextPopupEntries(String selectionValue, AnActionEvent e) {
		String indexStr = selectionValue.substring(0, 1);
		int index = Integer.parseInt(indexStr);
		GradleModuleActionGroup modulesGroup = new GradleModuleActionGroup(index);

		boolean isMultiModuleProject = modulesGroup.getChildren(e).length > 1;
		if (isMultiModuleProject) {
			return new GradleModuleActionGroup(index);
		}
		
		String projectPath = GradleModuleActionGroup.getModulePaths(e).get(0);
		HarpoonConfigurationsUtil.setHarpoonModule(e, projectPath, index);
		return new GradleTasksActionGroup(projectPath, index);
	}

}
