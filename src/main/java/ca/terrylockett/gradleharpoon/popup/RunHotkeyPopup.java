package ca.terrylockett.gradleharpoon.popup;

import ca.terrylockett.gradleharpoon.configurations.HarpoonConfigurationsUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.util.Consumer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static javax.swing.ListSelectionModel.SINGLE_SELECTION;

public class RunHotkeyPopup extends AnAction {

	@Override
	public void actionPerformed(@NotNull AnActionEvent e) {
		List<String> list = HarpoonConfigurationsUtil.getList(e);

		JBPopupFactory.getInstance()
				.createPopupChooserBuilder(list)
				.setVisibleRowCount(5)
				.setSelectionMode(SINGLE_SELECTION)
				.setItemChosenCallback(getCallBack(e))
				.setSelectionMode(0)
				.setTitle("Harpoon Select")
				.createPopup()
				.showInFocusCenter();
	}


	private Consumer<String> getCallBack(AnActionEvent e) {
		return selectedItemValue -> {
			String configName = getConfigurationName(selectedItemValue, e);
			HarpoonConfigurationsUtil.runConfig(configName, e);
		};
	}
	
	
	public static String getConfigurationName(String gradlePath, AnActionEvent e) {
		String[] tokens = gradlePath.split("\\s+");
		return tokens[1] + " " + tokens[2];
	}
}
