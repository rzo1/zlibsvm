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

import de.hhn.mi.configuration.SvmConfiguration;

import java.io.Serializable;
import java.util.List;

/**
 * Encapsulates metadata of a trained {@link SvmModel}, including the configuration
 * used for training and the internal LIBSVM model parameters (rho constants,
 * class labels, probability coefficients, and support-vector distribution).
 */
public interface SvmMetaInformation extends Serializable {

    /**
     * @return the user-assigned name identifying this model
     */
    String getModelName();

    /**
     * @return the {@link SvmConfiguration} used to produce this model
     */
    SvmConfiguration getSvmConfiguration();

    /**
     * @return the number of classes in the model (2 for binary classification)
     */
    int getNumberOfClasses();

    /**
     * @return the total number of support vectors across all classes
     */
    int getAmountOfSupportVectors();

    /**
     * @return the rho constants (biases) in the SVM decision functions, one per class pair
     */
    List<Double> getRhoConstants();

    /**
     * @return the numeric label assigned to each class
     */
    List<Integer> getLabelForEachClass();

    /**
     * @return pairwise probability coefficients A, or an empty list if probability estimation was not enabled
     */
    List<Double> getProbabilityA();

    /**
     * @return pairwise probability coefficients B, or an empty list if probability estimation was not enabled
     */
    List<Double> getProbabilityB();

    /**
     * @return the number of support vectors for each class
     */
    List<Integer> getNumberOfSupportVectorsForEachClass();

    /**
     * @param svmConfiguration the configuration to set
     */
    void setSvmConfiguration(SvmConfiguration svmConfiguration);

    /**
     * @param numberOfClasses the number of classes
     */
    void setNumberOfClasses(int numberOfClasses);

    /**
     * @param amountOfSupportVectors the total number of support vectors
     */
    void setAmountOfSupportVectors(int amountOfSupportVectors);

    /**
     * @param rhoConstants the rho constants (biases) for the decision functions
     */
    void setRhoConstants(List<Double> rhoConstants);

    /**
     * @param labelForEachClass the numeric labels for each class
     */
    void setLabelForEachClass(List<Integer> labelForEachClass);

    /**
     * @param probabilityA the pairwise probability coefficients A
     */
    void setProbabilityA(List<Double> probabilityA);

    /**
     * @param probabilityB the pairwise probability coefficients B
     */
    void setProbabilityB(List<Double> probabilityB);

    /**
     * @param numberOfSupportVectorsForEachClass the per-class support vector counts
     */
    void setNumberOfSupportVectorsForEachClass(List<Integer> numberOfSupportVectorsForEachClass);

}