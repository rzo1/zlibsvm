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

import de.hhn.mi.configuration.SvmConfigurationBuilder;
import de.hhn.mi.process.AbstractSvmTool;
import de.hhn.mi.shade.org.apache.commons.lang3.ArrayUtils;
import de.hhn.mi.util.PrimitiveHelper;
import libsvm.svm_model;

import java.util.List;
import java.util.Map;

/**
 *
 */
public record NativeSvmModelWrapper(svm_model svmModel) {

    public NativeSvmModelWrapper(SvmConfigurationBuilder svmConfigurationBuilder, int probabilityEstimates,
                                 List<Double> rhoConstants, List<Integer>
            labelForEachClass, List<Double> probabilityA, List<Double> probabilityB, List<Integer>
                                         numberOfSVforEachClass, int numberOfClasses, int amountOfSupportVectors) {

        this(svmConfigurationBuilder, probabilityEstimates, rhoConstants, labelForEachClass, probabilityA, probabilityB,
                numberOfSVforEachClass, numberOfClasses, amountOfSupportVectors, null, null);
    }

    public NativeSvmModelWrapper(SvmConfigurationBuilder svmConfigurationBuilder, int probabilityEstimates,
                                 List<Double> rhoConstants, List<Integer>
            labelForEachClass, List<Double> probabilityA, List<Double> probabilityB, List<Integer>
                                         numberOfSVforEachClass, int numberOfClasses, int amountOfSupportVectors,
                                 Map<Integer, List<Double>> svCoefficients, Map<Integer, List<SvmFeature>>
                                         supportVectors) {

        this(new svm_model());

        svmModel.l = amountOfSupportVectors;
        svmModel.nr_class = numberOfClasses;

        svmModel.rho = (ArrayUtils.toPrimitive(rhoConstants.toArray(new Double[0])));
        svmModel.probA = (ArrayUtils.toPrimitive(probabilityA.toArray(new Double[0])));
        svmModel.probB = (ArrayUtils.toPrimitive(probabilityB.toArray(new Double[0])));
        svmModel.label = ((ArrayUtils.toPrimitive(labelForEachClass.toArray(new Integer[0]))));
        svmModel.nSV = (ArrayUtils.toPrimitive(numberOfSVforEachClass.toArray(new Integer[0])));

        if (svCoefficients != null) {
            svmModel.sv_coef = (PrimitiveHelper.doubleMapTo2dArray(svCoefficients));
        }

        if (supportVectors != null) {
            svmModel.SV = (PrimitiveHelper.svmFeatureMapTo2dArray(supportVectors));
        }

        svmModel.param = AbstractSvmTool.unwrap((svmConfigurationBuilder.setProbability(probabilityEstimates == 1).build()));
    }

    public svm_model getSvmModel() {
        return svmModel();
    }

}
