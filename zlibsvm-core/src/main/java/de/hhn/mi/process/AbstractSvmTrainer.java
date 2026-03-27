/*-
 * ========================LICENSE_START=================================
 * zlibsvm-core
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

import de.hhn.mi.configuration.SvmConfiguration;
import de.hhn.mi.domain.SvmDocument;
import de.hhn.mi.domain.SvmModel;
import de.hhn.mi.exception.ClassificationCoreException;
import libsvm.svm_parameter;
import libsvm.svm_problem;

import java.util.List;
import java.util.Optional;

public abstract class AbstractSvmTrainer extends AbstractSvmTool implements SvmTrainer {

    private svm_parameter param;
    private svm_problem problem;
    private final SvmConfiguration configuration;
    private final String modelName;
    private double crossValidationAccuracy = -1.0d;

    /**
     * @param svmConfiguration must not be {@code null}.
     * @param modelName        must not be {@code null}.
     * @throws IllegalArgumentException if a given parameter is invalid.
     */
    AbstractSvmTrainer(SvmConfiguration svmConfiguration, String modelName) {
        if (svmConfiguration == null) {
            throw new IllegalArgumentException("svmConfiguration must not be null");
        }
        if (modelName == null) {
            throw new IllegalArgumentException("modelName must not be null");
        }
        this.configuration = svmConfiguration;
        this.modelName = modelName;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<SvmModel> train(List<SvmDocument> documents) {
        if (documents == null || documents.isEmpty()) {
            throw new IllegalArgumentException("documents must not be null or empty");
        }

        param = unwrap(configuration);
        problem = loadTrainingProblem(documents);
        validateConfiguration();
        if (configuration.getCrossValidation() != 0) {
            crossValidationAccuracy = doCrossValidation(configuration);
            return Optional.empty();
        }
        return Optional.of(getTrainedModel());
    }

    /**
     * Loads an {@link svm_problem}.
     *
     * @return the {@link svm_problem problem}
     */
    protected abstract svm_problem loadTrainingProblem(List<SvmDocument> documents);

    /**
     * @param configuration the {@link SvmConfiguration}.
     * @return the accuracy of the cross validation parameters.
     */
    protected abstract double doCrossValidation(SvmConfiguration configuration);

    /**
     * Validates the given {@link SvmConfiguration}.
     *
     * @throws ClassificationCoreException if a nu-svc in the given configuration is infeasible or one-class SVM probability output was specified.
     */
    protected abstract void validateConfiguration();

    protected abstract SvmModel getTrainedModel();

    /**
     * {@inheritDoc}
     */
    public double getCrossValidationAccuracy() {
        return crossValidationAccuracy;
    }

    String getModelName() {
        return modelName;
    }

    svm_parameter getParam() {
        return param;
    }

    svm_problem getProblem() {
        return problem;
    }
}
