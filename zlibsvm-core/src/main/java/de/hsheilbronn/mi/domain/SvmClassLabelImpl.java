/*-
 * ========================LICENSE_START=================================
 * zlibsvm-core
 * %%
 * Copyright (C) 2014 - 2017 Heilbronn University - Medical Informatics
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
package de.hsheilbronn.mi.domain;

/**
 * @author rz
 */
public class SvmClassLabelImpl implements SvmClassLabel {

    private double numeric;
    private String name;
    private Double probability = 1.0d;

    /**
     * /** The probability is by default set to 1.0d.
     *
     * @param numeric No restrictions on this parameter
     */
    public SvmClassLabelImpl(double numeric) {
        this.numeric = numeric;
    }

    /**
     * The probability is by default set to 1.0d.
     *
     * @param numeric No restrictions on this parameter
     * @param name    No restrictions on this parameter
     */
    public SvmClassLabelImpl(double numeric, String name) {
        this.numeric = numeric;
        this.name = name;
    }

    @Override
    public double getNumeric() {
        return numeric;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getProbability() {
        return probability;
    }

    @Override
    public void setProbability(double probability) {
        assert (probability <= 1.0d);
        this.probability = probability;
    }


    @Override
    public int compareTo(SvmClassLabel o) {
        return this.probability.compareTo(o.getProbability());
    }
}
