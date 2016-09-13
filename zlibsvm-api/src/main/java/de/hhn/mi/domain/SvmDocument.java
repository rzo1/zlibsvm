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
import java.util.List;

/**
 * A {@link SvmDocument} is the numeric representation of a textual document.
 *
 * @author rz
 */
public interface SvmDocument extends Serializable {

    /**
     * @return a sorted list in ascending order, sorted by {@link SvmFeature features numeric value}
     */
    List<SvmFeature> getSvmFeatures();

    /**
     * @return <code>null</code>, if this document is not classified yet.
     */
    SvmClassLabel getClassLabelWithHighestProbability();

    /**
     * @return a list containing all {@link SvmClassLabel labels} of this {@link SvmDocument document}. It is empty, if
     *         this document is not classified yet.
     */
    List<SvmClassLabel> getAllClassLabels();

    /**
     * @param classLabel must not be <code>null</code>
     * @throws AssertionError if a given parameter is invalid.
     */
    void addClassLabel(SvmClassLabel classLabel);
}
