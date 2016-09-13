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
package de.hsheilbronn.mi.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * A wrapper interface, which encapsulates a model for usage in a support vector machine. It is inspired by the
 * LIBSVM internally used model.
 *
 * @author rz
 */
public interface SvmModel extends Serializable{

    SvmMetaInformation getMetaInformation();

    /**
     * @return the coefficients for support vectors in the svm decision functions
     */
    Map<Integer, List<Double>> getSvCoefficients();

    Map<Integer, List<SvmFeature>> getSupportVectors();


}
