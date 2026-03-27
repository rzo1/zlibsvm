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
package de.hhn.mi.domain;

import java.io.Serializable;

/**
 * Represents a classification category for an {@link SvmDocument}.
 * Each label carries a numeric identifier, a human-readable name,
 * and a probability estimate (when available).
 * <p>
 * Labels are ordered by probability in ascending order via {@link Comparable}.
 */
public interface SvmClassLabel extends Comparable<SvmClassLabel>, Serializable {

    /**
     * @return the numeric class identifier used by LIBSVM
     */
    double getNumeric();

    /**
     * @return a human-readable name for this class label
     */
    String getName();

    /**
     * @return the probability estimate for this label, or 1.0 if probability estimation was not enabled
     */
    double getProbability();

}
