package com.leforemhe.aem.site.core.services;

import java.util.Calendar;
import java.util.Optional;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.Page;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.sitemap.SitemapException;
import org.apache.sling.sitemap.builder.Sitemap;
import org.apache.sling.sitemap.builder.Url;
import org.apache.sling.sitemap.spi.common.SitemapLinkExternalizer;
import org.apache.sling.sitemap.spi.generator.ResourceTreeSitemapGenerator;
import org.apache.sling.sitemap.spi.generator.SitemapGenerator;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceRanking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = SitemapGenerator.class)
@ServiceRanking(20)
public class ForemSitemapGenerator extends ResourceTreeSitemapGenerator {

    private static final Logger log = LoggerFactory.getLogger(ForemSitemapGenerator.class);

    @Reference
    private GlobalConfigService globalConfigService;

    @Override
    protected void addResource(final String name, final Sitemap sitemap, final Resource resource)
            throws
            SitemapException {

        final Page page = resource.adaptTo(Page.class);
        if (page == null) {
            log.warn("Could not add to sitemap, {} is not a page", resource.getPath());
            return;
        }
        final String location = globalConfigService.getConfig().publicServerURI() + page.getVanityUrl();
        final Url url = sitemap.addUrl(location);
        final Calendar lastmod = Optional.ofNullable(page.getLastModified())
                .orElse(page.getContentResource()
                        .getValueMap()
                        .get(JcrConstants.JCR_CREATED, Calendar.class));
        if (lastmod != null) {
            url.setLastModified(lastmod.toInstant());
        }
    }
}



