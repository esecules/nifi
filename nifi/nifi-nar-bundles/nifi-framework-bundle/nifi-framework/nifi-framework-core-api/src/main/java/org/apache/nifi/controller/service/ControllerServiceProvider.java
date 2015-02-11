/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.nifi.controller.service;

import java.util.Set;

import org.apache.nifi.annotation.lifecycle.OnAdded;
import org.apache.nifi.controller.ControllerServiceLookup;

/**
 *
 */
public interface ControllerServiceProvider extends ControllerServiceLookup {

    /**
     * Creates a new Controller Service of the specified type and assigns it the given id. If <code>firstTimeadded</code>
     * is true, calls any methods that are annotated with {@link OnAdded}
     *
     * @param type
     * @param id
     * @param firstTimeAdded
     * @return
     */
    ControllerServiceNode createControllerService(String type, String id, boolean firstTimeAdded);

    /**
     * Gets the controller service node for the specified identifier. Returns
     * <code>null</code> if the identifier does not match a known service
     *
     * @param id
     * @return
     */
    ControllerServiceNode getControllerServiceNode(String id);
    
    /**
     * Removes the given Controller Service from the flow. This will call all appropriate methods
     * that have the @OnRemoved annotation.
     * 
     * @param serviceNode the controller service to remove
     * 
     * @throws IllegalStateException if the controller service is not disabled or is not a part of this flow
     */
    void removeControllerService(ControllerServiceNode serviceNode);
    
    /**
     * Enables the given controller service that it can be used by other components
     * @param serviceNode
     */
    void enableControllerService(ControllerServiceNode serviceNode);
    
    /**
     * Disables the given controller service so that it cannot be used by other components. This allows
     * configuration to be updated or allows service to be removed.
     * @param serviceNode
     */
    void disableControllerService(ControllerServiceNode serviceNode);
    
    /**
     * Returns a Set of all Controller Services that exist for this service provider.
     * @return
     */
    Set<ControllerServiceNode> getAllControllerServices();
    
    /**
     * Recursively stops all Processors and Reporting Tasks that are referencing the given Controller Service,
     * as well as disabling any Controller Service that references this Controller Service (and stops
     * all Reporting Task or Controller Service that is referencing it, and so on).
     * @param serviceNode
     */
    void deactivateReferencingComponents(ControllerServiceNode serviceNode);
    
    /**
     * <p>
     * Starts any enabled Processors and Reporting Tasks that are referencing this Controller Service. If other Controller
     * Services reference this Controller Service, will also enable those services and 'activate' any components referencing
     * them.
     * </p>
     * 
     * <p>
     * NOTE: If any component cannot be started, an IllegalStateException will be thrown an no more components will
     * be activated. This method provides no atomicity.
     * </p>
     * 
     * @param serviceNode
     */
    void activateReferencingComponents(ControllerServiceNode serviceNode);
}
