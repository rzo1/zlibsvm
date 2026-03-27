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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Wraps a native LIBSVM {@link svm_model} and provides a {@link Builder} to construct one
 * from higher-level domain objects. Serves as a bridge between the zlibsvm domain model
 * and LIBSVM's raw data structures.
 *
 * @param svmModel the native LIBSVM model
 */
public record NativeSvmModelWrapper(svm_model svmModel) {

    /**
     * @return the underlying native LIBSVM {@link svm_model}
     */
    public svm_model getSvmModel() {
        return svmModel();
    }

    /**
     * Builder for constructing a {@link NativeSvmModelWrapper} from high-level domain objects.
     *
     * <p>Required fields: {@link #configuration}, {@link #numberOfClasses}, {@link #amountOfSupportVectors},
     * {@link #rhoConstants}. All other fields have sensible defaults (empty lists / null).
     */
    public static class Builder {
        private SvmConfigurationBuilder configuration;
        private boolean probabilityEstimates;
        private List<Double> rhoConstants = new ArrayList<>();
        private List<Integer> labelForEachClass = new ArrayList<>();
        private List<Double> probabilityA = new ArrayList<>();
        private List<Double> probabilityB = new ArrayList<>();
        private List<Integer> numberOfSVforEachClass = new ArrayList<>();
        private int numberOfClasses;
        private int amountOfSupportVectors;
        private Map<Integer, List<Double>> svCoefficients;
        private Map<Integer, List<SvmFeature>> supportVectors;

        /** @param configuration the configuration builder to derive LIBSVM parameters from */
        public Builder configuration(SvmConfigurationBuilder configuration) {
            this.configuration = configuration;
            return this;
        }

        /** @param probabilityEstimates {@code true} if probability estimates are enabled */
        public Builder probabilityEstimates(boolean probabilityEstimates) {
            this.probabilityEstimates = probabilityEstimates;
            return this;
        }

        /** @param rhoConstants the rho constants for the decision functions */
        public Builder rhoConstants(List<Double> rhoConstants) {
            this.rhoConstants = rhoConstants;
            return this;
        }

        /** @param labelForEachClass the numeric labels for each class */
        public Builder labelForEachClass(List<Integer> labelForEachClass) {
            this.labelForEachClass = labelForEachClass;
            return this;
        }

        /** @param probabilityA the pairwise probability coefficients A */
        public Builder probabilityA(List<Double> probabilityA) {
            this.probabilityA = probabilityA;
            return this;
        }

        /** @param probabilityB the pairwise probability coefficients B */
        public Builder probabilityB(List<Double> probabilityB) {
            this.probabilityB = probabilityB;
            return this;
        }

        /** @param numberOfSVforEachClass the number of support vectors per class */
        public Builder numberOfSVforEachClass(List<Integer> numberOfSVforEachClass) {
            this.numberOfSVforEachClass = numberOfSVforEachClass;
            return this;
        }

        /** @param numberOfClasses the total number of classes */
        public Builder numberOfClasses(int numberOfClasses) {
            this.numberOfClasses = numberOfClasses;
            return this;
        }

        /** @param amountOfSupportVectors the total number of support vectors */
        public Builder amountOfSupportVectors(int amountOfSupportVectors) {
            this.amountOfSupportVectors = amountOfSupportVectors;
            return this;
        }

        /** @param svCoefficients the support-vector coefficients, keyed by row index */
        public Builder svCoefficients(Map<Integer, List<Double>> svCoefficients) {
            this.svCoefficients = svCoefficients;
            return this;
        }

        /** @param supportVectors the support vectors, keyed by row index */
        public Builder supportVectors(Map<Integer, List<SvmFeature>> supportVectors) {
            this.supportVectors = supportVectors;
            return this;
        }

        /**
         * Builds a new {@link NativeSvmModelWrapper}.
         *
         * @return the constructed wrapper
         * @throws IllegalArgumentException if {@code configuration} is {@code null}
         */
        public NativeSvmModelWrapper build() {
            if (configuration == null) {
                throw new IllegalArgumentException("configuration must not be null");
            }

            svm_model model = new svm_model();
            model.l = amountOfSupportVectors;
            model.nr_class = numberOfClasses;
            model.rho = ArrayUtils.toPrimitive(rhoConstants.toArray(new Double[0]));
            model.probA = ArrayUtils.toPrimitive(probabilityA.toArray(new Double[0]));
            model.probB = ArrayUtils.toPrimitive(probabilityB.toArray(new Double[0]));
            model.label = ArrayUtils.toPrimitive(labelForEachClass.toArray(new Integer[0]));
            model.nSV = ArrayUtils.toPrimitive(numberOfSVforEachClass.toArray(new Integer[0]));

            if (svCoefficients != null) {
                model.sv_coef = PrimitiveHelper.doubleMapTo2dArray(svCoefficients);
            }
            if (supportVectors != null) {
                model.SV = PrimitiveHelper.svmFeatureMapTo2dArray(supportVectors);
            }

            model.param = AbstractSvmTool.unwrap(
                    configuration.setProbability(probabilityEstimates).build());

            return new NativeSvmModelWrapper(model);
        }
    }
}
