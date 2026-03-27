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

import de.hhn.mi.exception.ClassificationCoreException;

/**
 * Enumerates the kernel functions supported by LIBSVM.
 *
 * <ul>
 *   <li>{@link #LINEAR} — u'*v</li>
 *   <li>{@link #POLYNOMIAL} — (gamma*u'*v + coef0)^degree</li>
 *   <li>{@link #RBF} — exp(-gamma*|u-v|^2)</li>
 *   <li>{@link #SIGMOID} — tanh(gamma*u'*v + coef0)</li>
 *   <li>{@link #PRECOMPUTED} — kernel values in training data</li>
 * </ul>
 */
public enum KernelType {

    LINEAR(0), POLYNOMIAL(1), RBF(2), SIGMOID(3), PRECOMPUTED(4);

    private final int numericType;

    KernelType(int numericType) {
        this.numericType = numericType;
    }

    /**
     * @return the LIBSVM numeric identifier for this kernel type
     */
    public int getNumericType() {
        return numericType;
    }

    /**
     * @param kernelType the numeric kernel type
     * @return the fitting {@link KernelType}
     * @throws ClassificationCoreException if there is no kernel for the given argument.
     */
    public static KernelType getByValue(int kernelType) {
        return switch (kernelType) {
            case 0 -> LINEAR;
            case 1 -> POLYNOMIAL;
            case 2 -> RBF;
            case 3 -> SIGMOID;
            case 4 -> PRECOMPUTED;
            default -> throw new ClassificationCoreException("unknown kernel type");
        };

    }

}
