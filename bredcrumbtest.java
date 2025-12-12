package com.aem.play.core.models.impl;

import com.aem.play.core.models.BlogBreadcrumbModel;
import com.adobe.cq.wcm.core.components.models.NavigationItem;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
public class BlogBreadcrumbModelImplTest {

    private final AemContext context = new AemContext();

    private BlogBreadcrumbModel model;

    private JsonNode expectedJson;

    @BeforeEach
    void setup() throws Exception {

        context.addModelsForClasses(BlogBreadcrumbModelImpl.class);

        context.load().json("/breadcrumbPages.json",
                "/content/zendesk/amer/en_us/test");

        context.currentPage("/content/zendesk/amer/en_us/test/bc-child/bl-child/bp-child/bd-child2");
        context.currentResource("/content/zendesk/amer/en_us/test/bc-child/bl-child/bp-child/bd-child2/jcr:content");

        // Load expected JSON (the screenshot you shared)
        try (InputStream is = getClass().getResourceAsStream("/expectedBreadcrumb.json")) {
            expectedJson = new ObjectMapper().readTree(is);
        }
    }

    @Test
    void testBreadcrumbItemsAgainstJson() {

        model = context.request().adaptTo(BlogBreadcrumbModelImpl.class);
        assertNotNull(model);

        List<NavigationItem> items = model.getBreadcrumbItems();
        JsonNode expectedItems = expectedJson.get("blogbreadcrumb").get("breadcrumbItems");

        assertEquals(expectedItems.size(), items.size(), "Breadcrumb size mismatch");

        for (int i = 0; i < items.size(); i++) {
            NavigationItem actual = items.get(i);
            JsonNode expected = expectedItems.get(i);

            assertEquals(expected.get("title").asText(), actual.getTitle());
            assertEquals(expected.get("path").asText(), actual.getPath());
            assertEquals(expected.get("current").asBoolean(), actual.isCurrent());

            // Validate URL
            assertEquals(
                    expected.get("link").get("url").asText(),
                    actual.getLink().getUrl()
            );
        }
    }

    @Test
    void testExportedType() {
        model = context.request().adaptTo(BlogBreadcrumbModelImpl.class);
        assertEquals("zendesk/components/blogbreadcrumb/v1/blogbreadcrumb", expectedJson.get(":type").asText());
    }
}
