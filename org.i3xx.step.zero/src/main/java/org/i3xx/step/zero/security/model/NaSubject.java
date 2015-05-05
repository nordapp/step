package org.i3xx.step.zero.security.model;

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


import java.util.Collection;
import java.util.List;

public interface NaSubject {

    Object getPrincipal();

    NaPrincipalCollection getPrincipals();

    boolean isPermitted(String permission);

    boolean[] isPermitted(String... permissions);

    boolean isPermittedAll(String... permissions);

    void checkPermission(String permission) throws RuntimeException;

    void checkPermissions(String... permissions) throws RuntimeException;

    boolean hasRole(String roleIdentifier);

    boolean[] hasRoles(List<String> roleIdentifiers);

    boolean hasAllRoles(Collection<String> roleIdentifiers);

    void checkRole(String roleIdentifier) throws RuntimeException;
    
    void checkRoles(Collection<String> roleIdentifiers) throws RuntimeException;

    void checkRoles(String... roleIdentifiers) throws RuntimeException;

    void login(NaAuthenticationToken token) throws RuntimeException;

    boolean isAuthenticated();

    boolean isRemembered();

    NaSession getSession();

    NaSession getSession(boolean create);

    void logout();
    
    //Not supported
    //<V> V execute(Callable<V> callable) throws ExecutionException;

    void execute(Runnable runnable);

    //Not supported
    //<V> Callable<V> associateWith(Callable<V> callable);

    Runnable associateWith(Runnable runnable);

    void runAs(NaPrincipalCollection principals) throws NullPointerException, IllegalStateException;

    boolean isRunAs();

    NaPrincipalCollection getPreviousPrincipals();

    NaPrincipalCollection releaseRunAs();
}
