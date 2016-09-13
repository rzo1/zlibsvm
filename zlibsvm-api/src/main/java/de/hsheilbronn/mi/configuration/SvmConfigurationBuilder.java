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
package de.hsheilbronn.mi.configuration;

import java.io.Serializable;

/**
 * Builds a {@link SvmConfiguration} object.
 *
 * @author rz
 */
public interface SvmConfigurationBuilder extends Serializable {
    /**
     * Determines the type of svm.
     * <ul>
     * <li>0 - C-SVC (multi-class classification) <b>default</b></li>
     * <li>1 - nu-SVC(multi-class classification)</li>
     * <li>2 - one-class SVM</li>
     * <li>3 - epsilon-SVR (regression)</li>
     * <li>4 - nu-SVR (regression)</li>
     * </ul>
     *
     * @param svmType the {@link SvmType svm type}
     * @return this {@link SvmConfigurationBuilder builder}
     */
    SvmConfigurationBuilder setSvmType(SvmType svmType);

    /**
     * Determines the type of the kernel function of the svm.
     * <ul>
     * <li>0 - linear</li>
     * <li>1 - polynomial: (gamma*u'*v + coef0)^degree</li>
     * <li>2 - radial basis function: exp(-gamma*|u-v|^2) <b>default</b></li>
     * <li>3 - sigmoid: tanh(gamma*u'*v + coef0)</li>
     * <li>4 - precomputed kernel (kernel values included in training data set)</li>
     * </ul>
     *
     * @param kernelType the {@link KernelType kernel type}
     * @return this this {@link SvmConfigurationBuilder builder}
     */
    SvmConfigurationBuilder setKernelType(KernelType kernelType);

    /**
     * Determines the degree in the kernel function. <b>default: 3</b>
     *
     * @param degree must be <code> >= 0</code>
     * @return this {@link SvmConfigurationBuilder builder}
     * @throws AssertionError if the given parameter is invalid.
     */
    SvmConfigurationBuilder setDegree(int degree);

    /**
     * Determines the gamma in the kernel function. <b>default: 1/(amount_of_features)</b>
     *
     * @param gamma must be <code> >= 0</code>
     * @return this {@link SvmConfigurationBuilder builder}
     * @throws AssertionError if the given parameter is invalid.
     */
    SvmConfigurationBuilder setGamma(double gamma);

    /**
     * Determines the coef0 in kernel function. <b>default: 0</b>
     *
     * @param coef0 the coef0 in kernel function
     * @return this {@link SvmConfigurationBuilder builder}
     */
    SvmConfigurationBuilder setCoef0(double coef0);

    /**
     * Determines the parameter nu of nu-SVC, one-class SVM and nu-SVR <b>default: 0.5</b>
     *
     * @param nu must be <code> >0  and <= 1 </code>
     * @return this {@link SvmConfigurationBuilder builder}
     * @throws AssertionError if the given parameter is invalid.
     */
    SvmConfigurationBuilder setNu(double nu);

    /**
     * Cache memory size in MB. <b>default: 100</b>
     *
     * @param cacheSize must be <code> > 0</code>
     * @return this {@link SvmConfigurationBuilder builder}
     * @throws AssertionError if the given parameter is invalid.
     */
    SvmConfigurationBuilder setCacheSize(double cacheSize);

    /**
     * Determines the parameter C (cost) of C-SVC, epsilon-SVR, and nu-SVR <b>default: 1</b>
     * <p>See also: paper by Gaspar, Carbonell and Oliveira (Univ. of Aveiro) in JIB V201,
     * 2012: they describe C as penality weight</p>
     *
     * @param cost must be <code> > 0</code>
     * @return this {@link SvmConfigurationBuilder builder}
     * @throws AssertionError if the given parameter is invalid.
     */
    SvmConfigurationBuilder setCost(double cost);

    /**
     * Determines the tolerance of termination criterion <b>default: 0.001</b>
     *
     * @param epsilon must be <code> > 0 </code>
     * @return this {@link SvmConfigurationBuilder builder}
     * @throws AssertionError if the given parameter is invalid.
     */
    SvmConfigurationBuilder setEpsilon(double epsilon);

    /**
     * Determines the epsilon in the loss (or cost) function of epsilon-SVR <b>default: 0.1</b>
     *
     * @param p must be <code> p >= 0</code>      .
     * @return this {@link SvmConfigurationBuilder builder}
     * @throws AssertionError if the given parameter is invalid.
     */
    SvmConfigurationBuilder setP(double p);

    /**
     * Determines whether to use shrinking heuristics. <b>default: true</b>
     *
     * @param shrinking <code>true</code> if to use shrinking heuristics, else <code>false</code>
     * @return this {@link SvmConfigurationBuilder builder}
     */
    SvmConfigurationBuilder setShrinking(boolean shrinking);

    /**
     * Determines whether to train a SVC or SVR model for probability estimates.  <b>default: false</b>
     *
     * @param probability <code>true</code> if to train for probability estimates, else <code>false</code>
     * @return this {@link SvmConfigurationBuilder builder}
     */
    SvmConfigurationBuilder setProbability(boolean probability);

    /**
     * Sets the parameter C (cost) of a C-SVC svm for a specified class label to C*weight. <b>default: 1</b>
     *
     * @param classLabel classLabel, who's parameter C should be weighted
     * @param weight     the weight
     * @return this {@link SvmConfigurationBuilder builder}
     */
    SvmConfigurationBuilder setNrWeight(int classLabel, double weight);

    /**
     * Determines whether to use n-fold cross validation mode. <b>default: false</b>
     *
     * @param crossValidation <code>true</code if to use n-fold cross validation mode, else <code>false</code>
     * @param nFold           must be <code>>= 2</code>
     * @return this {@link SvmConfigurationBuilder builder}
     * @throws AssertionError if the given parameter is invalid.
     */
    SvmConfigurationBuilder setCrossValidation(boolean crossValidation, int nFold);

    /**
     * @return a fully configured {@link SvmConfiguration configuration}
     */
    SvmConfiguration build();
}
