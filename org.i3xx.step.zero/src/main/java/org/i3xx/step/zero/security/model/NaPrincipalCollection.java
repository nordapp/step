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
import java.util.Set;

public interface NaPrincipalCollection {

    /**
     * @return the primary principal used to uniquely identify the owning account/Subject
     */
    Object getPrimaryPrincipal();

    /**
     * @param type
     * @return a principal of the specified type or null if there isn't one of the specified type.
     */
    <T> T oneByType(Class<T> type);

    /**
     * @param type
     * @return a Collection of principals that are assignable from the specified type,
     * or an empty Collection if no principals of this type are associated.
     */
    <T> Collection<T> byType(Class<T> type);

    /**
     * @return a single Subject's principals retrieved from all configured Realms as a List.
     */
    List<?> asList();

    /**
     * @return a single Subject's principals retrieved from all configured Realms as a Set.
     */
    Set<?> asSet();

    /**
     * @param realmName the name of the Realm from which the principals were retrieved.
     * @return the Subject's principals from the specified Realm only as a Collection
     * or an empty Collection if there are not any principals from that realm.
     */
    Collection<?> fromRealm(String realmName);

    /**
     * @return the names of realms that this collection has one or more principals for.
     */
    Set<String> getRealmNames();

    /**
     * @return true if this collection is empty, false otherwise.
     */
    boolean isEmpty();
    
    /**
     * @return The principal collection
     */
    Object getPrincipals();
}
