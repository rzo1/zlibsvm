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
import java.util.List;
import java.util.Map;

/**
 * Represents a trained SVM model, encapsulating the LIBSVM model data needed
 * for classification or further inspection.
 * <p>
 * A model consists of {@link SvmMetaInformation metadata} (configuration, class info,
 * rho constants), support vectors, and their coefficients.
 */
public interface SvmModel extends Serializable {

    /**
     * @return the metadata of this model, including configuration and structural parameters
     */
    SvmMetaInformation getMetaInformation();

    /**
     * @return the support-vector coefficients used in the SVM decision functions,
     * keyed by row index
     */
    Map<Integer, List<Double>> getSvCoefficients();

    /**
     * @return the support vectors of this model, keyed by row index,
     * where each value is a list of {@link SvmFeature features}
     */
    Map<Integer, List<SvmFeature>> getSupportVectors();

}
