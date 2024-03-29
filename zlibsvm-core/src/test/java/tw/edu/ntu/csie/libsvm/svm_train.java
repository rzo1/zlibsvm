/*-
 * ========================LICENSE_START=================================
 * zlibsvm-core
 * %%
 * Copyright (C) 2014 - 2022 Heilbronn University - Medical Informatics
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
/*
Copyright (c) 2000-2017 Chih-Chung Chang and Chih-Jen Lin
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions
are met:

1. Redistributions of source code must retain the above copyright
notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright
notice, this list of conditions and the following disclaimer in the
documentation and/or other materials provided with the distribution.

3. Neither name of copyright holders nor the names of its contributors
may be used to endorse or promote products derived from this software
without specific prior written permission.


THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package tw.edu.ntu.csie.libsvm;

import libsvm.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.StringTokenizer;
import java.util.Vector;

public class svm_train {
    private svm_parameter param;        // set by parse_command_line
    private svm_problem prob;        // set by read_problem
    private String input_file_name;        // set by parse_command_line
    private String model_file_name;        // set by parse_command_line
    private int cross_validation;
    private int nr_fold;

    private static final svm_print_interface svm_print_null = s -> {
    };

    private static void exit_with_help() {
        System.out.print(
                """
                        Usage: svm_train [options] training_set_file [model_file]
                        options:
                        -s svm_type : set type of SVM (default 0)
                        	0 -- C-SVC		(multi-class classification)
                        	1 -- nu-SVC		(multi-class classification)
                        	2 -- one-class SVM
                        	3 -- epsilon-SVR	(regression)
                        	4 -- nu-SVR		(regression)
                        -t kernel_type : set type of kernel function (default 2)
                        	0 -- linear: u'*v
                        	1 -- polynomial: (gamma*u'*v + coef0)^degree
                        	2 -- radial basis function: exp(-gamma*|u-v|^2)
                        	3 -- sigmoid: tanh(gamma*u'*v + coef0)
                        	4 -- precomputed kernel (kernel values in training_set_file)
                        -d degree : set degree in kernel function (default 3)
                        -g gamma : set gamma in kernel function (default 1/num_features)
                        -r coef0 : set coef0 in kernel function (default 0)
                        -c cost : set the parameter C of C-SVC, epsilon-SVR, and nu-SVR (default 1)
                        -n nu : set the parameter nu of nu-SVC, one-class SVM, and nu-SVR (default 0.5)
                        -p epsilon : set the epsilon in loss function of epsilon-SVR (default 0.1)
                        -m cachesize : set cache memory size in MB (default 100)
                        -e epsilon : set tolerance of termination criterion (default 0.001)
                        -h shrinking : whether to use the shrinking heuristics, 0 or 1 (default 1)
                        -b probability_estimates : whether to train a SVC or SVR model for probability estimates, 0 or 1 (default 0)
                        -wi weight : set the parameter C of class i to weight*C, for C-SVC (default 1)
                        -v n : n-fold cross validation mode
                        -q : quiet mode (no outputs)
                        """
        );
        System.exit(1);
    }

    private void do_cross_validation() {
        int i;
        int total_correct = 0;
        double total_error = 0;
        double sumv = 0, sumy = 0, sumvv = 0, sumyy = 0, sumvy = 0;
        double[] target = new double[prob.l];

        svm.svm_cross_validation(prob, param, nr_fold, target);
        if (param.svm_type == svm_parameter.EPSILON_SVR ||
                param.svm_type == svm_parameter.NU_SVR) {
            for (i = 0; i < prob.l; i++) {
                double y = prob.y[i];
                double v = target[i];
                total_error += (v - y) * (v - y);
                sumv += v;
                sumy += y;
                sumvv += v * v;
                sumyy += y * y;
                sumvy += v * y;
            }
            System.out.print("Cross Validation Mean squared error = " + total_error / prob.l + "\n");
            System.out.print("Cross Validation Squared correlation coefficient = " +
                    ((prob.l * sumvy - sumv * sumy) * (prob.l * sumvy - sumv * sumy)) /
                            ((prob.l * sumvv - sumv * sumv) * (prob.l * sumyy - sumy * sumy)) + "\n"
            );
        } else {
            for (i = 0; i < prob.l; i++)
                if (target[i] == prob.y[i])
                    ++total_correct;
            System.out.print("Cross Validation Accuracy = " + 100.0 * total_correct / prob.l + "%\n");
        }
    }

    private void run(String[] argv) throws IOException {
        parse_command_line(argv);
        read_problem();
        String error_msg = svm.svm_check_parameter(prob, param);

        if (error_msg != null) {
            System.err.print("ERROR: " + error_msg + "\n");
            System.exit(1);
        }

        if (cross_validation != 0) {
            do_cross_validation();
        } else {
            svm_model model = svm.svm_train(prob, param);
            svm.svm_save_model(model_file_name, model);
        }
    }

    public static void main(String[] argv) throws IOException {
        svm_train t = new svm_train();
        t.run(argv);
    }

    private static double atof(String s) {
        double d = Double.parseDouble(s);
        if (Double.isNaN(d) || Double.isInfinite(d)) {
            System.err.print("NaN or Infinity in input\n");
            System.exit(1);
        }
        return (d);
    }

    private static int atoi(String s) {
        return Integer.parseInt(s);
    }

    private void parse_command_line(String[] argv) {
        int i;
        svm_print_interface print_func = null;    // default printing to stdout

        param = new svm_parameter();
        // default values
        param.svm_type = svm_parameter.C_SVC;
        param.kernel_type = svm_parameter.RBF;
        param.degree = 3;
        param.gamma = 0;    // 1/num_features
        param.coef0 = 0;
        param.nu = 0.5;
        param.cache_size = 100;
        param.C = 1;
        param.eps = 1e-3;
        param.p = 0.1;
        param.shrinking = 1;
        param.probability = 0;
        param.nr_weight = 0;
        param.weight_label = new int[0];
        param.weight = new double[0];
        cross_validation = 0;

        // parse options
        for (i = 0; i < argv.length; i++) {
            if (argv[i].charAt(0) != '-') break;
            if (++i >= argv.length)
                exit_with_help();
            switch (argv[i - 1].charAt(1)) {
                case 's':
                    param.svm_type = atoi(argv[i]);
                    break;
                case 't':
                    param.kernel_type = atoi(argv[i]);
                    break;
                case 'd':
                    param.degree = atoi(argv[i]);
                    break;
                case 'g':
                    param.gamma = atof(argv[i]);
                    break;
                case 'r':
                    param.coef0 = atof(argv[i]);
                    break;
                case 'n':
                    param.nu = atof(argv[i]);
                    break;
                case 'm':
                    param.cache_size = atof(argv[i]);
                    break;
                case 'c':
                    param.C = atof(argv[i]);
                    break;
                case 'e':
                    param.eps = atof(argv[i]);
                    break;
                case 'p':
                    param.p = atof(argv[i]);
                    break;
                case 'h':
                    param.shrinking = atoi(argv[i]);
                    break;
                case 'b':
                    param.probability = atoi(argv[i]);
                    break;
                case 'q':
                    print_func = svm_print_null;
                    i--;
                    break;
                case 'v':
                    cross_validation = 1;
                    nr_fold = atoi(argv[i]);
                    if (nr_fold < 2) {
                        System.err.print("n-fold cross validation: n must >= 2\n");
                        exit_with_help();
                    }
                    break;
                case 'w':
                    ++param.nr_weight;
                {
                    int[] old = param.weight_label;
                    param.weight_label = new int[param.nr_weight];
                    System.arraycopy(old, 0, param.weight_label, 0, param.nr_weight - 1);
                }

                {
                    double[] old = param.weight;
                    param.weight = new double[param.nr_weight];
                    System.arraycopy(old, 0, param.weight, 0, param.nr_weight - 1);
                }

                param.weight_label[param.nr_weight - 1] = atoi(argv[i - 1].substring(2));
                param.weight[param.nr_weight - 1] = atof(argv[i]);
                break;
                default:
                    System.err.print("Unknown option: " + argv[i - 1] + "\n");
                    exit_with_help();
            }
        }

        svm.svm_set_print_string_function(print_func);

        // determine filenames

        if (i >= argv.length)
            exit_with_help();

        input_file_name = argv[i];

        if (i < argv.length - 1)
            model_file_name = argv[i + 1];
        else {
            int p = argv[i].lastIndexOf('/');
            ++p;    // whew...
            model_file_name = argv[i].substring(p) + ".model";
        }
    }

    // read in a problem (in svmlight format)

    private void read_problem() throws IOException {
        BufferedReader fp = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream
                (input_file_name), StandardCharsets.UTF_8));
        Vector<Double> vy = new Vector<>();
        Vector<svm_node[]> vx = new Vector<>();
        int max_index = 0;
        //skip first line because of meta information
        fp.readLine();
        while (true) {
            String line = fp.readLine();
            if (line == null) break;

            StringTokenizer st = new StringTokenizer(line, " \t\n\r\f:");

            vy.addElement(atof(st.nextToken()));
            int m = st.countTokens() / 2;
            svm_node[] x = new svm_node[m];
            for (int j = 0; j < m; j++) {
                x[j] = new svm_node();
                x[j].index = atoi(st.nextToken());
                x[j].value = atof(st.nextToken());
            }
            if (m > 0) max_index = Math.max(max_index, x[m - 1].index);
            vx.addElement(x);
        }

        prob = new svm_problem();
        prob.l = vy.size();
        prob.x = new svm_node[prob.l][];
        for (int i = 0; i < prob.l; i++)
            prob.x[i] = vx.elementAt(i);
        prob.y = new double[prob.l];
        for (int i = 0; i < prob.l; i++)
            prob.y[i] = vy.elementAt(i);

        if (param.gamma == 0 && max_index > 0)
            param.gamma = 1.0 / max_index;

        if (param.kernel_type == svm_parameter.PRECOMPUTED)
            for (int i = 0; i < prob.l; i++) {
                if (prob.x[i][0].index != 0) {
                    System.err.print("Wrong kernel matrix: first column must be 0:sample_serial_number\n");
                    System.exit(1);
                }
                if ((int) prob.x[i][0].value <= 0 || (int) prob.x[i][0].value > max_index) {
                    System.err.print("Wrong input format: sample_serial_number out of range\n");
                    System.exit(1);
                }
            }

        fp.close();
    }
}
