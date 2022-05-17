package com.leforemhe.aem.site.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.jcr.resource.api.JcrResourceConstants;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Sling Model of the first row of the Remarque Layout Component
 */
@Model(adaptables = {Resource.class, SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class RemarqueLayoutFirstRowModel {

    private static final Logger LOG = LoggerFactory.getLogger(RemarqueLayoutFirstRowModel.class);

    @SlingObject
    private ResourceResolver resourceResolver;

    @SlingObject
    private Resource resource;

    private List<RemarqueLayoutColumnModel> columns;

    /**
     * Initiating the columns of the row
     */
    @PostConstruct
    public void init() {
        if (columns == null) {
            columns = initColumns(resource.getChildren());

            if(columns.size() < 4) {
                Map<String, Object> attributes = new HashMap<>();
                attributes.put(JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY, "leforemhe/components/site/remarquelayout/firstrow/column");
                try {
                    resourceResolver.create(resource, getNewColumnName(),attributes);
                    resourceResolver.commit();
                } catch (PersistenceException e) {
                    LOG.error("Something when wrong while creating new column node: {}", e.getMessage());
                }
            }
        }
    }

    private List<RemarqueLayoutColumnModel> initColumns(Iterable<Resource> children){
        List<RemarqueLayoutColumnModel> columnsList = new ArrayList<>();
        for (Resource columnResource : children) {
            RemarqueLayoutColumnModel column = columnResource.adaptTo(RemarqueLayoutColumnModel.class);
            if ((column!=null) && column.isEmpty()) {
                try {
                    resourceResolver.delete(columnResource);
                } catch (PersistenceException e) {
                    LOG.error("Something when wrong while deleting empty column node: {}", e.getMessage());
                }
            } else {
                columnsList.add(column);
            }
        }
        return columnsList;
    }

    public List<RemarqueLayoutColumnModel> getColumns() {
        return new ArrayList<>(columns);
    }

    public List<List<RemarqueLayoutColumnModel>> getGroups() {
        List<List<RemarqueLayoutColumnModel>> result = new ArrayList<>();
        for (int i = 0; i < columns.size(); i += 2) {
            List<RemarqueLayoutColumnModel> item = new ArrayList<>();
            item.add(columns.get(i));
            if (i + 1 < columns.size()) {
                item.add(columns.get(i + 1));
            }
            result.add(item);
        }
        return result;
    }

    public boolean isLastContainerFull() {
        return columns.size() % 2 == 0;
    }

    /**
     * @return a unique name for a new column to be instanciated
     * */
    public String getNewColumnName() {
        List<String> columnNames = new ArrayList<>();
        for(RemarqueLayoutColumnModel column : getColumns()) {
            columnNames.add(column.getName());
        }
        String newName;
        do {
            newName = "column_" + UUID.randomUUID().toString();
        }while (columnNames.contains(newName));
        return newName;
    }

}
