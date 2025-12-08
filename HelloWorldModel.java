package com.aem.play.core.models;

import com.day.cq.wcm.api.Page;
import java.util.ArrayList;
import java.util.List;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;

/**
 * Model responsible for generating breadcrumb items
 * based on the current page path inside AEM.
 */
@Model(
        adaptables = SlingHttpServletRequest.class,
        resourceType = "aem-play/components/helloworld",
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class HelloWorldModel {

    /**
     * The current AEM page injected by Sling.
     */
    @ScriptVariable
    private Page currentPage;

    /**
     * Returns the current AEM page object.
     *
     * @return current page
     */
    public Page getCurrentPage() {
        return currentPage;
    }

    /**
     * Builds and returns a breadcrumb list by extracting
     * segments from the path after the project folder name.
     *
     * Example:
     * <pre>
     * /content/aem-play/us/en/nt → us, en, nt
     * </pre>
     *
     * @return list of breadcrumb items
     */
    public List<BreadcrumbItem> getBreadcrumbItems() {
        final List<BreadcrumbItem> items = new ArrayList<>();

        if (currentPage == null) {
            return items;
        }

        final String path = currentPage.getPath();
        final String[] segments = path.split("/");

        final String projectRoot = "aem-play";

        int startIndex = -1;

        for (int i = 0; i < segments.length; i++) {
            if (projectRoot.equals(segments[i])) {
                startIndex = i;
                break;
            }
        }

        if (startIndex == -1) {
            return items;
        }

        final StringBuilder buildPath = new StringBuilder("/content/")
                .append(projectRoot);

        for (int i = startIndex + 1; i < segments.length; i++) {
            buildPath.append("/").append(segments[i]);
            final String title = segments[i];
            final String link = buildPath.toString() + ".html";
            items.add(new BreadcrumbItem(title, link));
        }

        return items;
    }
}
