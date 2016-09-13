/*-
 * ========================LICENSE_START=================================
 * zlibsvm-api
 * %%
 * Copyright (C) 2014 - 2016 Heilbronn University - Medical Informatics
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
 * =========================LICENSE_END==================================
 */
package de.hhn.mi.domain;

import java.io.Serializable;

/**
 * A {SvmClassLabel} represents a category in which a {@link SvmDocument document} can be classified.
 *
 * @author rz
 */
public interface SvmClassLabel extends Comparable<SvmClassLabel>, Serializable {

    double getNumeric();

    String getName();

    double getProbability();

    /**
     * @param probability must be in the interval <code>[0,1]</code>
     * @throws AssertionError if a given parameter is invalid.
     */
    void setProbability(double probability);

}
