package org.i3xx.step.uno.impl;

/*
 * #%L
 * NordApp OfficeBase :: uno
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


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.i3xx.step.uno.impl.service.TestService;
import org.i3xx.step.uno.impl.service.builtin.ContextAdministrationService;
import org.i3xx.step.uno.impl.service.builtin.ContextServiceAdapter;
import org.i3xx.step.uno.impl.service.builtin.KeyIndexService;
import org.i3xx.step.uno.impl.service.builtin.NotifyValueService;
import org.i3xx.step.uno.impl.service.builtin.ServiceFactoryService;
import org.i3xx.step.uno.impl.service.builtin.VerifyValueService;
import org.i3xx.step.uno.model.StepContext;
import org.i3xx.step.uno.model.service.builtin.keyindex.KeyItem;
import org.junit.Test;

public class ContextImplTest {

	@Test
	public void test() throws Exception {
		
		StepContext cx = new ContextImpl();
		
		//
		// Tests the context service
		//
		
		ContextServiceAdapter css = (ContextServiceAdapter)cx.getService("org.i3xx.step.uno.ContextService");
		assertNotNull(css);
		
		//
		// Tests the context admin service
		//
		
		ContextAdministrationService cas = (ContextAdministrationService)cx.getService("org.i3xx.step.uno.ContextAdministrationService");
		//There is no function implemented yet
		assertNotNull(cas);
		
		//
		// Tests the context key index
		//
		
		KeyIndexService kis = (KeyIndexService)css.getService("org.i3xx.step.uno.KeyIndexService");
		kis.initialize();
		
		css.set("com.i3xx.step.uno.hello.world.en", "Hello World");
		css.set("com.i3xx.step.uno.hello.world.de", "Hallo Welt");
		css.set("com.i3xx.step.uno.hello.world.fr", "�llo Monde");
		
		KeyItem ki = kis.getKey("com.i3xx.step.uno.hello.world");
		
		assertEquals(3, ki.getList().size());
		assertEquals("de", ki.getList().get(0).getKey());
		assertEquals("en", ki.getList().get(1).getKey());
		assertEquals("fr", ki.getList().get(2).getKey());
		
		css.set("com.i3xx.step.uno.hello.world.ru", "Ay CBeT");
		
		assertEquals(4, ki.getList().size());
		assertEquals("de", ki.getList().get(0).getKey());
		assertEquals("en", ki.getList().get(1).getKey());
		assertEquals("fr", ki.getList().get(2).getKey());
		assertEquals("ru", ki.getList().get(3).getKey());
		
		kis.removeKey("com.i3xx.step.uno.hello.world.en");
		kis.removeKey("com.i3xx.step.uno.hello.world.fr");
		
		css.set("com.i3xx.step.uno.hello.world.es", "y Hola Mundo");
		
		assertEquals(3, ki.getList().size());
		assertEquals("de", ki.getList().get(0).getKey());
		assertEquals("es", ki.getList().get(1).getKey());
		assertEquals("ru", ki.getList().get(2).getKey());
		
		//The object removed from the index is still available
		assertEquals("Hello World", css.get("com.i3xx.step.uno.hello.world.en"));
		assertEquals("�llo Monde", css.get("com.i3xx.step.uno.hello.world.fr"));
		
		kis.clear();
		
		// use "" to get the root node of the index
		assertEquals( "index", kis.getKey("").getKey() );
		// clear clears the root node
		assertNull( kis.getKey("").getList() );
		
		//
		// Tests the notify facility
		//
		
		final int[] counter = new int[]{0};
		
		NotifyValueService nvs = (NotifyValueService)css.getService("org.i3xx.step.uno.NotifyValueService");
		nvs.initialize();
		
		nvs.register("notify.me", new NotifyValueService.Change() {
			public void notify(String key, Object oldValue, Object newValue) {
				counter[0]++;
				
				assertEquals("notify.me", key);
				assertNull(oldValue);
				assertEquals("Test", newValue);
			}
		});
		
		css.set("notify.me", "Test");
		assertEquals(1, counter[0]);
		
		nvs.unregister("notify.me");
		css.set("notify.me", "new value");
		assertEquals(1, counter[0]);
		
		//
		// Tests the verification
		//
		
		VerifyValueService vvs = (VerifyValueService)css.getService("org.i3xx.step.uno.VerifyValueService");
		Object obj = new Object();
		
		// without serializable test
		
		css.set("test.me", "Text");
		assertEquals("Text", css.get("test.me"));
		
		css.set("test.me", obj);
		assertEquals(obj, css.get("test.me"));
		
		css.set("test.me", null);
		assertNull(css.get("test.me"));
		
		// enable serializable test
		
		vvs.initialize();
		
		//Object is not serializable
		css.set("test.me", obj);
		
		assertFalse(vvs.verify(obj));
		assertNull(css.get("test.me"));
		
		css.set("test.me", "Text");
		assertEquals("Text", css.get("test.me"));
		
		//
		// Tests the service factory
		//
		
		ServiceFactoryService adm = (ServiceFactoryService)css.getService("org.i3xx.step.uno.ServiceFactoryService");
		adm.initialize();
		adm.setFactory();
		
		adm.register("test.obj", new TestService());
		
		TestService ts = (TestService)css.getService("test.obj");
		assertEquals("Test", ts.test());
	}

}
