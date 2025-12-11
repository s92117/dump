package com.aem.play.core.models.impl;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.aem.play.core.models.BlogBreadcrumbModel;
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
import com.adobe.cq.wcm.core.components.commons.link.LinkManager;
import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.aem.play.core.models.BreadcrumbItem;
import com.zendesk.core.models.v1.impl.NavigationItemModelImpl;

/**
 * Breadcrumb Component using NavigationItemModelImpl.
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

    public static final String RESOURCE_TYPE =
            "aem-play/components/blogbreadcrumb";

    @ScriptVariable
    private Page currentPage;

    @ScriptVariable
    private LinkManager linkManager;

    private List<NavigationItemModelImpl> breadcrumbItems;

    private static final List<String> ALLOWED_TEMPLATES = List.of(
            "/conf/aem-play/settings/wcm/templates/blogdetail",
            "/conf/aem-play/settings/wcm/templates/blog-category",
            "/conf/aem-play/settings/wcm/templates/blog-landing"
    );

    @PostConstruct
    protected void init() {

        breadcrumbItems = new ArrayList<>();

        if (currentPage == null) {
            return;
        }

        String currentTemplate =
                currentPage.getProperties().get("cq:template", String.class);

        if (currentTemplate == null || !ALLOWED_TEMPLATES.contains(currentTemplate)) {
            return; // Not blog template → return empty
        }

        Page page = currentPage;

        while (page != null && page.getPath().startsWith("/content/aem-play")) {

            String template = page.getProperties().get("cq:template", String.class);

            if (template != null && ALLOWED_TEMPLATES.contains(template)) {

                NavigationItemModelImpl navItem =
                        new NavigationItemModelImpl(
                                page,
                                currentPage.getPath().equals(page.getPath()),
                                linkManager,
                                List.of()
                        );

                breadcrumbItems.add(navItem);
            }

            page = page.getParent();
        }

        Collections.reverse(breadcrumbItems);
    }


    /**
     * Return breadcrumb items coming directly from NavigationItemModelImpl.
     */
    public List<NavigationItemModelImpl> getBreadcrumbItems() {
        return breadcrumbItems;
    }

    @JsonProperty(value = ":type")
    public String getExportedType() {
        return RESOURCE_TYPE;
    }
}
