@PostConstruct
protected void init() {

    if (!isBlogDetail(currentPage)) {
        return;
    }

    Page parent = currentPage.getParent();

    boolean parentIsBlogDetail = isBlogDetail(parent);

    // Collect Blog Detail children of current page
    List<NavigationItem> currentPageChildren = buildChildren(currentPage);

    // CASE 1: current page has Blog Detail children
    if (!currentPageChildren.isEmpty()) {
        sideNavigationItem = new NavigationItemModelImpl(
                currentPage,
                true,
                linkManager,
                currentPageChildren
        );
        return;
    }

    // CASE 2: parent is Blog Detail (siblings navigation)
    if (parentIsBlogDetail) {
        List<NavigationItem> siblingItems = buildChildren(parent);

        // Defensive: if parent has only one child (current page), do not render
        if (siblingItems.size() <= 1) {
            return;
        }

        sideNavigationItem = new NavigationItemModelImpl(
                parent,
                false,
                linkManager,
                siblingItems
        );
    }
}
