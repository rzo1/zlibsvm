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
package de.hsheilbronn.mi.configuration;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @author rz
 */
public class SvmConfigurationImpl implements SvmConfiguration {

    private final SvmType svmType;
    private final KernelType kernelType;
    private final int degree;
    private final double gamma;
    private final double coef0;
    private final double nu;
    private final double cacheSize;
    private final double cost;
    private final double eps;
    private final double p;
    private final int shrinking;
    private final int probability;
    private final int nrWeight;
    private final int crossValidation;
    private final int[] weightLabel;
    private final double[] weight;
    private final int nFold;
    private boolean quietMode;

    private SvmConfigurationImpl(Builder builder) {
        this.svmType = builder.svmType;
        this.kernelType = builder.kernelType;
        this.degree = builder.degree;
        this.gamma = builder.gamma;
        this.coef0 = builder.coef0;
        this.nu = builder.nu;
        this.cacheSize = builder.cacheSize;
        this.cost = builder.cost;
        this.eps = builder.eps;
        this.p = builder.p;
        this.shrinking = builder.shrinking;
        this.probability = builder.probability;
        this.nrWeight = builder.nrWeight;
        this.crossValidation = builder.crossValidation;
        this.weightLabel = builder.weightLabel;
        this.weight = builder.weight;

        this.nFold = builder.nFold;
        this.quietMode = builder.quietMode;
    }

    @Override
    public SvmType getSvmType() {
        return this.svmType;
    }

    @Override
    public int getNFold() {
        return nFold;
    }

    @Override
    public boolean isQuietMode() {
        return quietMode;
    }

    @Override
    public List<Double> getWeight() {
        return Arrays.asList(ArrayUtils.toObject(weight));
    }

    @Override
    public List<Integer> getWeightLabel() {
        return Arrays.asList(ArrayUtils.toObject(weightLabel));
    }

    @Override
    public int getCrossValidation() {
        return crossValidation;
    }

    @Override
    public int getNrWeight() {
        return nrWeight;
    }

    @Override
    public int getProbability() {
        return probability;
    }

    @Override
    public int getShrinking() {
        return shrinking;
    }

    @Override
    public double getP() {
        return p;
    }

    @Override
    public double getCost() {
        return cost;
    }

    @Override
    public double getEps() {
        return eps;
    }

    @Override
    public double getCacheSize() {
        return cacheSize;
    }

    @Override
    public double getNu() {
        return nu;
    }

    @Override
    public double getGamma() {
        return gamma;
    }

    @Override
    public double getCoef0() {
        return coef0;
    }

    @Override
    public int getDegree() {
        return degree;
    }

    @Override
    public KernelType getKernelType() {
        return this.kernelType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SvmConfigurationImpl that = (SvmConfigurationImpl) o;

        if (Double.compare(that.cacheSize, cacheSize) != 0) return false;
        if (Double.compare(that.coef0, coef0) != 0) return false;
        if (Double.compare(that.cost, cost) != 0) return false;
        if (crossValidation != that.crossValidation) return false;
        if (degree != that.degree) return false;
        if (Double.compare(that.eps, eps) != 0) return false;
        if (Double.compare(that.gamma, gamma) != 0) return false;
        if (nFold != that.nFold) return false;
        if (nrWeight != that.nrWeight) return false;
        if (Double.compare(that.nu, nu) != 0) return false;
        if (Double.compare(that.p, p) != 0) return false;
        if (probability != that.probability) return false;
        if (shrinking != that.shrinking) return false;
        if (kernelType != that.kernelType) return false;
        if (svmType != that.svmType) return false;
        if (!Arrays.equals(weight, that.weight)) return false;
        return Arrays.equals(weightLabel, that.weightLabel);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = svmType != null ? svmType.hashCode() : 0;
        result = 31 * result + (kernelType != null ? kernelType.hashCode() : 0);
        result = 31 * result + degree;
        temp = Double.doubleToLongBits(gamma);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(coef0);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(nu);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(cacheSize);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(cost);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(eps);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(p);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + shrinking;
        result = 31 * result + probability;
        result = 31 * result + nrWeight;
        result = 31 * result + crossValidation;
        result = 31 * result + (weightLabel != null ? Arrays.hashCode(weightLabel) : 0);
        result = 31 * result + (weight != null ? Arrays.hashCode(weight) : 0);
        result = 31 * result + nFold;
        return result;
    }

    public static class Builder implements SvmConfigurationBuilder {
        private SvmType svmType = SvmType.C_SVC;
        private KernelType kernelType = KernelType.RBF;
        private int degree = 3;
        private double gamma = 0;
        private double coef0 = 0;
        private double nu = 0.5;
        private double cacheSize = 100;
        private double cost = 1;
        private double eps = 1e-3;
        private double p = 0.1;
        private int shrinking = 1;
        private int probability = 0;
        private int nrWeight = 0;
        private int crossValidation = 0;
        private int[] weightLabel = new int[0];
        private double[] weight = new double[0];
        private int nFold = 0;
        private boolean quietMode = true;

        /**
         * {@inheritDoc}
         */
        @Override
        public SvmConfigurationBuilder setSvmType(SvmType svmType) {
            this.svmType = svmType;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public SvmConfigurationBuilder setKernelType(KernelType
                                                             kernelType) {
            this.kernelType = kernelType;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public SvmConfigurationBuilder setDegree(int degree) {
            assert (degree >= 0);
            this.degree = degree;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public SvmConfigurationBuilder setGamma(double gamma) {
            assert (gamma >= 0);
            this.gamma = gamma;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public SvmConfigurationBuilder setCoef0(double coef0) {
            this.coef0 = coef0;
            return this;
        }


        /**
         * {@inheritDoc}
         */
        @Override
        public SvmConfigurationBuilder setNu(double nu) {
            assert ((nu > 0) && (nu <= 1));
            this.nu = nu;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public SvmConfigurationBuilder setCacheSize(double cacheSize) {
            assert (cacheSize > 0);
            this.cacheSize = cacheSize;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public SvmConfigurationBuilder setCost(double cost) {
            assert (cost > 0);
            this.cost = cost;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public SvmConfigurationBuilder setEpsilon(double epsilon) {
            assert (epsilon > 0);
            this.eps = epsilon;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public SvmConfigurationBuilder setP(double p) {
            assert (p >= 0);
            this.p = p;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public SvmConfigurationBuilder setShrinking(boolean shrinking) {
            this.shrinking = shrinking ? 1 : 0;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public SvmConfigurationBuilder setProbability(boolean probability) {
            this.probability = probability ? 1 : 0;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public SvmConfigurationBuilder setNrWeight(int classLabel, double weight) {
            this.nrWeight++;
            {
                int[] old = this.weightLabel;
                this.weightLabel = new int[this.nrWeight];

                System.arraycopy(old, 0, this.weightLabel, 0,
                        this.nrWeight - 1);
            }
            {
                double[] old = this.weight;
                this.weight = new double[this.nrWeight];
                System.arraycopy(old, 0, this.weight, 0,
                        this.nrWeight - 1);
            }
            this.weightLabel[this.nrWeight - 1] = classLabel;
            this.weight[this.nrWeight - 1] = weight;

            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public SvmConfigurationBuilder setCrossValidation(boolean crossValidation, int nFold) {
            assert (!crossValidation || nFold >= 2);
            this.crossValidation = crossValidation ? 1 : 0;
            this.nFold = nFold;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public SvmConfigurationBuilder setQuietMode(boolean quietMode) {
            this.quietMode = quietMode;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public SvmConfiguration build() {
            return new SvmConfigurationImpl(this);
        }

    }


}
