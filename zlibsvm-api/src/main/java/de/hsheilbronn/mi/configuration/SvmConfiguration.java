/*-
 * ========================LICENSE_START=================================
 * zlibsvm-api
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
package de.hsheilbronn.mi.configuration;

import java.io.Serializable;
import java.util.List;

/**
 * A svm configuration encapsulating necessary LIBSVM parameters.
 *
 * @author rz
 */
public interface SvmConfiguration  extends Serializable {

    SvmType getSvmType();

    KernelType getKernelType();

    int getDegree();

    double getCoef0();

    double getGamma();

    double getNu();

    double getCacheSize();

    double getCost();

    double getEps();

    double getP();

    int getShrinking();

    int getProbability();

    int getNrWeight();

    List<Double> getWeight();

    List<Integer> getWeightLabel();

    int getCrossValidation();

    int getNFold();

    boolean isQuietMode();
}
