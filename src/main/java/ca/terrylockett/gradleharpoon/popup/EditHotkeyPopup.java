package ca.terrylockett.gradleharpoon.popup;

import ca.terrylockett.gradleharpoon.configurations.HarpoonConfigurationsUtil;
import ca.terrylockett.gradleharpoon.actiongroups.GradleModuleActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static javax.swing.ListSelectionModel.SINGLE_SELECTION;

public class EditHotkeyPopup extends AnAction {

	@Override
	public void actionPerformed(@NotNull AnActionEvent e) {

		List<String> list = HarpoonConfigurationsUtil.getList(e);

		JBPopupFactory.getInstance()
				.createPopupChooserBuilder(list)
				.setVisibleRowCount(5)
				.setSelectionMode(SINGLE_SELECTION)
				.setItemChosenCallback(item -> JBPopupFactory.getInstance()
						.createActionGroupPopup("Harpoon", getModulesList(item), e.getDataContext(),
								JBPopupFactory.ActionSelectionAid.SPEEDSEARCH, false)
						.showInFocusCenter())
				.setSelectionMode(0)
				.setTitle("Edit Harpoon Hotkeys")
				.createPopup()
				.showInFocusCenter();
	}


	private GradleModuleActionGroup getModulesList(String selectionValue) {
		String indexStr = selectionValue.substring(0, 1);
		int index = Integer.parseInt(indexStr);
		return new GradleModuleActionGroup(index);
	}
}
