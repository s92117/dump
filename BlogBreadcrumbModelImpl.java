package com.aem.play.core.models.impl;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.aem.play.core.models.BlogBreadcrumbModel;
import com.aem.play.core.models.BreadcrumbItem;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import com.day.cq.wcm.api.Page;

/**
 * Implementation for Blog Breadcrumb Component.
 */
@Model(
        adaptables = {SlingHttpServletRequest.class},
        adapters = {BlogBreadcrumbModel.class, ComponentExporter.class},
        resourceType = {BlogBreadcrumbModelImpl.RESOURCE_TYPE},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter(
        name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
        extensions = ExporterConstants.SLING_MODEL_EXTENSION
)
@JsonSerialize(as = BlogBreadcrumbModelImpl.class)
public class BlogBreadcrumbModelImpl implements BlogBreadcrumbModel {

    /**
     * Resource type of the component.
     */
    public static final String RESOURCE_TYPE =
            "aem-play/components/blogbreadcrumb";

    @ScriptVariable
    private Page currentPage;

    private List<BreadcrumbItem> breadcrumbItems;

    /**
     * Allowed templates for breadcrumb.
     */
    private static final List<String> ALLOWED_TEMPLATES = List.of(
            "/conf/aem-play/settings/wcm/templates/blogdetail",
            "/conf/aem-play/settings/wcm/templates/blog-category",
            "/conf/aem-play/settings/wcm/templates/blog-landing"
    );

    /**
     * Initializes breadcrumb logic.
     */
    @PostConstruct
    protected void init() {
        breadcrumbItems = new ArrayList<>();

        if (currentPage == null) {
            return;
        }

        // Verify current page template — if NO match, STOP
        String currentTemplate =
                currentPage.getProperties().get("cq:template", String.class);

        if (currentTemplate == null || !ALLOWED_TEMPLATES.contains(currentTemplate)) {
            return; // return empty breadcrumb
        }

        // Continue traversal up the tree
        Page page = currentPage;

        while (page != null && page.getPath().startsWith("/content/aem-play")) {

            String template = page.getProperties().get("cq:template", String.class);

            if (template != null && ALLOWED_TEMPLATES.contains(template)) {

                String title =
                        page.getTitle() != null ? page.getTitle() : page.getName();

                String link = page.getPath() + ".html";

                breadcrumbItems.add(new BreadcrumbItem(title, link));
            }

            page = page.getParent();
        }

        // Reverse so breadcrumb goes: root → child → current
        Collections.reverse(breadcrumbItems);
    }

    /**
     * Returns breadcrumb items.
     *
     * @return list of breadcrumb entries
     */
    @Override
    public List<BreadcrumbItem> getBreadcrumbItems() {
        return breadcrumbItems;
    }

    /**
     * Returns exported type for SPA editor.
     *
     * @return resource type
     */
    @JsonProperty(value = ":type")
    public String getExportedType() {
        return RESOURCE_TYPE;
    }
}
