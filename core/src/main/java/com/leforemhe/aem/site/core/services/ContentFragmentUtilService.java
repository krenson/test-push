package com.leforemhe.aem.site.core.services;

import com.adobe.cq.dam.cfm.ContentFragment;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
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

    public String getCleMeteirFromPage(Page page) {
        var cleMetierProperty = page.getProperties().get(Constants.CLE_METIER);
        if (cleMetierProperty == null) {
            return StringUtils.EMPTY;
        } else {
            return cleMetierProperty.toString();
        }
    }

    /*
     * Returns a job based on JobID
     */
    public Job getJobFromJobID(String jobID) {
        if (!jobID.isEmpty()) {
            return getJobFromJobID(jobID, false, false);
        } else {
            return new Job();
        }
    }

    public Job getJobFromJobID(String jobID, boolean resolveRelatedJobs, boolean resolvePossibleJobs) {
        SearchResult resultPage = createQueryForPageWithLinkedContentFragment(
                Constants.CONTENT_ROOT_PATH, jobID);
        SearchResult result = createQueryForContentFragments(
                contentFragmentConfigService.getConfig().modelMetierPath(), jobID, Job.CONTENT_FRAGMENT_MODEL_CONF);
        if (!result.getHits().isEmpty()) {
            Resource iterationResource = result.getResources().next();
            if (iterationResource != null ) {
                ContentFragment contentFragmentJob = getContentFragmentFromPath(iterationResource.getPath());
                if (contentFragmentJob != null) {
                   String[] tagLabels = {Job.LABELS_KEY, Job.SECTORS_KEY, Job.TREE_STRUCTURE_KEY};
                   String[] tagListIds = resolveTagIds(tagLabels, contentFragmentJob);
                    Job job = new Job(contentFragmentJob, resolveTags(tagListIds), resolveLinkedPage(resultPage), tagListIds);
                    if (resolveRelatedJobs) {
                        String[] relatedJobIds = ContentFragmentUtils.getMultifieldValue(contentFragmentJob, Job.RELATED_JOBS_KEY,
                                String.class);
                        job.setRelatedJobs(resolveJobs(relatedJobIds));
                    }
                    if (resolvePossibleJobs) {
                        String[] possibleJobIds = ContentFragmentUtils.getMultifieldValue(contentFragmentJob, Job.POSSIBLE_JOBS_KEY,
                                String.class);
                        job.setPossibleJobs(resolveJobs(possibleJobIds));
                    }
                    return job;
                }
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
        paramMap.put("p.limit", "-1");
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
            if (tagListIds != null) {
                for (String tagId : tagListIds) {
                    Tag resolvedTag = tagManager.resolve(tagId);
                    if (resolvedTag != null) {
                        tags.add(resolvedTag);
                    }
                }
            }
        }
        return tags;
    }

    private List<Job> resolveJobs(String[] jobIds) {
        List<Job> jobs = new ArrayList<>();
        if (jobIds != null) {
            for (String relatedJobID : jobIds) {
                Job resolvedJob = getJobFromJobID(relatedJobID);
                if (resolvedJob != null) {
                    jobs.add(resolvedJob);
                }
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

    private ContentFragment getContentFragmentFromPath(String path) {
        return getResourceResolver().getResource(path).adaptTo(ContentFragment.class);
    }

    private String[] resolveTagIds(String[] tagLabels, ContentFragment contentFragmentJob) {
        ArrayList<String> tagIds = new ArrayList<>();
        for(String label : tagLabels) {
            String[] ids = ContentFragmentUtils.getMultifieldValue(contentFragmentJob, label,
                    String.class);
            if (ids != null && ids.length > 0) {
                tagIds.addAll(Arrays.asList(ids));
            }
        }
        return tagIds.toArray(new String[0]);
    }
}
