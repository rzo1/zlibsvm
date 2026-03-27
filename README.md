# zlibsvm

![Build Status](https://github.com/rzo1/zlibsvm/actions/workflows/main.yml/badge.svg)  ![Maven Central](https://img.shields.io/maven-central/v/de.hs-heilbronn.mi/zlibsvm.svg?style=flat-square)

**zlibsvm** is an object-oriented Java binding for the [LIBSVM](https://github.com/cjlin1/libsvm) library.
It wraps the cross-compiled Java code behind a clean API that can be easily integrated via Apache Maven.

## Requirements

- Java 21+
- Maven 3.9.0+

## Maven Dependency

```xml
<dependency>
    <groupId>de.hs-heilbronn.mi</groupId>
    <artifactId>zlibsvm-core</artifactId>
    <version>2.1.2</version>
</dependency>
```

## Quickstart

A UI-based example project is available at [rzo1/zlibsvm-example](https://github.com/rzo1/zlibsvm-example).

### Dataset Format

zlibsvm uses the standard [LIBSVM data format](https://www.csie.ntu.edu.tw/~cjlin/libsvm/):

```
label feature_id1:feature_value1 feature_id2:feature_value2 ...
```

Each feature needs a unique integer identifier. For example, given three classes `1, 2, 3` and
features `a(id=1), b(id=2), c(id=3)`:

```
2 1:0.5325 3:0.523
3 2:0.7853 3:0.6326
1 1:0.53265 2:0.5422
```

Features not present in a data point can simply be omitted.

### Implementing SvmDocument and SvmFeature

You need to provide implementations of `SvmDocument` and `SvmFeature`:

```java
public class SvmDocumentImpl implements SvmDocument {

    private final List<SvmFeature> features;
    private final List<SvmClassLabel> classLabels = new ArrayList<>();

    public SvmDocumentImpl(List<SvmFeature> features) {
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
```

```java
public record SvmFeatureImpl(int index, double value) implements SvmFeature {

    public int getIndex() {
        return index;
    }

    public double getValue() {
        return value;
    }

    @Override
    public int compareTo(SvmFeature o) {
        return Integer.compare(getIndex(), o.getIndex());
    }
}
```

### Training

Create a trainer with an `SvmConfigurationImpl.Builder` and train a model.
The default configuration matches LIBSVM defaults: C-SVC with RBF kernel, gamma = 0, cost = 1.

```java
SvmTrainer trainer = new SvmTrainerImpl(
        new SvmConfigurationImpl.Builder().build(), "my-model");

SvmModel model = trainer.train(documentsForTraining);
```

### Prediction

Use the trained model to classify new documents:

```java
SvmClassifier classifier = new SvmClassifierImpl(model);

List<SvmDocument> classified = classifier.classify(documentsForPrediction, true);

for (SvmDocument d : classified) {
    System.out.println(d + " was classified as category: "
            + d.getClassLabelWithHighestProbability().getNumeric());
}
```

## License

Published under [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0)
