package com.aem.play.core.models.impl;

import com.aem.play.core.models.BlogBreadcrumbModel;
import com.aem.play.core.models.BreadcrumbItem;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class BlogBreadcrumbModelImplTest {

    private final AemContext context = new AemContext();

    @BeforeEach
    void setup() {
        context.addModelsForClasses(BlogBreadcrumbModelImpl.class);

        // Load updated JSON
        context.load().json("/blogBreadcrumbTest.json",
                "/content/zendesk/amer/en_us/test/jcr:content/root/blogbreadcrumb");
    }

    @Test
    void testValidBreadcrumb() {

        context.currentResource(
                "/content/zendesk/amer/en_us/test/jcr:content/root/blogbreadcrumb"
        );

        BlogBreadcrumbModel model =
                context.request().adaptTo(BlogBreadcrumbModel.class);

        assertNotNull(model);

        List<BreadcrumbItem> items = model.getBreadcrumbItems();
        assertEquals(4, items.size());

        // Item 1
        assertEquals("test", items.get(0).getTitle());
        assertEquals("/content/zendesk/amer/en_us/test.html", items.get(0).getLink());

        // Item 2
        assertEquals("bc-child", items.get(1).getTitle());
        assertEquals("/content/zendesk/amer/en_us/test/bc-child.html", items.get(1).getLink());

        // Item 3
        assertEquals("bl-child", items.get(2).getTitle());
        assertEquals("/content/zendesk/amer/en_us/test/bc-child/bl-child.html", items.get(2).getLink());

        // Item 4
        assertEquals("bd-child2", items.get(3).getTitle());
        assertEquals("/content/zendesk/amer/en_us/test/bc-child/bl-child/bp-child/bd-child2.html",
                     items.get(3).getLink());
    }

    @Test
    void testExportedType() {

        context.currentResource(
                "/content/zendesk/amer/en_us/test/jcr:content/root/blogbreadcrumb"
        );

        BlogBreadcrumbModelImpl model =
                context.request().adaptTo(BlogBreadcrumbModelImpl.class);

        assertNotNull(model);
        assertEquals("zendesk/components/blogbreadcrumb/v1/blogbreadcrumb",
                     model.getExportedType());
    }

    @Test
    void testNullCurrentPageHandled() {
        BlogBreadcrumbModelImpl model = new BlogBreadcrumbModelImpl();
        assertNull(model.getBreadcrumbItems());
    }
}
