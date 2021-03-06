/**
 * 
 */
package org.vaadin.bread.example;

import org.vaadin.bread.example.base.repo.GroupRepository;
import org.vaadin.bread.example.base.repo.JPAService;
import org.vaadin.bread.example.model.Group;
import org.vaadin.bread.example.model.GroupFilter;
import org.vaadin.bread.ui.crud.CrudListener;
import org.vaadin.bread.ui.crud.FilterOperation;
import org.vaadin.bread.ui.crud.impl.GridCrud;
import org.vaadin.bread.ui.form.impl.form.factory.FormFactoryBuilder;
import org.vaadin.bread.ui.form.impl.form.factory.GridLayoutFormFactory;
import org.vaadin.bread.ui.layout.impl.HorizontalSplitCrudLayout;

import com.vaadin.data.provider.CallbackDataProvider;
import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.ui.Component;

/**
 * @author Dmitrij Colautti
 *
 */
public class GroupCrud extends GridCrud<Group> implements CrudListener<Group> {

    private GroupFilter filterBean = new GroupFilter();
    
	public GroupCrud() {
		super(Group.class, new HorizontalSplitCrudLayout());
		
		// build filter
        GridLayoutFormFactory<GroupFilter> filterFormFactory = 
        		new FormFactoryBuilder<GroupFilter, GridLayoutFormFactory<GroupFilter>>()
        		.gridLayoutFilter(GroupFilter.class, 4, 2)
        		.build();
        
        filterFormFactory.getConfiguration(FilterOperation.APPLY).setOperationActionListener(FilterOperation.APPLY, (e)-> {
        	refreshGrid();
        });
        filterFormFactory.getConfiguration(FilterOperation.APPLY).setOperationActionListener(FilterOperation.EMPTY, (e)-> {
        	filterBean.clear();
        	filterFormFactory.getBinder().readBean(filterBean);
        	refreshGrid();
        });
        filterFormFactory.buildSensitiveDefaults();
        
        Component filterForm = filterFormFactory.buildNewForm(FilterOperation.APPLY
        		, filterBean, false);
        setCrudListener(this);
        getCrudLayout().addFilterComponent(filterForm);

        // build form
        GridLayoutFormFactory<Group> formFactory = new FormFactoryBuilder<Group, GridLayoutFormFactory<Group>>()
        		.gridLayoutBread(Group.class, 2, 2).build();
        setCrudFormFactory(formFactory);

        formFactory.setJpaTypeForJpaValidation(JPAService.getFactory().getMetamodel().managedType(Group.class));
        formFactory.buildSensitiveDefaults();

        setRowCountCaption("%d group(s) found");

	};
	

    @Override
    public Group add(Group group) {
    	GroupRepository.save(group);
        return group;
    }

    @Override
    public Group update(Group group) {
        return GroupRepository.save(group);
    }

    @Override
    public void delete(Group group) {
    	GroupRepository.delete(group);
    }

    @Override
    public ConfigurableFilterDataProvider<Group, Void, GroupFilter> getDataProvider() {
    	
    	CallbackDataProvider<Group, GroupFilter> dp = new CallbackDataProvider<Group, GroupFilter>(
    			q -> {
    				return GroupRepository.findAll(q.getFilter().orElse(null), q.getOffset(), q.getLimit()).stream();
    			}
    			, q -> GroupRepository.findAll(q.getFilter().orElse(null), q.getOffset(), q.getLimit()).size()
    			);
    	
    	ConfigurableFilterDataProvider<Group, Void, GroupFilter> everythingConfigurable = dp.withConfigurableFilter();
    	everythingConfigurable.setFilter(filterBean);
    	
    	return everythingConfigurable;
    }
}
