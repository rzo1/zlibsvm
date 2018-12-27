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
package de.hhn.mi.exception;

/**
 * A {@link ClassificationCoreException} is thrown, if something went wrong in the LIBSVM internals
 * and cannot be automatically solved (for example: calculation is not feasible).
 *
 * @author rz
 */
public class ClassificationCoreException extends RuntimeException {

    public ClassificationCoreException(String message) {
        super(message);
    }

    public ClassificationCoreException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
