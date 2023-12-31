package ca.terrylockett.gradleharpoon.popup;

import ca.terrylockett.gradleharpoon.configurations.HarpoonConfigurationsUtil;
import ca.terrylockett.gradleharpoon.actiongroups.GradleModuleActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import org.jetbrains.annotations.NotNull;

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
						.createActionGroupPopup("Harpoon", getModulesList(item), e.getDataContext(),
								JBPopupFactory.ActionSelectionAid.SPEEDSEARCH, false)
						.showInFocusCenter())
				.setSelectionMode(SINGLE_SELECTION)
				.setTitle("Edit Harpoon Hotkeys")
				.createPopup()
				.showCenteredInCurrentWindow(e.getProject());
	}


	private GradleModuleActionGroup getModulesList(String selectionValue) {
		String indexStr = selectionValue.substring(0, 1);
		int index = Integer.parseInt(indexStr);
		return new GradleModuleActionGroup(index);
	}

}
