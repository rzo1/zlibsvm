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
package de.hsheilbronn.mi.configuration;

import de.hsheilbronn.mi.exception.ClassificationCoreException;

/**
 * @author rz
 */
public enum SvmType {

    C_SVC(0), NU_SVC(1), ONE_CLASS(2), EPSILON_SVR(3), NU_SVR(4);

    private final int numericType;

    SvmType(int numericType) {
        this.numericType = numericType;
    }

    public int getNumericType() {
        return numericType;
    }

    /**
     * @param svmType the numeric svm type
     * @return the fitting {@link SvmType}
     * @throws ClassificationCoreException if there is no kernel for the given argument.
     */
    public static SvmType getByValue(int svmType) {
        switch (svmType) {
            case 0:
                return C_SVC;
            case 1:
                return NU_SVC;
            case 2:
                return ONE_CLASS;
            case 3:
                return EPSILON_SVR;
            case 4:
                return NU_SVR;
            default:
                throw new ClassificationCoreException("unknown svm type");
        }

    }

}
