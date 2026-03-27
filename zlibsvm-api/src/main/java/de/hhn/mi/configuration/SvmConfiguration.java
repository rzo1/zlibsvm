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
package de.hhn.mi.configuration;

import java.io.Serializable;
import java.util.List;

/**
 * An immutable configuration encapsulating all LIBSVM parameters needed to train or classify.
 *
 * @see SvmConfigurationBuilder
 */
public interface SvmConfiguration extends Serializable {

    /**
     * @return the SVM formulation type (C-SVC, nu-SVC, one-class, epsilon-SVR, nu-SVR)
     */
    SvmType getSvmType();

    /**
     * @return the kernel function type (LINEAR, POLYNOMIAL, RBF, SIGMOID, PRECOMPUTED)
     */
    KernelType getKernelType();

    /**
     * @return the degree parameter for the polynomial kernel
     */
    int getDegree();

    /**
     * @return the coef0 parameter used in polynomial and sigmoid kernels
     */
    double getCoef0();

    /**
     * @return the gamma parameter used in polynomial, RBF, and sigmoid kernels
     */
    double getGamma();

    /**
     * @return the nu parameter for nu-SVC, one-class SVM, and nu-SVR
     */
    double getNu();

    /**
     * @return the cache memory size in MB used by LIBSVM during training
     */
    double getCacheSize();

    /**
     * @return the cost (C) penalty parameter for C-SVC, epsilon-SVR, and nu-SVR
     */
    double getCost();

    /**
     * @return the tolerance of the termination criterion (epsilon)
     */
    double getEps();

    /**
     * @return the epsilon value in the loss function of epsilon-SVR
     */
    double getP();

    /**
     * @return 1 if shrinking heuristics are enabled, 0 otherwise
     */
    int getShrinking();

    /**
     * @return 1 if the model is trained for probability estimates, 0 otherwise
     */
    int getProbability();

    /**
     * @return the number of class-specific weight entries
     */
    int getNrWeight();

    /**
     * @return the per-class weight multipliers for the cost parameter C
     */
    List<Double> getWeight();

    /**
     * @return the class labels corresponding to each entry in {@link #getWeight()}
     */
    List<Integer> getWeightLabel();

    /**
     * @return 1 if cross-validation mode is enabled, 0 otherwise
     */
    int getCrossValidation();

    /**
     * @return the number of folds for cross-validation (only meaningful when cross-validation is enabled)
     */
    int getNFold();

    /**
     * @return {@code true} if LIBSVM diagnostic output is suppressed
     */
    boolean isQuietMode();
}
