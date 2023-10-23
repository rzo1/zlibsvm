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
import de.hhn.mi.domain.SvmFeature;
import libsvm.svm;
import libsvm.svm_node;
import libsvm.svm_parameter;
import org.apache.commons.lang3.ArrayUtils;

import java.util.List;

/**
 * An {@link AbstractSvmTool} encapsulates some common issues occurring while working with the LIBSVM library.
 *
 *
 */
public abstract class AbstractSvmTool {

    public static svm_parameter unwrap(SvmConfiguration configuration) {
        svm_parameter param = new svm_parameter();

        param.svm_type = configuration.getSvmType().getNumericType();
        param.kernel_type = configuration.getKernelType().getNumericType();
        param.degree = configuration.getDegree();
        param.gamma = configuration.getGamma();
        param.coef0 = configuration.getCoef0();
        param.nu = configuration.getNu();
        param.cache_size = configuration.getCacheSize();
        param.C = configuration.getCost();
        param.eps = configuration.getEps();
        param.p = configuration.getP();
        param.shrinking = configuration.getShrinking();
        param.probability = configuration.getProbability();
        param.nr_weight = configuration.getNrWeight();

        List<Integer> weightLabel = configuration.getWeightLabel();

        param.weight_label = ArrayUtils.toPrimitive(weightLabel.toArray(new Integer[0]));
        List<Double> weight = configuration.getWeight();

        param.weight = ArrayUtils.toPrimitive(weight.toArray(new Double[0]));

        if(configuration.isQuietMode()) {
            svm.svm_set_print_string_function(s -> {
                //nothing to do here...
            });
        }
        return param;
    }

    public svm_node[] readProblem(SvmDocument document) {
        List<SvmFeature> svmFeatures = document.getSvmFeatures();
        int size = svmFeatures.size();
        svm_node[] x = new svm_node[size];
        int i = 0;
        for (SvmFeature svmFeature : svmFeatures) {
            x[i] = new svm_node();
            x[i].value = svmFeature.getValue();
            x[i].index = svmFeature.getIndex();
            i++;
        }
        return x;
    }
}
