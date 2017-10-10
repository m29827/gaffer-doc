/*
 * Copyright 2016 Crown Copyright
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.gov.gchq.gaffer.doc.operation.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;

import uk.gov.gchq.gaffer.doc.operation.OperationExample;
import uk.gov.gchq.gaffer.operation.Operation;
import uk.gov.gchq.gaffer.spark.SparkConstants;
import uk.gov.gchq.gaffer.spark.SparkContextUtil;
import uk.gov.gchq.gaffer.store.Context;

public abstract class SparkOperationExample extends OperationExample {
    private SparkSession sparkSession;

    public SparkOperationExample(final Class<? extends Operation> opClass, final String description) {
        super(opClass, createSparkDescription(description));
        final SparkConf sparkConf = new SparkConf()
                .setMaster("local")
                .setAppName("GetJavaRDDOfElementsExample")
                .set(SparkConstants.SERIALIZER, SparkConstants.DEFAULT_SERIALIZER)
                .set(SparkConstants.KRYO_REGISTRATOR, SparkConstants.DEFAULT_KRYO_REGISTRATOR)
                .set(SparkConstants.DRIVER_ALLOW_MULTIPLE_CONTEXTS, "true");
        sparkSession = SparkSession.builder().config(sparkConf).getOrCreate();
        sparkSession.sparkContext().setLogLevel("OFF");
    }

    public SparkOperationExample(final Class<? extends Operation> opClass) {
        this(opClass, "");
    }

    private static String createSparkDescription(final String description) {
        String sparkDescription = description;
        if (!"".equals(sparkDescription)) {
            sparkDescription += "\n\n";
        }

        return sparkDescription + "When executing a spark operation you can either let " +
                "Gaffer create a SparkSession for you or you can add it yourself " +
                "to the Context object and provide it when you execute the operation.\n" +
                "e.g:\n" +
                "```java\n" +
                "Context context = SparkContextUtil.createContext(new User(\"User01\"), sparkSession);\n" +
                "graph.execute(operation, context);\n" +
                "```\n";
    }

    @Override
    public void runExamples() {
        try {
            _runExamples();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        } finally {
            sparkSession.stop();
        }
    }

    protected abstract void _runExamples() throws Exception;

    @Override
    protected Context createContext() {
        final Context context = super.createContext();
        SparkContextUtil.addSparkSession(context, sparkSession);
        return context;
    }

    @Override
    protected String getPython(final Object object) {
        // skip
        return null;
    }
}