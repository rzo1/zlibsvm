/*-
 * ========================LICENSE_START=================================
 * zlibsvm-core
 * %%
 * Copyright (C) 2014 - 2017 Heilbronn University - Medical Informatics
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

import de.hsheilbronn.mi.configuration.KernelType;
import de.hsheilbronn.mi.configuration.SvmConfigurationImpl;
import de.hsheilbronn.mi.configuration.SvmType;
import de.hsheilbronn.mi.util.PrimitiveHelper;
import libsvm.svm_model;
import libsvm.svm_parameter;

import java.util.List;
import java.util.Map;


/**
 * @author rz
 */
public class SvmModelImpl implements SvmModel {

    private SvmMetaInformation svmMetaInformation;
    private svm_model svmModel;

    /**
     * @param svmModel must not be <code>null</code>
     * @throws AssertionError if a given parameter is invalid.
     */
    public SvmModelImpl(String modelName, NativeSvmModelWrapper svmModel) {
        assert (svmModel != null);
        this.svmModel = svmModel.getSvmModel();
        svm_parameter svmParameter = this.svmModel.param;
        svmMetaInformation = new SvmMetaInformationImpl(svmModel, new SvmConfigurationImpl.Builder().setSvmType
                (SvmType.getByValue(svmParameter.svm_type)).setKernelType(KernelType.getByValue(svmParameter
                .kernel_type)).setDegree(svmParameter.degree).setGamma(svmParameter.gamma).setCoef0(svmParameter
                .coef0).setProbability(svmParameter.probability == 1 ?
                true : false).build(), modelName);

    }

    @Override
    public SvmMetaInformation getMetaInformation() {
        return svmMetaInformation;
    }

    @Override
    public Map<Integer, List<Double>> getSvCoefficients() {
        return PrimitiveHelper.double2dArrayToMap(svmModel.sv_coef);
    }

    @Override
    public Map<Integer, List<SvmFeature>> getSupportVectors() {
        return PrimitiveHelper.svmFeature2dArrayToMap(svmModel.SV);
    }


}
