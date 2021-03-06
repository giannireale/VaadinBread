package org.vaadin.bread.ui.form;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.vaadin.bread.data.JpaValidationBinder;
import org.vaadin.bread.data.converter.StringToByteConverter;
import org.vaadin.bread.data.converter.StringToCharacterConverter;
import org.vaadin.bread.ui.crud.OperationAction;
import org.vaadin.bread.ui.crud.OperationException;
import org.vaadin.bread.ui.crud.OperationMode;
import org.vaadin.bread.ui.form.impl.field.provider.DefaultFieldProvider;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.data.HasValue;
import com.vaadin.data.converter.LocalDateToDateConverter;
import com.vaadin.data.converter.StringToBigDecimalConverter;
import com.vaadin.data.converter.StringToBigIntegerConverter;
import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.data.converter.StringToFloatConverter;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.converter.StringToLongConverter;
import com.vaadin.data.util.BeanUtil;
import com.vaadin.shared.util.SharedUtil;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;

/**
 * @author Alejandro Duarte.
 */
public abstract class AbstractAutoGeneratedFormFactory<T> extends AbstractFormFactory<T> implements FormFactory<T> {


//    protected String cancelButtonCaption = "Cancel";
    protected String validationErrorMessage = "Please fix the errors and try again";
    protected Class<T> domainType;

    protected Binder<T> binder;

    public AbstractAutoGeneratedFormFactory(Class<T> domainType, OperationMode[] operationModes) {
    	super(operationModes);
        this.domainType = domainType;
    }


    public void setValidationErrorMessage(String validationErrorMessage) {
        this.validationErrorMessage = validationErrorMessage;
    }

    public void buildSensitiveDefaults() {
    	buildSensitiveDefaults(domainType);
    }

    @SuppressWarnings("rawtypes")
	protected List<HasValue> buildFields(OperationMode operationMode, T domainObject, boolean readOnly) {
        binder = buildBinder(operationMode, domainObject);
        ArrayList<HasValue> fields = new ArrayList<>();
        FormConfiguration configuration = getConfiguration(operationMode);

        for (int i = 0; i < configuration.getVisibleProperties().size(); i++) {
            String property = configuration.getVisibleProperties().get(i);
            try {
                String fieldCaption = null;
                if (!configuration.getFieldCaptions().isEmpty()) {
                    fieldCaption = configuration.getFieldCaptions().get(i);
                }

                Class<?> propertyType = BeanUtil.getPropertyType(domainObject.getClass(), property);

                if (propertyType == null) {
                    throw new RuntimeException("Cannot find type for property " + domainObject.getClass().getName() + "." + property);
                }

                HasValue<Object> field = buildField(configuration, property, propertyType);
                configureField(field, property, fieldCaption, readOnly, configuration);
                bindField(field, property, propertyType);
                fields.add(field);

                FieldCreationListener creationListener = configuration.getFieldCreationListeners().get(property);
                if (creationListener != null) {
                    creationListener.fieldCreated(field);
                }

            } catch (Exception e) {
                throw new RuntimeException("Error creating Field for property " + domainObject.getClass().getName() + "." + property, e);
            }
        }

        binder.readBean(domainObject);

        if (!fields.isEmpty() && !readOnly) {
            HasValue field = fields.get(0);
            if (field instanceof Component.Focusable) {
                ((Component.Focusable) field).focus();
            }
        }

        return fields;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	protected HasValue<Object> buildField(FormConfiguration configuration, String property, Class<?> propertyType) throws InstantiationException, IllegalAccessException {
        HasValue<Object> field;
        FieldProvider provider = configuration.getFieldProviders().get(property);

        if (provider != null) {
            field = provider.buildField();
        } else {
            Class<? extends HasValue> fieldType = configuration.getFieldTypes().get(property);
            if (fieldType != null) {
                field = fieldType.newInstance();
            } else {
                field = new DefaultFieldProvider(propertyType).buildField();
            }
        }

        return field;
    }

    private void configureField(HasValue<Object> field, String property, String fieldCaption, boolean readOnly, FormConfiguration configuration) {
        if (field instanceof AbstractComponent) {
            if (fieldCaption != null) {
                ((AbstractComponent) field).setCaption(fieldCaption);
            } else {
                ((AbstractComponent) field).setCaption(SharedUtil.propertyIdToHumanFriendly(property));
            }
        }

        if (field != null && field instanceof Component) {
            ((Component) field).setWidth("100%");
        }

        field.setReadOnly(readOnly);

        if (!configuration.getDisabledProperties().isEmpty()) {
            ((Component) field).setEnabled(!configuration.getDisabledProperties().contains(property));
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	protected void bindField(HasValue<Object> field, String property, Class<?> propertyType) {
        Binder.BindingBuilder bindingBuilder = binder.forField(field);

        if (AbstractTextField.class.isAssignableFrom(field.getClass())) {
            bindingBuilder = bindingBuilder.withNullRepresentation("");
        }

        if (Double.class.isAssignableFrom(propertyType) || double.class.isAssignableFrom(propertyType)) {
            bindingBuilder = bindingBuilder.withConverter(new StringToDoubleConverter(null, "Must be a number"));

        } else if (Long.class.isAssignableFrom(propertyType) || long.class.isAssignableFrom(propertyType)) {
            bindingBuilder = bindingBuilder.withConverter(new StringToLongConverter(null, "Must be a number"));

        } else if (BigDecimal.class.isAssignableFrom(propertyType)) {
            bindingBuilder = bindingBuilder.withConverter(new StringToBigDecimalConverter(null, "Must be a number"));

        } else if (BigInteger.class.isAssignableFrom(propertyType)) {
            bindingBuilder = bindingBuilder.withConverter(new StringToBigIntegerConverter(null, "Must be a number"));

        } else if (Integer.class.isAssignableFrom(propertyType) || int.class.isAssignableFrom(propertyType)) {
            bindingBuilder = bindingBuilder.withConverter(new StringToIntegerConverter(null, "Must be a number"));

        } else if (Byte.class.isAssignableFrom(propertyType) || byte.class.isAssignableFrom(propertyType)) {
            bindingBuilder = bindingBuilder.withConverter(new StringToByteConverter(null, "Must be a number"));

        } else if (Character.class.isAssignableFrom(propertyType) || char.class.isAssignableFrom(propertyType)) {
            bindingBuilder = bindingBuilder.withConverter(new StringToCharacterConverter());

        } else if (Float.class.isAssignableFrom(propertyType) || float.class.isAssignableFrom(propertyType)) {
            bindingBuilder = bindingBuilder.withConverter(new StringToFloatConverter(null, "Must be a number"));

        } else if (Date.class.isAssignableFrom(propertyType)) {
            bindingBuilder = bindingBuilder.withConverter(new LocalDateToDateConverter());
        }

        bindingBuilder.bind(property);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	protected Binder<T> buildBinder(OperationMode operationMode, T domainObject) {
        Binder<T> binder;

        if (getConfiguration(operationMode).isUseBeanValidation()) {
            if (getConfiguration(operationMode).getJpaTypeForJpaValidation()!=null) {
            	binder = new JpaValidationBinder(getConfiguration(operationMode).getJpaTypeForJpaValidation(), domainObject.getClass());
            } else {
            	binder = new BeanValidationBinder(domainObject.getClass());
            }
        } else {
            binder = new Binder(domainObject.getClass());
        }

        return binder;
    }

    protected Button buildOperationButton(OperationMode operationMode, OperationAction operationAction, T domainObject, Button.ClickListener clickListener) {
        if (clickListener == null || operationMode==null || operationAction==null) {
            return null;
        }

        Button button = new Button(getConfiguration(operationMode).getButtonCaption(operationAction)
        		, getConfiguration(operationMode).getButtonIcon(operationAction)
        		);
        Set<String> set = getConfiguration(operationMode).getButtonStyleNames(operationAction);
        if (set!=null) {
        	set.forEach(styleName -> button.addStyleName(styleName));
        }
        button.addClickListener(event -> {
            if (domainObject==null || binder.writeBeanIfValid(domainObject)) {
                try {
                    clickListener.buttonClick(event);
                } catch (Exception e) {
                    showError(operationMode, operationAction, domainObject, e);
                }
            } else {
                Notification.show(validationErrorMessage);
            }
        });
        return button;
    }

    @Override
    public void showError(OperationMode operationMode, OperationAction operationAction, T domainObject, Exception e) {
        if (errorListener != null) {
            errorListener.onError(operationMode, operationAction, domainObject, e);
        } else {
            if (OperationException.class.isAssignableFrom(e.getClass())) {
                Notification.show(e.getMessage(), Notification.Type.ERROR_MESSAGE);
            } else {
                Notification.show(e.getMessage(), Notification.Type.ERROR_MESSAGE);
                throw new RuntimeException("Error executing " + operationAction + " action on " +domainObject+ " in mode "+operationMode, e);
            }
        }
    }

    protected Layout buildFooter(OperationMode operationMode, T domainObject) {
    	ArrayList<Button> buttons = new ArrayList<>();
    	for (OperationAction operationAction : getConfiguration(operationMode).getOperationActions()) {
			
    		Button btn = buildOperationButton(operationMode, operationAction, domainObject, getConfiguration(operationMode).getOperationActionListener(operationAction));
    		if (btn!=null) {
    			buttons.add(btn);
    		}
		}
    	
        HorizontalLayout footerLayout = new HorizontalLayout();
        footerLayout.setSizeUndefined();
        footerLayout.setSpacing(true);
        if (!buttons.isEmpty()) {
            footerLayout.addComponents(buttons.toArray(new Button[] {}));
        }

        return footerLayout;
    }


	public Binder<T> getBinder() {
		return binder;
	}

}
