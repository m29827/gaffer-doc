/*
 * Copyright 2017 Crown Copyright
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
package uk.gov.gchq.gaffer.doc.function;

import uk.gov.gchq.koryphe.impl.function.FirstItem;

import java.util.Arrays;

public class FirstItemExample extends FunctionExample {
    public static void main(final String[] args) {
        new FirstItemExample().runAndPrint();
    }

    public FirstItemExample() {
        super(FirstItem.class, "For a given Iterable, a FirstItem will extract the first item.");
    }

    @Override
    protected void runExamples() {
        extractFirstItem();
    }

    public void extractFirstItem() {
        // ---------------------------------------------------------
        final FirstItem<Integer> function = new FirstItem<>();
        // ---------------------------------------------------------

        runExample(function,
                null,
                Arrays.asList(2, 3, 5),
                Arrays.asList(7, 11, 13),
                Arrays.asList(17, 19, 23));
    }
}
