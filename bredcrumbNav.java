@Test
void testValidBreadcrumb() {
    context.currentResource("/apps/test/blogbreadcrumb");

    BlogBreadcrumbModelImpl model =
        context.request().adaptTo(BlogBreadcrumbModelImpl.class);

    List<BreadcrumbItem> items = List.of(
        new BreadcrumbItem("test", "/content/zendesk/amer/en_us/test.html"),
        new BreadcrumbItem("bc-child", "/content/zendesk/amer/en_us/test/bc-child.html"),
        new BreadcrumbItem("bl-child", "/content/zendesk/amer/en_us/test/bc-child/bl-child.html"),
        new BreadcrumbItem("bd-child2", "/content/zendesk/amer/en_us/test/bc-child/bl-child/bp-child/bd-child2.html")
    );

    // Inject into model
    Field field = BlogBreadcrumbModelImpl.class.getDeclaredField("breadcrumbItems");
    field.setAccessible(true);
    field.set(model, items);

    assertEquals(4, model.getBreadcrumbItems().size());
}
