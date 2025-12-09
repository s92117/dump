package com.aem.play.core.models;

import com.adobe.cq.export.json.ComponentExporter;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

/**
 * Blog Breadcrumb Sling Model interface.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public interface BlogBreadcrumbModel extends ComponentExporter {

    /**
     * Returns breadcrumb items.
     *
     * @return list of breadcrumb entries
     */
    List<BreadcrumbItem> getBreadcrumbItems();
}
