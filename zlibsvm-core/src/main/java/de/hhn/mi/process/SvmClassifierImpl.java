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

import de.hhn.mi.configuration.SvmType;
import de.hhn.mi.domain.SvmClassLabel;
import de.hhn.mi.domain.SvmClassLabelImpl;
import de.hhn.mi.domain.SvmDocument;
import de.hhn.mi.domain.SvmModel;
import de.hhn.mi.exception.ClassificationCoreException;
import libsvm.svm;
import libsvm.svm_node;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;


/**
 * (partly)
 */
public class SvmClassifierImpl extends AbstractSvmClassifier {

    private static final Logger logger = org.slf4j.LoggerFactory
            .getLogger(SvmClassifierImpl.class);

    public SvmClassifierImpl(SvmModel svmModel) {
        super(svmModel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SvmDocument> classify(List<SvmDocument> documents, boolean probabilityEstimates) {
        assert (documents != null);
        assert (!documents.isEmpty());

        if (probabilityEstimates) {
            if (svm.svm_check_probability_model(getSvmModel()) == 0) {
                String msg = "Model does not support probability estimates";
                throw new ClassificationCoreException(msg);
            }
        } else {
            if (svm.svm_check_probability_model(getSvmModel()) != 0) {
                logger.info("Model supports probability estimates, but disabled in prediction.");
            }
        }

        int correctEstimated = 0;
        int totalEstimated = 0;
        double error = 0;
        double sumOfEstimatedClassLabels = 0, sumOfTargetClassLabels = 0, sumOfEstimatedClassLabelsSquared = 0,
                sumOfTargetClassLabelsSquared = 0, sumMultipliedEstimatedTargetClassLabels = 0;

        final SvmType svmType = SvmType.getByValue(svm.svm_get_svm_type(getSvmModel()));
        final int numberOfClasses = svm.svm_get_nr_class(getSvmModel());
        final double[] estimatedProbabilities = new double[numberOfClasses];
        final int[] labels = new int[numberOfClasses];

        if (probabilityEstimates) {
            if (svmType == SvmType.EPSILON_SVR
                    || svmType == SvmType.NU_SVR) {
                logger
                        .info("Prob. model for test data: target value = predicted value + z,  " +
                                "z: Laplace distribution e^(-|z|/sigma)/(2sigma),sigma="
                                + svm.svm_get_svr_probability(getSvmModel()));
            } else {
                svm.svm_get_labels(getSvmModel(), labels);
            }
        }
        boolean validate = false;
        for (SvmDocument svmDocument : documents) {
            validate = (svmDocument.getClassLabelWithHighestProbability() != null);

            final svm_node[] x = super.readProblem(svmDocument);

            double predictedClassLabel;
            final List<SvmClassLabel> classLabels = new ArrayList<>();

            if (probabilityEstimates
                    && (svmType == SvmType.C_SVC || svmType == SvmType.NU_SVC)) {
                predictedClassLabel = svm.svm_predict_probability(getSvmModel(), x, estimatedProbabilities);

                for (int i = 0; i < numberOfClasses; i++) {
                    classLabels.add(new SvmClassLabelImpl(labels[i], String.valueOf(labels[i]), estimatedProbabilities[i]));
                }
            } else {
                predictedClassLabel = svm.svm_predict(getSvmModel(), x);
                classLabels.add(new SvmClassLabelImpl(predictedClassLabel));
            }

            //classLabel is only set, if the former classLabel was null
            if (validate) {
                double targetClassLabel = svmDocument.getClassLabelWithHighestProbability().getNumeric();
                if (predictedClassLabel == targetClassLabel)
                    ++correctEstimated;
                // some regression correlation coefficient issue
                error += (predictedClassLabel - targetClassLabel) * (predictedClassLabel - targetClassLabel);
                sumOfEstimatedClassLabels += predictedClassLabel;
                sumOfTargetClassLabels += targetClassLabel;
                sumOfEstimatedClassLabelsSquared += predictedClassLabel * predictedClassLabel;
                sumOfTargetClassLabelsSquared += targetClassLabel * targetClassLabel;
                sumMultipliedEstimatedTargetClassLabels += predictedClassLabel * targetClassLabel;
                ++totalEstimated;
            } else {
                for (SvmClassLabel classLabel : classLabels) {
                    svmDocument.addClassLabel(classLabel);
                }
            }
        }

        if (validate) {
            if (svmType == SvmType.EPSILON_SVR
                    || svmType == SvmType.NU_SVR) {
                logger.info("Mean squared error = " + error / totalEstimated
                        + " (regression)\n");
                logger.info("Squared correlation coefficient = "
                        + ((totalEstimated * sumMultipliedEstimatedTargetClassLabels -
                        sumOfEstimatedClassLabels * sumOfTargetClassLabels) * (totalEstimated *
                        sumMultipliedEstimatedTargetClassLabels - sumOfEstimatedClassLabels
                        * sumOfTargetClassLabels))
                        / ((totalEstimated * sumOfEstimatedClassLabelsSquared - sumOfEstimatedClassLabels *
                        sumOfEstimatedClassLabels) * (totalEstimated * sumOfTargetClassLabelsSquared -
                        sumOfTargetClassLabels
                                * sumOfTargetClassLabels)) + " (regression)\n");
            } else
                logger.info("Accuracy = " + (double) correctEstimated / totalEstimated * 100
                        + "% (" + correctEstimated + "/" + totalEstimated + ") (classification)");
        }
        return documents;
    }


}
