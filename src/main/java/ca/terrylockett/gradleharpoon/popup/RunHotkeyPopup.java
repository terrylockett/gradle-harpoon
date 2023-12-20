package ca.terrylockett.gradleharpoon.popup;

import ca.terrylockett.gradleharpoon.configurations.HarpoonConfigurationsUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.util.Consumer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static ca.terrylockett.gradleharpoon.configurations.HarpoonConfigurationsUtil.NUMBER_OF_BOOKMARKS;
import static javax.swing.ListSelectionModel.SINGLE_SELECTION;

public class RunHotkeyPopup extends AnAction {

	@Override
	public void actionPerformed(@NotNull AnActionEvent e) {
		List<String> list = HarpoonConfigurationsUtil.getList(e);

		JBPopupFactory.getInstance()
				.createPopupChooserBuilder(list)
				.setVisibleRowCount(NUMBER_OF_BOOKMARKS)
				.setSelectionMode(SINGLE_SELECTION)
				.setItemChosenCallback(getCallBack(e))
				.setSelectionMode(SINGLE_SELECTION)
				.setTitle("Harpoon Select")
				.createPopup()
				.showCenteredInCurrentWindow(e.getProject());
	}


	private Consumer<String> getCallBack(AnActionEvent e) {
		return selectedItemValue -> {
			String[] entryNameTokens = selectedItemValue.split("\\s+");
			boolean isEmptySelection = entryNameTokens.length < 3;
			if (isEmptySelection) {
				return;
			}
			String configName = getConfigurationName(entryNameTokens);
			HarpoonConfigurationsUtil.runConfig(configName, e);
		};
	}


	private static String getConfigurationName(String[] entryNameTokens) {
		return entryNameTokens[1] + " " + entryNameTokens[2];
	}


}
