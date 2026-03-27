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
package de.hhn.mi.process;

import de.hhn.mi.domain.SvmDocument;
import de.hhn.mi.domain.SvmModel;

import java.util.List;
import java.util.Optional;

/**
 * Trains an SVM model from a set of labeled {@link SvmDocument documents} using LIBSVM.
 * <p>
 * Optionally supports n-fold cross-validation when enabled in the
 * {@link de.hhn.mi.configuration.SvmConfiguration}.
 */
public interface SvmTrainer {

    /**
     * Trains the underlying learning machine with the given documents.
     *
     * @param documents must not be {@code null} or an empty {@link List list}.
     * @return an {@link Optional} containing the trained {@link SvmModel}, or empty
     *         if cross-validation mode is enabled (use {@link #getCrossValidationAccuracy()} instead)
     * @throws IllegalArgumentException if a given parameter is invalid.
     */
    Optional<SvmModel> train(List<SvmDocument> documents);

    /**
     * @return the accuracy of the cross-validation in the interval 0 to 100,
     *         or -1 if cross-validation mode is not enabled or {@link #train(List)} has not been called yet
     */
    double getCrossValidationAccuracy();
}
