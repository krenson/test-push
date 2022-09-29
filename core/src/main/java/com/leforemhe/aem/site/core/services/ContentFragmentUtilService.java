package com.leforemhe.aem.site.core.services;

import com.adobe.cq.dam.cfm.ContentFragment;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.leforemhe.aem.site.core.models.Constants;
import com.leforemhe.aem.site.core.models.ModelUtils;
import com.leforemhe.aem.site.core.models.cfmodels.Activity;
import com.leforemhe.aem.site.core.models.cfmodels.Job;
import com.leforemhe.aem.site.core.models.utils.ContentFragmentUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

/**
 * Content fragment utility service
 */
@Component(service = ContentFragmentUtilService.class)
public class ContentFragmentUtilService {

    public static final String ELEMENT_TITLE = "titre";
    public static final String ELEMENT_SYNONYMES = "synonymes";
    public static final String ELEMENT_ACCROCHE = "description";
    public static final String ELEMENT_ETIQUETTES = "etiquettes";

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Reference
    private GlobalConfigService globalConfigService;

    @Reference
    private ContentFragmentConfigService contentFragmentConfigService;

    private static final Logger LOG = LoggerFactory.getLogger(ContentFragmentUtilService.class);

    /*
     * Returns a list of activities based on JobID
     */
    public List<Activity> getActivitiesFromJobID(String jobID) {
        SearchResult result = createQueryForContentFragments(
                contentFragmentConfigService.getConfig().modelActivitesPath(), jobID,
                Activity.CONTENT_FRAGMENT_MODEL_CONF);
        if (!result.getHits().isEmpty()) {
            List<Activity> activities = new ArrayList<>();
            Iterator<Resource> iterator = result.getResources();
            while (iterator.hasNext()) {
                ContentFragment contentFragment = iterator.next().adaptTo(ContentFragment.class);
                if (contentFragment != null) {
                    activities.add(new Activity(contentFragment));
                }
            }
            return activities;
        }
        return Collections.EMPTY_LIST;
    }

    /*
     * Returns a job based on JobID
     */
    public Job getJobFromJobID(String jobID) {
        return getJobFromJobID(jobID, true);
    }

    public Job getJobFromJobID(String jobID, boolean resolveRelatedJobs) {
        SearchResult resultPage = createQueryForPageWithLinkedContentFragment(
                Constants.CONTENT_ROOT_PATH, jobID);
        SearchResult result = createQueryForContentFragments(
                contentFragmentConfigService.getConfig().modelMetierPath(), jobID, Job.CONTENT_FRAGMENT_MODEL_CONF);
        if (!result.getHits().isEmpty()) {
            ContentFragment contentFragmentJob = result.getResources().next().adaptTo(ContentFragment.class);
            if (contentFragmentJob != null) {
                String[] tagListIds = ContentFragmentUtils.getMultifieldValue(contentFragmentJob, Job.LABELS_KEY,
                        String.class);
                Job job = new Job(contentFragmentJob, resolveTags(tagListIds), resolveLinkedPage(resultPage));
                if (resolveRelatedJobs) {
                    String[] relatedJobIds = ContentFragmentUtils.getMultifieldValue(contentFragmentJob, Job.RELATED_JOBS_KEY,
                            String.class);
                    job.setRelatedJobs(resolveJobs(relatedJobIds));
                }
                return job;
            }
        }
        return null;
    }

    /*
     * Created query to search for Content Fragments based on JobID
     */
    private SearchResult createQueryForContentFragments(String path, String jobID, String contentFragmentType) {
        Map<String, String> paramMap = new HashMap();
        paramMap.put("path", path);
        paramMap.put("type", "dam:Asset");
        paramMap.put("1_property.property", "jcr:content/data/cq:model");
        paramMap.put("1_property.value", contentFragmentType);
        paramMap.put("2_property.property", "jcr:content/data/master/" + Activity.CODE_METIER_KEY);
        paramMap.put("2_property.value", jobID);

        ResourceResolver resourceResolver = getResourceResolver();
        QueryBuilder builder = resourceResolver.adaptTo(QueryBuilder.class);

        Query query = builder.createQuery(PredicateGroup.create(paramMap), resourceResolver.adaptTo(Session.class));
        return query.getResult();
    }

    /*
     * Created query to search for Page with a linked content fragment through a JobID
     */
    private SearchResult createQueryForPageWithLinkedContentFragment(String path, String jobID) {
        Map<String, String> paramMap = new HashMap();
        paramMap.put("path", path);
        paramMap.put("type", "cq:Page");
        paramMap.put("1_property", "jcr:content/clemetier");
        paramMap.put("1_property.value", jobID);

        ResourceResolver resourceResolver = getResourceResolver();
        QueryBuilder builder = resourceResolver.adaptTo(QueryBuilder.class);

        Query query = builder.createQuery(PredicateGroup.create(paramMap), resourceResolver.adaptTo(Session.class));
        return query.getResult();
    }

    public List<Tag> resolveTags(String[] tagListIds) {
        ResourceResolver resourceResolver = getResourceResolver();
        List<Tag> tags = new ArrayList<>();
        if (resourceResolver != null) {
            TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
            for (String tagId : tagListIds) {
                Tag resolvedTag = tagManager.resolve(tagId);
                if (resolvedTag != null) {
                    tags.add(resolvedTag);
                }
            }
        }
        return tags;
    }

    private List<Job> resolveJobs(String[] jobIds) {
        List<Job> jobs = new ArrayList<>();
        for (String relatedJobID : jobIds) {
            Job resolvedJob = getJobFromJobID(relatedJobID, false);
            if (resolvedJob != null) {
                jobs.add(resolvedJob);
            }
        }
        return jobs;
    }

    private String resolveLinkedPage(SearchResult searchResult) {
        if (!searchResult.getHits().isEmpty()) {
            try {
                return ModelUtils.getVanityOfPageIfExists(searchResult.getHits().get(0).getPath(), getResourceResolver());
            } catch (RepositoryException e) {
                e.printStackTrace();
                return StringUtils.EMPTY;
            }
        } else {
            return StringUtils.EMPTY;
        }
    }

    private ResourceResolver getResourceResolver() {
        return ServiceUtils.getResourceResolver(resolverFactory,
                globalConfigService.getConfig().systemUser());
    }
}
