package ca.terrylockett.gradleharpoon.action;

import ca.terrylockett.gradleharpoon.configurations.HarpoonConfigurationsUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class ResetHarpoonConfigs extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        HarpoonConfigurationsUtil.resetConfigurations(e);
    }
}
