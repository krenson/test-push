package com.leforemhe.aem.site.core.models.horizonemploi.impl;

import com.day.cq.commons.Filter;
import com.day.cq.tagging.Tag;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.Template;
import com.day.cq.wcm.api.WCMException;
import com.leforemhe.aem.site.core.models.Constants;
import com.leforemhe.aem.site.core.models.cfmodels.Job;
import com.leforemhe.aem.site.core.models.cfmodels.JobTag;
import com.leforemhe.aem.site.core.models.horizonemploi.JobPageModel;
import com.leforemhe.aem.site.core.services.ContentFragmentUtilService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.via.ResourceSuperType;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.*;

@Model(adaptables = SlingHttpServletRequest.class,
        adapters = {JobPageModel.class},
        resourceType = JobPageModelImpl.RESOURCE_TYPE,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class JobPageModelImpl implements JobPageModel {
    public static final String RESOURCE_TYPE = "leforemhe/components/site/jobpage";

    @Self
    private SlingHttpServletRequest request;

    @Self
    @Via(type = ResourceSuperType.class)
    private Page currentPage;

    private Job job;

    @PostConstruct
    private void initModel() {
        if (this.currentPage == null) {
            this.currentPage = request.getResource().getParent().adaptTo(Page.class);
        }
    }

    @Inject
    private ContentFragmentUtilService contentFragmentUtilService;

    @Override
    public String getFeaturedImage() {
        return this.currentPage.getContentResource().getChild("cq:featuredimage").getValueMap().get("fileReference",
                String.class);
    }

    @Override
    public List<JobTag> getJobTags() {
        boolean inExperienceFragment = currentPage.getContentResource().getResourceType().equalsIgnoreCase(Constants.EF_RESOURCE_TYPE);
        if (this.job == null && !inExperienceFragment) {
                String cleMetier = currentPage.getProperties().get(Constants.CLE_METIER).toString();
                job = contentFragmentUtilService.getJobFromJobID(cleMetier);
        }
        return job.getLabels();
    }

    @Override
    public String getPath() {
        return currentPage.getPath();
    }
    @Override
    public PageManager getPageManager() {
        return this.currentPage.getPageManager();
    }
    @Override
    public Resource getContentResource() {
        return this.currentPage.getContentResource();
    }
    @Override
    public Resource getContentResource(String relPath) {
        return this.currentPage.getContentResource(relPath);
    }
    @Override
    public Iterator<Page> listChildren() {
        return this.currentPage.listChildren();
    }

    @Override
    public Iterator<Page> listChildren(Filter<Page> filter) {
        return this.currentPage.listChildren(filter);
    }

    @Override
    public Iterator<Page> listChildren(Filter<Page> filter, boolean b) {
        return this.currentPage.listChildren(filter, b);
    }

    @Override
    public boolean hasChild(String name) {
        return this.currentPage.hasChild(name);
    }
    @Override
    public int getDepth() {
        return this.currentPage.getDepth();
    }
    @Override
    public Page getParent() {
        return this.currentPage.getParent();
    }
    @Override
    public Page getParent(int level) {
        return this.currentPage.getParent(level);
    }
    @Override
    public Page getAbsoluteParent(int level) {
        return this.currentPage.getAbsoluteParent(level);
    }
    @Override
    public ValueMap getProperties() {
        return this.currentPage.getProperties();
    }
    @Override
    public ValueMap getProperties(String relPath) {
        return this.currentPage.getProperties(relPath);
    }
    @Override
    public String getName() {
        return this.currentPage.getName();
    }
    @Override
    public String getTitle() {
        return this.currentPage.getTitle();
    }
    @Override
    public String getPageTitle() {
        return this.currentPage.getPageTitle();
    }
    @Override
    public String getNavigationTitle() {
        return this.currentPage.getNavigationTitle();
    }
    @Override
    public boolean isHideInNav() {
        return this.currentPage.isHideInNav();
    }
    @Override
    public boolean hasContent() {
        return this.currentPage.hasContent();
    }
    @Override
    public boolean isValid() {
        return this.currentPage.isValid();
    }
    @Override
    public long timeUntilValid() {
        return this.currentPage.timeUntilValid();
    }
    @Override
    public Calendar getOnTime() {
        return this.currentPage.getOnTime();
    }
    @Override
    public Calendar getOffTime() {
        return this.currentPage.getOffTime();
    }
    @Override
    public Calendar getDeleted() {
        return this.currentPage.getDeleted();
    }
    @Override
    public String getDeletedBy() {
        return this.currentPage.getDeletedBy();
    }
    @Override
    public String getLastModifiedBy() {
        return this.currentPage.getLastModifiedBy();
    }
    @Override
    public Calendar getLastModified() {
        return this.currentPage.getLastModified();
    }
    @Override
    public String getVanityUrl() {
        return this.currentPage.getVanityUrl();
    }
    @Override
    public Tag[] getTags() {
        return this.currentPage.getTags();
    }

    @Override
    public void lock() throws WCMException {
        this.currentPage.lock();
    }
    @Override
    public boolean isLocked() {
        return this.currentPage.isLocked();
    }
    @Override
    public String getLockOwner() {
        return this.currentPage.getLockOwner();
    }
    @Override
    public boolean canUnlock() {
        return this.currentPage.canUnlock();
    }
    @Override
    public void unlock() throws WCMException {
        this.currentPage.unlock();
    }
    @Override
    public Template getTemplate() {
        return this.currentPage.getTemplate();
    }
    @Override
    public Locale getLanguage(boolean ignoreContent) {
        return this.currentPage.getLanguage(ignoreContent);
    }
    @Override
    public Locale getLanguage() {
        return this.currentPage.getLanguage();
    }

    @Override
    public String getDescription() {
        return this.currentPage.getDescription();
    }

    @Override
    public <AdapterType> AdapterType adaptTo(Class<AdapterType> type) {
        if (currentPage != null) {
            return this.currentPage.adaptTo(type);
        } else {
            return null;
        }
    }
}