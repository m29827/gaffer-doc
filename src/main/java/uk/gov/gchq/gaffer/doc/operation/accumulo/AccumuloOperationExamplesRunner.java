/*
 * Copyright 2016-2020 Crown Copyright
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
package uk.gov.gchq.gaffer.doc.operation.accumulo;

import uk.gov.gchq.gaffer.doc.operation.OperationExample;
import uk.gov.gchq.gaffer.doc.util.ExampleDocRunner;

/**
 * This runner will run all accumulo operation examples.
 */
public class AccumuloOperationExamplesRunner extends ExampleDocRunner {

    public AccumuloOperationExamplesRunner() {
        super("Accumulo Operations", OperationExample.class);
    }

    public static void main(final String[] args) throws Exception {
        new AccumuloOperationExamplesRunner().generate();
    }
}
