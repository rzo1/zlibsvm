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

import de.hhn.mi.domain.SvmDocument;
import de.hhn.mi.domain.SvmMetaInformation;
import de.hhn.mi.domain.SvmModel;
import de.hhn.mi.shade.org.apache.commons.lang3.ArrayUtils;
import de.hhn.mi.util.PrimitiveHelper;
import libsvm.svm_model;

import java.util.List;

/**
 * A {@link AbstractSvmClassifier} encapsulates common issues occurring while classification process of LIBSVM.
 */
abstract class AbstractSvmClassifier extends AbstractSvmTool implements SvmClassifier {

    private final svm_model svmModel;

    AbstractSvmClassifier(SvmModel svmModel) {
        assert (svmModel != null);
        this.svmModel = unwrap(svmModel);
    }

    private svm_model unwrap(SvmModel svmModel) {
        svm_model model = new svm_model();

        SvmMetaInformation metaInformation = svmModel.getMetaInformation();
        model.param = (unwrap(metaInformation.getSvmConfiguration()));

        model.l =(metaInformation.getAmountOfSupportVectors());
        model.nr_class = (metaInformation.getNumberOfClasses());

        List<Double> rhoConstants = metaInformation.getRhoConstants();
        model.rho = (ArrayUtils.toPrimitive(rhoConstants.toArray(new Double[0])));
        List<Double> probabilityA = metaInformation.getProbabilityA();
        model.probA =(ArrayUtils.toPrimitive(probabilityA.toArray(new Double[0])));
        List<Double> probabilityB = metaInformation.getProbabilityB();
        model.probB =(ArrayUtils.toPrimitive(probabilityB.toArray(new Double[0])));
        List<Integer> labelForEachClass = metaInformation.getLabelForEachClass();
        model.label =((ArrayUtils.toPrimitive(labelForEachClass.toArray(new Integer[0]))));
        List<Integer> numberOfSupportVectorsForEachClass = metaInformation.getNumberOfSupportVectorsForEachClass();
        model.nSV= (ArrayUtils.toPrimitive(numberOfSupportVectorsForEachClass.toArray(new Integer[0])));

        model.sv_coef =(PrimitiveHelper.doubleMapTo2dArray(svmModel.getSvCoefficients()));
        model.SV =  PrimitiveHelper.svmFeatureMapTo2dArray(svmModel.getSupportVectors());

        return model;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public abstract List<SvmDocument> classify(List<SvmDocument> documents, boolean probabilityEstimates);

   svm_model getSvmModel() {
        return svmModel;
    }

}
