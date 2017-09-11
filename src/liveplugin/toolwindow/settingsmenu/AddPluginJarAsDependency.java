package liveplugin.toolwindow.settingsmenu;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.util.PathUtil;
import liveplugin.LivePluginAppComponent;
import liveplugin.toolwindow.util.DependenciesUtil;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.roots.OrderRootType.CLASSES;
import static com.intellij.openapi.roots.OrderRootType.SOURCES;
import static com.intellij.openapi.util.Pair.create;
import static java.util.Arrays.asList;

public class AddPluginJarAsDependency extends AnAction implements DumbAware {
	private static final String LIVE_PLUGIN_LIBRARY = "LivePlugin";

	@Override public void actionPerformed(@NotNull AnActionEvent event) {
		Project project = event.getProject();
		if (project == null) return;

		if (DependenciesUtil.anyModuleHasLibraryAsDependencyIn(project, LIVE_PLUGIN_LIBRARY)) {
			DependenciesUtil.removeLibraryDependencyFrom(project, LIVE_PLUGIN_LIBRARY);
		} else {
			DependenciesUtil.addLibraryDependencyTo(project, LIVE_PLUGIN_LIBRARY, asList(
					create(findPathToMyClasses(), CLASSES),
					create(findPathToMyClasses() + "src/", SOURCES)
			));
		}
	}

	private static String findPathToMyClasses() {
		String pathToMyClasses = PathUtil.getJarPathForClass(LivePluginAppComponent.class);
		// need trailing "/" because folder dependency doesn't work without it
		if (pathToMyClasses.endsWith(".jar")) {
			pathToMyClasses = "jar://" + pathToMyClasses + "!/";
		} else {
			pathToMyClasses = "file://" + pathToMyClasses + "/";
		}
		return pathToMyClasses;
	}

	@Override public void update(@NotNull AnActionEvent event) {
		Project project = event.getProject();
		if (project == null) return;

		if (DependenciesUtil.anyModuleHasLibraryAsDependencyIn(project, LIVE_PLUGIN_LIBRARY)) {
			event.getPresentation().setText("Remove LivePlugin Jar from Project");
			event.getPresentation().setDescription(
					"Remove LivePlugin jar from project dependencies. This will enable auto-complete and other IDE features for IntelliJ classes.");
		} else {
			event.getPresentation().setText("Add LivePlugin Jar to Project");
			event.getPresentation().setDescription(
					"Add LivePlugin jar to project dependencies. This will enable auto-complete and other IDE features for PluginUtil.");
		}
	}

}
