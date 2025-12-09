package com.aem.play.core.models;

/**
 * Represents a single breadcrumb item.
 */
public class BreadcrumbItem {

    private final String title;
    private final String link;

    /**
     * Constructor.
     *
     * @param title breadcrumb display text
     * @param link breadcrumb link
     */
    public BreadcrumbItem(String title, String link) {
        this.title = title;
        this.link = link;
    }

    /**
     * Returns title.
     *
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns link.
     *
     * @return link
     */
    public String getLink() {
        return link;
    }
}
