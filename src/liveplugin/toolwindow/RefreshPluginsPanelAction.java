package liveplugin.toolwindow;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.newvfs.RefreshQueue;
import liveplugin.Icons;
import liveplugin.LivePluginAppComponent;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("ComponentNotRegistered")
public class RefreshPluginsPanelAction extends AnAction implements DumbAware {

	public RefreshPluginsPanelAction() {
		super("Refresh Plugins Panel", "Refresh Plugins Panel", Icons.refreshPluginsPanelIcon);
	}

	public static void refreshPluginTree() {
		ApplicationManager.getApplication().runWriteAction(() -> {
			VirtualFile pluginsRoot = VirtualFileManager.getInstance().findFileByUrl("file://" + LivePluginAppComponent.pluginsRootPath());
			if (pluginsRoot == null) return;

			RefreshQueue.getInstance().refresh(false, true, PluginToolWindowManager::reloadPluginTreesInAllProjects, pluginsRoot);
		});
	}

	@Override public void actionPerformed(@Nullable AnActionEvent e) {
		refreshPluginTree();
	}
}
