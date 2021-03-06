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
package de.hhn.mi.mock;

import de.hhn.mi.domain.SvmFeature;

public class SvmFeatureMock implements SvmFeature {

    private int index;
    private double value;

    public SvmFeatureMock(int index, double value) {
        this.index = index;
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public double getValue() {
        return value;
    }

    public void setIndex(int index) {
        this.index = index;

    }

    public void setValue(double value) {
        this.value = value;

    }

    @Override
    public int compareTo(SvmFeature o) {
        throw new UnsupportedOperationException("not needed for mock");
    }
}
