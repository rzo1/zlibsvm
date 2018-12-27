/*-
 * ========================LICENSE_START=================================
 * zlibsvm-api
 * %%
 * Copyright (C) 2014 - 2019 Heilbronn University - Medical Informatics
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
package de.hsheilbronn.mi.domain;

import java.io.Serializable;

/**
 * An {@link SvmFeature feature} is a pair of two values. One representing its unique id within a given corpus and
 * the numeric feature value.
 * <p/>
 * Example: There are two documents in a given corpus, called A and B. A and B are containing the term t. The
 * index is a unique identifier for the term t in the corpus. However the feature value
 * must not be unique for a term t and can vary in A and B based on the chosen feature selection method.
 *
 * @author rz
 */
public interface SvmFeature extends Comparable<SvmFeature>, Serializable

{
    /**
     * @param index must be greater than zero
     * @throws AssertionError if a given parameter is invalid
     */
    void setIndex(int index);

    /**
     * @param value no restrictions on this parameter
     */
    void setValue(double value);

    int getIndex();

    double getValue();

}
