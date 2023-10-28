package ca.terrylockett.gradleharpoon.action;

import ca.terrylockett.gradleharpoon.configurations.HarpoonConfigurationsUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class GradleTaskAction extends AnAction {

	String module = "";
	String taskName = "";
	int hotkeyIndex = -1;
	
	public GradleTaskAction() {
		super();
	}

	public GradleTaskAction(String module, String taskName, int hotkeyIndex) {
		super(taskName);
		this.module = module;
		this.taskName = taskName;
		this.hotkeyIndex = hotkeyIndex;
	}

	
	@Override
	public void actionPerformed(@NotNull AnActionEvent e) {
		System.out.println("Task Action Triggered");
		if (null == e.getProject()) {
			return;
		}

		HarpoonConfigurationsUtil.addConfigurationToIDE(e, this.module, this.taskName);
		HarpoonConfigurationsUtil.setHarpoonTask(e, taskName, hotkeyIndex);
		
		//TODO run new config
	}
}
