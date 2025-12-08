package com.aem.play.core.models;

/**
 * Represents a single breadcrumb navigation item.
 */
public class BreadcrumbItem {

    /**
     * The display title of the breadcrumb segment.
     */
    private final String title;

    /**
     * The generated link for the breadcrumb segment.
     */
    private final String link;

    /**
     * Constructor for {@code BreadcrumbItem}.
     *
     * @param title breadcrumb display text
     * @param link  URL for breadcrumb target
     */
    public BreadcrumbItem(final String title, final String link) {
        this.title = title;
        this.link = link;
    }

    /**
     * Returns the breadcrumb segment title.
     *
     * @return title string
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the breadcrumb link.
     *
     * @return link string
     */
    public String getLink() {
        return link;
    }
}
