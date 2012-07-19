package org.apache.hupa.server.ioc;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.web.bindery.requestfactory.server.ServiceLayerDecorator;
import com.google.web.bindery.requestfactory.shared.Locator;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;
import com.google.web.bindery.requestfactory.shared.ServiceLocator;

public final class IocRfServiceDecorator extends ServiceLayerDecorator {
    @Inject
    private Injector injector;

    @Override
    public <T extends Locator<?, ?>> T createLocator(Class<T> clazz) {
        return injector.getInstance(clazz);
    }

    @Override
    public Object createServiceInstance(
            Class<? extends RequestContext> requestContext) {
        Class<? extends ServiceLocator> serviceLocatorClass;
        if ((serviceLocatorClass = getTop().resolveServiceLocator(
                requestContext)) != null) {
            return injector.getInstance(serviceLocatorClass).getInstance(
                    requestContext.getAnnotation(Service.class).value());
        } else {
            return null;
        }
    }

    @Override
    public <T> T createDomainObject(Class<T> clazz) {
        System.out.println("Create domain " + clazz);
        return injector.getInstance(clazz);
    }
}