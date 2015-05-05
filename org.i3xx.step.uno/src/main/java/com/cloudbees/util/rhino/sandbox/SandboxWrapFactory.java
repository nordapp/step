/*
 * Copyright 2013, CloudBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cloudbees.util.rhino.sandbox;

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

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.WrapFactory;

/**
 * A {@link WrapFactory} that ensures {@link org.mozilla.javascript.NativeJavaObject} instances are of the
 * {@link SandboxNativeJavaObject} variety.
 */
public class SandboxWrapFactory extends WrapFactory {

    @Override
    public Scriptable wrapAsJavaObject(Context cx, Scriptable scope, Object javaObject, Class<?> staticType) {
        return new SandboxNativeJavaObject(scope, javaObject, staticType);
    }
}
