 @Self
    private SlingHttpServletRequest request;

    private LinkManager linkManager;

    private List<BreadcrumbItem> breadcrumbItems;

    private Page currentPage;

    private static final List<String> ALLOWED_TEMPLATES = List.of(
            "/conf/aem-play/settings/wcm/templates/page-content/page"
    );

    /**
     * Initializes breadcrumb items for the current page.
     */
    @PostConstruct
    protected void init() {
        linkManager = request.adaptTo(LinkManager.class);

        breadcrumbItems = new ArrayList<>();

        if (currentPage == null) {
            return;
        }

        String currentTemplate = currentPage.getProperties().get("cq:template", String.class);
        if (currentTemplate == null || !ALLOWED_TEMPLATES.contains(currentTemplate)) {
            return;
        }

        Page page = currentPage;
        while (page != null && page.getPath().startsWith("/content/aem-play")) {

            String template = page.getProperties().get("cq:template", String.class);
            if (template != null && ALLOWED_TEMPLATES.contains(template)) {

                NavigationItemModel navItem = new NavigationItemModelImpl(page, page.equals(currentPage),
                        linkManager, Collections.emptyList());

                String title = Optional.ofNullable(navItem.getTitle()).orElse(page.getName());
                Link<Page> link = navItem.getLink();
                String url = (link != null && link.getURL() != null) ? link.getURL() : page.getPath();

                breadcrumbItems.add(new BreadcrumbItem(title, url));
            }

            page = page.getParent();
        }

        Collections.reverse(breadcrumbItems);
    }

    /**
     * Returns the list of breadcrumb items.
     *
     * @return breadcrumb items
     */
    public List<BreadcrumbItem> getBreadcrumbItems() {
        return breadcrumbItems;
    }
}
