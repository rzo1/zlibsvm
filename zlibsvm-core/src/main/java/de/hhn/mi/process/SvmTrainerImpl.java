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
package de.hhn.mi.process;

import de.hhn.mi.configuration.KernelType;
import de.hhn.mi.configuration.SvmConfiguration;
import de.hhn.mi.configuration.SvmType;
import de.hhn.mi.domain.NativeSvmModelWrapper;
import de.hhn.mi.domain.SvmDocument;
import de.hhn.mi.domain.SvmModel;
import de.hhn.mi.domain.SvmModelImpl;
import de.hhn.mi.exception.ClassificationCoreException;
import libsvm.svm;
import libsvm.svm_node;
import libsvm.svm_problem;
import org.slf4j.Logger;

import java.util.List;

public class SvmTrainerImpl extends AbstractSvmTrainer {

    private static final Logger logger = org.slf4j.LoggerFactory
            .getLogger(SvmTrainerImpl.class);


    /**
     * @param configuration must not be {@code null}       .
     * @throws AssertionError if a given parameter is invalid.
     */
    public SvmTrainerImpl(SvmConfiguration configuration, String modelName) {
        super(configuration, modelName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected svm_problem loadTrainingProblem(List<SvmDocument> documents) {

        //build svm problem
        svm_problem problem = new svm_problem();
        problem.l = (documents.size());

        int maxIndex = 0;

        svm_node[][] svmFeaturesPerDocument = new svm_node[problem.l][];

        double[] numericClassLabel = new double[problem.l];

        for (int i = 0; i < documents.size(); i++) {
            SvmDocument svmDocument = documents.get(i);

            int size = svmDocument.getSvmFeatures().size();


            svm_node[] svmFeatures = super.readProblem(svmDocument);
            if (size > 0) {
                maxIndex = Math.max(maxIndex, svmFeatures[size - 1].index);
            }

            svmFeaturesPerDocument[i] = svmFeatures;
            numericClassLabel[i] = svmDocument.getClassLabelWithHighestProbability().getNumeric();
        }


        problem.x = svmFeaturesPerDocument;
        problem.y = (numericClassLabel);


        if (getParam().gamma == 0 && maxIndex > 0)
            getParam().gamma = (1.0 / maxIndex);

        if (getParam().kernel_type == KernelType.PRECOMPUTED.getNumericType())
            for (int i = 0; i < problem.l; i++) {
                if (getProblem().x[i][0].index != 0) {
                    String msg = "Wrong kernel matrix: first column must be 0:sample_serial_number";
                    throw new ClassificationCoreException(msg);
                }
                if ((int) problem.x[i][0].value <= 0
                        || (int) problem.x[i][0].value > maxIndex) {
                    String msg = "Wrong input format: sample_serial_number out of range";
                    throw new ClassificationCoreException(msg);
                }
            }


        return problem;
    }

    protected double doCrossValidation(SvmConfiguration configuration) {
        int i;
        int amountOfCorrect = 0;
        double amountOfErrors = 0;
        double sumv = 0, sumy = 0, sumvv = 0, sumyy = 0, sumvy = 0;
        double[] target = new double[getProblem().l];

        svm.svm_cross_validation(getProblem(), getParam(), configuration.getNFold(), target);
        if (getParam().svm_type == SvmType.EPSILON_SVR.getNumericType()
                || getParam().svm_type == SvmType.NU_SVR.getNumericType()) {
            for (i = 0; i < getProblem().l; i++) {
                double y = getProblem().y[i];
                double v = target[i];
                amountOfErrors += (v - y) * (v - y);
                sumv += v;
                sumy += y;
                sumvv += v * v;
                sumyy += y * y;
                sumvy += v * y;
            }
            logger.info("Cross Validation Mean squared error = "
                    + amountOfErrors / getProblem().l);

            double scc = ((getProblem().l * sumvy - sumv * sumy) * (getProblem().l * sumvy - sumv
                    * sumy))
                    / ((getProblem().l * sumvv - sumv * sumv) * (getProblem().l * sumyy - sumy
                    * sumy));
            logger.info("Cross Validation Squared correlation coefficient = "
                    + scc);
            return scc;
        } else {
            for (i = 0; i < getProblem().l; i++)
                if (target[i] == getProblem().y[i])
                    ++amountOfCorrect;

            double accuracy = 100.0
                    * amountOfCorrect / getProblem().l;
            logger.info("Cross Validation Accuracy = " + accuracy);
            return accuracy;

        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateConfiguration() {
        //not nice, but library issue
        String errorMsg = svm.svm_check_parameter(getProblem(), getParam());
        if (errorMsg != null) {
            throw new ClassificationCoreException("Error: " + errorMsg);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected SvmModel getTrainedModel() {
        return new SvmModelImpl(getModelName(), new NativeSvmModelWrapper(svm.svm_train(getProblem(),
                getParam())));
    }


}
