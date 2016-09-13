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
package de.hhn.mi.process;

import de.hhn.mi.configuration.SvmType;
import de.hhn.mi.domain.SvmDocument;
import de.hhn.mi.exception.ClassificationCoreException;

import java.util.List;

/**
 * A Classifier using LIBSVM for classification.
 *
 * @author rz
 */
public interface SvmClassifier {

    /**
     * Classifies the given documents.
     *
     * @param documents            must not be <code>null</code>  or an empty {@link List list}.
     * @param probabilityEstimates <code>true</code>  for probability estimates else false. Note well,
     *                             that this is only supported by SVC or SVR SVMs(see {@link SvmType})
     * @return classified documents
     * @throws AssertionError Thrown if a given parameter is invalid.
     * @throws ClassificationCoreException Thrown if the underlaying model does not support probability estimates.
     */
    List<SvmDocument> classify(List<SvmDocument> documents, boolean probabilityEstimates);


}
