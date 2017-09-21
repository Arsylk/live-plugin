package liveplugin.toolwindow.settingsmenu;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import liveplugin.toolwindow.util.DependenciesUtil;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.roots.OrderRootType.CLASSES;
import static com.intellij.openapi.util.Pair.create;
import static java.util.Arrays.asList;
import static liveplugin.toolwindow.settingsmenu.EnableLivePluginAutoComplete.findGroovyJarOn;

public class AddIDEAJarsAsDependencies extends AnAction implements DumbAware {
	private static final String IDEA_JARS_LIBRARY = "IDEA jars";

	@Override public void actionPerformed(@NotNull AnActionEvent event) {
		Project project = event.getProject();
		if (project == null) return;

		if (DependenciesUtil.anyModuleHasLibraryAsDependencyIn(project, IDEA_JARS_LIBRARY)) {
			DependenciesUtil.removeLibraryDependencyFrom(project, IDEA_JARS_LIBRARY);
		} else {
			String ideaJarsPath = PathManager.getHomePath() + "/lib/";
			//noinspection unchecked
			DependenciesUtil.addLibraryDependencyTo(project, IDEA_JARS_LIBRARY, asList(
					create("jar://" + ideaJarsPath + "openapi.jar!/", CLASSES),
					create("jar://" + ideaJarsPath + "idea.jar!/", CLASSES),
					create("jar://" + ideaJarsPath + "idea_rt.jar!/", CLASSES),
					create("jar://" + ideaJarsPath + "annotations.jar!/", CLASSES),
					create("jar://" + ideaJarsPath + "util.jar!/", CLASSES),
					create("jar://" + ideaJarsPath + "extensions.jar!/", CLASSES),
					create("jar://" + ideaJarsPath + findGroovyJarOn(ideaJarsPath) + "!/", CLASSES)
			));
		}
	}

	@Override public void update(@NotNull AnActionEvent event) {
		Project project = event.getProject();
		if (project == null) return;

		if (DependenciesUtil.anyModuleHasLibraryAsDependencyIn(project, IDEA_JARS_LIBRARY)) {
			event.getPresentation().setText("Remove IDEA Jars from Project");
			event.getPresentation().setDescription("Remove IDEA jars dependencies from project");
		} else {
			event.getPresentation().setText("Add IDEA Jars to Project");
			event.getPresentation().setDescription("Add IDEA jars to project as dependencies");
		}
	}
}
