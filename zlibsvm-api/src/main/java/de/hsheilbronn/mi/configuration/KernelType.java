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

import de.hsheilbronn.mi.exception.ClassificationCoreException;

/**
 * @author rz
 */
public enum KernelType {

    LINEAR(0), POLYNOMIAL(1), RBF(2), SIGMOID(3), PRECOMPUTED(4);

    private final int numericType;

    KernelType(int numericType) {
        this.numericType = numericType;
    }

    public int getNumericType() {
        return numericType;
    }

    /**
     * @param kernelType the numeric kernel type
     * @return the fitting {@link KernelType}
     * @throws ClassificationCoreException if there is no kernel for the given argument.
     */
    public static KernelType getByValue(int kernelType) {
        switch (kernelType) {
            case 0:
                return LINEAR;
            case 1:
                return POLYNOMIAL;
            case 2:
                return RBF;
            case 3:
                return SIGMOID;
            case 4:
                return PRECOMPUTED;
            default:
                throw new ClassificationCoreException("unknown kernel type");
        }

    }

}
