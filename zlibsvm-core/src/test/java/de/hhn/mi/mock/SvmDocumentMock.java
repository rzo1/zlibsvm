/*-
 * ========================LICENSE_START=================================
 * zlibsvm-core
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
package de.hhn.mi.mock;

import de.hhn.mi.domain.SvmClassLabel;
import de.hhn.mi.domain.SvmDocument;
import de.hhn.mi.domain.SvmFeature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SvmDocumentMock implements SvmDocument {

    private final List<SvmFeature> features;
    private List<SvmClassLabel> classLabels = new ArrayList<>();

    public SvmDocumentMock(List<SvmFeature> features) {
        this.features = features;
    }

    public List<SvmFeature> getSvmFeatures() {
        return features;
    }

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
        assert (classLabel != null);
        this.classLabels.add(classLabel);
    }


}
