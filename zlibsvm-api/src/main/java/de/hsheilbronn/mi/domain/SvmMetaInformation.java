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

import de.hsheilbronn.mi.configuration.SvmConfiguration;

import java.io.Serializable;
import java.util.List;

/**
 * A {@link SvmMetaInformation} encapsulates meta information of a {@link SvmModel}.
 *
 * @author rz
 */
public interface SvmMetaInformation extends Serializable{

    String getModelName();

    SvmConfiguration getSvmConfiguration();

    int getNumberOfClasses();

    int getAmountOfSupportVectors();

    /**
     * @return a list of needed mathematical constants in the svm decision functions
     */
    List<Double> getRhoConstants();

    List<Integer> getLabelForEachClass();

    List<Double> getProbabilityA();

    List<Double> getProbabilityB();

    List<Integer> getNumberOfSupportVectorsForEachClass();

    void setSvmConfiguration(SvmConfiguration svmConfiguration);

    void setNumberOfClasses(int numberOfClasses);

    void setAmountOfSupportVectors(int amountOfSupportVectors);

    void setRhoConstants(List<Double> rhoConstants);

    void setLabelForEachClass(List<Integer> labelForEachClass);

    void setProbabilityA(List<Double> probabilityA);

    void setProbabilityB(List<Double> probabilityB);

    void setNumberOfSupportVectorsForEachClass(List<Integer> numberOfSupportVectorsForEachClass);


}