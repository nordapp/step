package org.i3xx.step.zero.service.impl.cx;

/*
 * #%L
 * NordApp OfficeBase :: zero
 * %%
 * Copyright (C) 2014 - 2015 I.D.S. DialogSysteme GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.i3xx.step.zero.service.model.cx.Context;
import org.i3xx.step.zero.service.model.cx.ContextProvider;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContextProviderImpl implements ContextProvider {
	
	private static Logger logger = LoggerFactory.getLogger(ContextProviderImpl.class);
	
	private BundleContext bundleContext;
	
	private Map<BigInteger, ServiceRegistration<?>> services;
	
	public ContextProviderImpl() {
		setBundleContext(null);
		services = new HashMap<BigInteger, ServiceRegistration<?>>();
	}

	/* (non-Javadoc)
	 * @see org.i3xx.step.tre.core.model.cx.ContextProvider#bind(java.util.Map)
	 */
	public Context bind(Map<String, Object> properties) {
		String clazz = Context.class.getName();
		String filter = ContextProviderImpl.getFilter(properties);
		
		ServiceReference<?>[] refs = null;
		try {
			refs = bundleContext.getServiceReferences(clazz, filter);
		} catch (InvalidSyntaxException e) {
			logger.warn("bind: The filter is not matching.", e);
		}
		
		Context context = null;
		for(ServiceReference<?> ref : refs) {
			context = (Context)bundleContext.getService(ref);
			if(context!=null)
				break;
		}
		
		Hashtable<String, Object> prop = new Hashtable<String, Object>();
		prop.putAll(properties);
		
		synchronized(services){
			if(context==null) {
				context = new ContextImpl();
				prop.put(Context.CONTEXT_ID, context.getId());
				services.put( context.getId(), bundleContext.registerService(clazz, context, prop) );
			}
			context.bind();
		}
		
		return context;
	}

	/* (non-Javadoc)
	 * @see org.i3xx.step.tre.core.model.cx.ContextProvider#find(java.util.Map)
	 */
	public Context find(Map<String, Object> properties) {
		String clazz = Context.class.getName();
		String filter = ContextProviderImpl.getFilter(properties);
		
		ServiceReference<?>[] refs = null;
		try {
			refs = bundleContext.getServiceReferences(clazz, filter);
		} catch (InvalidSyntaxException e) {
			logger.warn("find: The filter is not matching.", e);
		}
		
		Context context = null;
		for(ServiceReference<?> ref : refs) {
			context = (Context)bundleContext.getService(ref);
			if(context!=null)
				break;
		}//for
		
		return context;
	}
	
	/* (non-Javadoc)
	 * @see org.i3xx.step.zero.service.model.cx.ContextProvider#findAll(java.util.Map)
	 */
	public Context[] findAll(Map<String, Object> properties) {
		String clazz = Context.class.getName();
		String filter = getFilter(properties);
		
		ServiceReference<?>[] refs = null;
		try {
			refs = bundleContext.getServiceReferences(clazz, filter);
		} catch (InvalidSyntaxException e) {
			logger.warn("find: The filter is not matching.", e);
		}
		
		List<Context> context = new ArrayList<Context>();
		if(refs!=null){
			for(ServiceReference<?> ref : refs) {
				
				context.add( (Context)bundleContext.getService(ref) );
			}//for
		}//fi
		
		return context.toArray(new Context[context.size()]);
	}

	/* (non-Javadoc)
	 * @see org.i3xx.step.tre.core.model.cx.ContextProvider#free(java.util.Map)
	 */
	public void free(Map<String, Object> properties) {
		String clazz = Context.class.getName();
		String filter = ContextProviderImpl.getFilter(properties);
		
		ServiceReference<?>[] refs = null;
		try {
			refs = bundleContext.getServiceReferences(clazz, filter);
		} catch (InvalidSyntaxException e) {
			logger.warn("find: The filter is not matching.", e);
		}
		
		Context context = null;
		for(ServiceReference<?> ref : refs) {
			context = (Context)bundleContext.getService(ref);
			if(context!=null) {
				//Avoids NullPointerException
				synchronized(services){
					int c = context.unbind();
					if(c<=0 && services.containsKey(context.getId())) {
						services.remove(context.getId()).unregister();
					}//fi
					break;
				}
			}//fi
		}//for
	}
	
	/* (non-Javadoc)
	 * @see org.i3xx.step.zero.service.model.cx.ContextProvider#kill(java.util.Map)
	 */
	public void kill(Map<String, Object> properties) {
		String clazz = Context.class.getName();
		String filter = getFilter(properties);
		
		ServiceReference<?>[] refs = null;
		try {
			refs = bundleContext.getServiceReferences(clazz, filter);
		} catch (InvalidSyntaxException e) {
			logger.warn("find: The filter is not matching.", e);
		}
		
		Context context = null;
		if(refs!=null){
			for(ServiceReference<?> ref : refs) {
				context = (Context)bundleContext.getService(ref);
				if(services.containsKey(context.getId())) {
					services.remove(context.getId()).unregister();
				}//fi
			}//for
		}//fi
	}

	/**
	 * @return the bundleContext
	 */
	public BundleContext getBundleContext() {
		return bundleContext;
	}

	/**
	 * @param bundleContext the bundleContext to set
	 */
	public void setBundleContext(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}
	
	/**
	 * @param properties
	 * @return
	 */
	private static String getFilter(Map<String, Object> properties) {
		StringBuffer filter = new StringBuffer();
		
		filter.append("(&");
		for(Map.Entry<String, Object> e : properties.entrySet()) {
			filter.append('(');
			filter.append( e.getKey() );
			filter.append('=');
			filter.append( e.getValue() );
			filter.append(')');
		}
		filter.append(')');
		
		String result = filter.toString();
		logger.debug("Searching with filter {}", result);
		
		return result;
	}
	
	/**
	 * Gets the first matching context or null if nothing matches.
	 * 
	 * @param bundleContext
	 * @param properties
	 * @return
	 */
	public static Context find(BundleContext bundleContext, Map<String, Object> properties) {
		String clazz = Context.class.getName();
		String filter = getFilter(properties);
		
		ServiceReference<?>[] refs = null;
		try {
			refs = bundleContext.getServiceReferences(clazz, filter);
		} catch (InvalidSyntaxException e) {
			logger.warn("find: The filter is not matching.", e);
		}
		
		Context context = null;
		if(refs!=null){
			for(ServiceReference<?> ref : refs) {
				context = (Context)bundleContext.getService(ref);
				if(context!=null)
					break;
			}//for
		}//fi
		
		return context;
	}

}
