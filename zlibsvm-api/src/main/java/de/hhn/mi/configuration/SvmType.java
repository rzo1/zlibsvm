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
 * Enumerates the SVM formulations supported by LIBSVM.
 *
 * <ul>
 *   <li>{@link #C_SVC} — C-Support Vector Classification (multi-class)</li>
 *   <li>{@link #NU_SVC} — nu-Support Vector Classification (multi-class)</li>
 *   <li>{@link #ONE_CLASS} — one-class SVM (distribution estimation)</li>
 *   <li>{@link #EPSILON_SVR} — epsilon-Support Vector Regression</li>
 *   <li>{@link #NU_SVR} — nu-Support Vector Regression</li>
 * </ul>
 */
public enum SvmType {

    C_SVC(0), NU_SVC(1), ONE_CLASS(2), EPSILON_SVR(3), NU_SVR(4);

    private final int numericType;

    SvmType(int numericType) {
        this.numericType = numericType;
    }

    /**
     * @return the LIBSVM numeric identifier for this SVM type
     */
    public int getNumericType() {
        return numericType;
    }

    /**
     * Resolves an {@link SvmType} from its LIBSVM numeric identifier.
     *
     * @param svmType the numeric svm type (0-4)
     * @return the corresponding {@link SvmType}
     * @throws ClassificationCoreException if the given value does not map to a known SVM type
     */
    public static SvmType getByValue(int svmType) {
        return switch (svmType) {
            case 0 -> C_SVC;
            case 1 -> NU_SVC;
            case 2 -> ONE_CLASS;
            case 3 -> EPSILON_SVR;
            case 4 -> NU_SVR;
            default -> throw new ClassificationCoreException("unknown svm type");
        };

    }

}
