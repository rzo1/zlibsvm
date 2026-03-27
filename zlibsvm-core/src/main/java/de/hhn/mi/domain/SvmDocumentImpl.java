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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Default implementation of {@link SvmDocument}.
 */
public class SvmDocumentImpl implements SvmDocument {

    private final List<SvmFeature> features;
    private final List<SvmClassLabel> classLabels = new ArrayList<>();

    /**
     * @param features the feature vector for this document; must not be {@code null}
     */
    public SvmDocumentImpl(List<SvmFeature> features) {
        if (features == null) {
            throw new IllegalArgumentException("features must not be null");
        }
        this.features = features;
    }

    @Override
    public List<SvmFeature> getSvmFeatures() {
        return Collections.unmodifiableList(features);
    }

    @Override
    public SvmClassLabel getClassLabelWithHighestProbability() {
        if (classLabels.isEmpty()) {
            return null;
        }
        return Collections.max(classLabels);
    }

    @Override
    public List<SvmClassLabel> getAllClassLabels() {
        return Collections.unmodifiableList(classLabels);
    }

    @Override
    public void addClassLabel(SvmClassLabel classLabel) {
        if (classLabel == null) {
            throw new IllegalArgumentException("classLabel must not be null");
        }
        this.classLabels.add(classLabel);
    }
}
