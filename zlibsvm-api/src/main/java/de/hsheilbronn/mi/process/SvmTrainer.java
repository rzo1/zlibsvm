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
package de.hsheilbronn.mi.process;

import de.hsheilbronn.mi.domain.SvmDocument;
import de.hsheilbronn.mi.domain.SvmModel;

import java.util.List;

/**
 * A trainer to use with LIBSVM.
 *
 * @author rz
 */
public interface SvmTrainer {

    /**
     * Trains the underlying learning machine with the given documents.
     *
     * @param documents must not be {@code null}  or an empty {@link List list}.
     * @return the trained {@link SvmModel model} or {@code null} if cross validation mode is enabled.
     * @throws AssertionError Thrown if a given parameter is invalid.
     */

    SvmModel train(List<SvmDocument> documents);


    /**
     * @return the accuracy of a cross validation performed while training in the interval 0 to 100 or -1, if cross validation mode is not enabled or this method was called before {@link SvmTrainer#train(List)}.
     */
    double getCrossValidationAccuracy();
}
