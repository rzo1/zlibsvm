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
package de.hhn.mi.domain;

import de.hhn.mi.configuration.SvmConfiguration;
import libsvm.svm_model;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 *
 */
public class SvmMetaInformationImpl implements SvmMetaInformation {
    private final svm_model svmModel;
    private SvmConfiguration svmConfiguration;
    private final String modelName;

    public SvmMetaInformationImpl(NativeSvmModelWrapper svmModel, SvmConfiguration svmConfiguration, String modelName) {
        this.svmModel = svmModel.getSvmModel();
        this.svmConfiguration = svmConfiguration;
        this.modelName = modelName;
    }

    @Override
    public String getModelName() {
        return modelName;
    }

    @Override
    public SvmConfiguration getSvmConfiguration() {
        return svmConfiguration;
    }

    @Override
    public int getNumberOfClasses() {
        return svmModel.nr_class;
    }

    @Override
    public int getAmountOfSupportVectors() {
        return svmModel.l;
    }

    @Override
    public List<Double> getRhoConstants() {
        return svmModel.rho == null ? new ArrayList<>() : Arrays.asList((ArrayUtils.toObject((svmModel.rho))));
    }

    @Override
    public List<Integer> getLabelForEachClass() {
        //in one-class svm case, NO class labels are set while processing
        if (svmModel.label == null) {
            return new ArrayList<>();
        } else {
            return Arrays.asList((ArrayUtils.toObject((svmModel.label))));
        }
    }

    @Override
    public List<Double> getProbabilityA() {
        return svmModel.probA == null ? new ArrayList<>() : Arrays.asList((ArrayUtils.toObject((svmModel
                .probA))));
    }

    @Override
    public List<Double> getProbabilityB() {
        return svmModel.probB == null ? new ArrayList<>() : Arrays.asList((ArrayUtils.toObject((svmModel
                .probB))));
    }

    @Override
    public List<Integer> getNumberOfSupportVectorsForEachClass() {
        return svmModel.nSV == null ? new ArrayList<>() : Arrays.asList((ArrayUtils.toObject((svmModel.nSV))));
    }

    @Override
    public void setSvmConfiguration(SvmConfiguration svmConfiguration) {
        this.svmConfiguration = svmConfiguration;
    }

    @Override
    public void setNumberOfClasses(int numberOfClasses) {
        svmModel.nr_class = numberOfClasses;
    }

    @Override
    public void setAmountOfSupportVectors(int amountOfSupportVectors) {
        svmModel.l = amountOfSupportVectors;
    }

    @Override
    public void setRhoConstants(List<Double> rhoConstants) {
        svmModel.rho = (ArrayUtils.toPrimitive(rhoConstants.toArray(new Double[0])));
    }

    @Override
    public void setLabelForEachClass(List<Integer> labelForEachClass) {
        svmModel.label = (ArrayUtils.toPrimitive(labelForEachClass.toArray(new Integer[0])));
    }

    @Override
    public void setProbabilityA(List<Double> probabilityA) {
        svmModel.probA = (ArrayUtils.toPrimitive(probabilityA.toArray(new Double[0])));
    }

    @Override
    public void setProbabilityB(List<Double> probabilityB) {
        svmModel.probB = (ArrayUtils.toPrimitive(probabilityB.toArray(new Double[0])));
    }

    @Override
    public void setNumberOfSupportVectorsForEachClass(List<Integer> numberOfSupportVectorsForEachClass) {
        svmModel.nSV = (ArrayUtils.toPrimitive(numberOfSupportVectorsForEachClass.toArray(new Integer[0])));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SvmMetaInformationImpl that = (SvmMetaInformationImpl) o;
        return Objects.equals(modelName, that.modelName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(modelName);
    }
}
