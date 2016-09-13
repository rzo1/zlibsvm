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
package de.hsheilbronn.mi.process;

import de.hsheilbronn.mi.configuration.SvmType;
import de.hsheilbronn.mi.domain.SvmClassLabel;
import de.hsheilbronn.mi.domain.SvmClassLabelImpl;
import de.hsheilbronn.mi.domain.SvmDocument;
import de.hsheilbronn.mi.domain.SvmModel;
import de.hsheilbronn.mi.exception.ClassificationCoreException;
import libsvm.svm_node;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;


/**
 * @author rz (partly)
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
            if (getSvmEngine().svm_check_probability_model(getSvmModel()) == 0) {
                String msg = "Model does not support probability estimates";
                throw new ClassificationCoreException(msg);
            }
        } else {
            if (getSvmEngine().svm_check_probability_model(getSvmModel()) != 0) {
                logger.info("Model supports probability estimates, but disabled in prediction.");
            }
        }

        int correctEstimated = 0;
        int totalEstimated = 0;
        double error = 0;
        double sumOfEstimatedClassLabels = 0, sumOfTargetClassLabels = 0, sumOfEstimatedClassLabelsSquared = 0,
                sumOfTargetClassLabelsSquared = 0, sumMultipliedEstimatedTargetClassLabels = 0;

        SvmType svmType = SvmType.getByValue(getSvmEngine().svm_get_svm_type(getSvmModel()));
        int numberOfClasses = getSvmEngine().svm_get_nr_class(getSvmModel());
        double[] estimatedProbabilities = new double[numberOfClasses];
        int[] labels = new int[numberOfClasses];

        if (probabilityEstimates) {
            if (svmType == SvmType.EPSILON_SVR
                    || svmType == SvmType.NU_SVR) {
                logger
                        .info("Prob. model for test data: target value = predicted value + z,  " +
                                "z: Laplace distribution e^(-|z|/sigma)/(2sigma),sigma="
                                + getSvmEngine().svm_get_svr_probability(getSvmModel()));
            } else {
                getSvmEngine().svm_get_labels(getSvmModel(), labels);
            }
        }
        boolean validate = false;
        for (SvmDocument svmDocument : documents) {
            validate = ((svmDocument.getClassLabelWithHighestProbability() == null) ? false : true);

            svm_node[] x = super.readProblem(svmDocument);

            double estimatedClassLabel;
            List<SvmClassLabel> classLabels = new ArrayList<>();

            if (probabilityEstimates
                    && (svmType == SvmType.C_SVC || svmType == SvmType.NU_SVC)) {
                estimatedClassLabel = getSvmEngine().svm_predict_probability(getSvmModel(), x, estimatedProbabilities);

                for (int i = 0; i < numberOfClasses; i++) {
                    SvmClassLabel scl = new SvmClassLabelImpl(labels[i]);
                    scl.setProbability(estimatedProbabilities[i]);
                    classLabels.add(scl);
                }
            } else {
                estimatedClassLabel = getSvmEngine().svm_predict(getSvmModel(), x);
                classLabels.add(new SvmClassLabelImpl(estimatedClassLabel));
            }

            //classLabel is only set, if the former classLabel was null
            if (validate) {
                double targetClassLabel = svmDocument.getClassLabelWithHighestProbability().getNumeric();
                if (estimatedClassLabel == targetClassLabel)
                    ++correctEstimated;
                // some regression correlation coefficient issue
                error += (estimatedClassLabel - targetClassLabel) * (estimatedClassLabel - targetClassLabel);
                sumOfEstimatedClassLabels += estimatedClassLabel;
                sumOfTargetClassLabels += targetClassLabel;
                sumOfEstimatedClassLabelsSquared += estimatedClassLabel * estimatedClassLabel;
                sumOfTargetClassLabelsSquared += targetClassLabel * targetClassLabel;
                sumMultipliedEstimatedTargetClassLabels += estimatedClassLabel * targetClassLabel;
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
