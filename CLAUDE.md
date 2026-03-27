# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

zlibsvm is an object-oriented Java wrapper for the LIBSVM machine learning library. It provides a clean
API/implementation separation across two Maven modules:

- **zlibsvm-api** — Interfaces and domain model (no external dependencies)
- **zlibsvm-core** — Implementation backed by native LIBSVM Java bindings

## Build Commands

```bash
# Build and test
mvn clean package

# Run all tests
mvn test

# Run a single test class
mvn -pl zlibsvm-core -Dtest=SvmTrainingTestCase test

# Release build (includes source, javadoc, GPG signing)
mvn clean package -Prelease

# Check for dependency updates
mvn clean compile -Pversion-check
```

**Requirements:** Maven 3.8.0+, Java 21

## Architecture

The project follows a strict API/implementation separation:

**API layer** (`de.hhn.mi.*` in zlibsvm-api):

- `domain` — `SvmDocument`, `SvmFeature`, `SvmClassLabel`, `SvmModel`, `SvmMetaInformation`
- `configuration` — `SvmConfiguration`, `SvmType`, `KernelType`, `SvmConfigurationBuilder`
- `process` — `SvmTrainer`, `SvmClassifier`
- `exception` — `ClassificationCoreException`

**Implementation layer** (`de.hhn.mi.*` in zlibsvm-core):

- `SvmConfigurationImpl` uses Builder pattern for constructing LIBSVM parameter sets
- `SvmTrainerImpl` / `SvmClassifierImpl` extend abstract base classes that handle conversion between domain objects and
  native LIBSVM structures (`svm_problem`, `svm_model`)
- `NativeSvmModelWrapper` bridges the domain model to LIBSVM's native model representation

**Key dependencies:** libsvm 3.35, slf4j-api, JUnit 5 (Jupiter)

## Testing

Tests are in `zlibsvm-core` and use the **mushroom** dataset in `src/test/resources/`. Four test suites:
`SvmTrainingTestCase`, `SvmClassifierTestCase`, `SvmConfigurationTestCase`, `SvmPerformanceTestCase`. Tests run with max
heap 512MB.

## CI

GitHub Actions runs `mvn clean package` on JDK 21 (Temurin) for pushes to master and PRs.
