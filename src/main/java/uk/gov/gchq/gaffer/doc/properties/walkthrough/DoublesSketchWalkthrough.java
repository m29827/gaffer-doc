/*
 * Copyright 2017-2020 Crown Copyright
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
package uk.gov.gchq.gaffer.doc.properties.walkthrough;

import com.yahoo.sketches.quantiles.DoublesSketch;

import uk.gov.gchq.gaffer.commonutil.StreamUtil;
import uk.gov.gchq.gaffer.commonutil.iterable.CloseableIterable;
import uk.gov.gchq.gaffer.data.element.Element;
import uk.gov.gchq.gaffer.data.element.id.DirectedType;
import uk.gov.gchq.gaffer.doc.properties.generator.DoublesSketchElementGenerator;
import uk.gov.gchq.gaffer.graph.Graph;
import uk.gov.gchq.gaffer.operation.OperationChain;
import uk.gov.gchq.gaffer.operation.OperationException;
import uk.gov.gchq.gaffer.operation.data.EdgeSeed;
import uk.gov.gchq.gaffer.operation.impl.add.AddElements;
import uk.gov.gchq.gaffer.operation.impl.generate.GenerateElements;
import uk.gov.gchq.gaffer.operation.impl.get.GetAllElements;
import uk.gov.gchq.gaffer.operation.impl.get.GetElements;
import uk.gov.gchq.gaffer.user.User;

import java.util.Collections;
import java.util.Set;

public class DoublesSketchWalkthrough extends PropertiesWalkthrough {
    public DoublesSketchWalkthrough() {
        super(DoublesSketch.class, "properties/doublesSketch", DoublesSketchElementGenerator.class);
    }

    public static void main(final String[] args) throws OperationException {
        new DoublesSketchWalkthrough().run();
    }

    @Override
    public CloseableIterable<? extends Element> run() throws OperationException {
        /// [graph] create a graph using our schema and store properties
        // ---------------------------------------------------------
        final Graph graph = new Graph.Builder()
                .config(getDefaultGraphConfig())
                .addSchemas(StreamUtil.openStreams(getClass(), schemaPath))
                .storeProperties(getDefaultStoreProperties())
                .build();
        // ---------------------------------------------------------


        // [user] Create a user
        // ---------------------------------------------------------
        final User user = new User("user01");
        // ---------------------------------------------------------


        // [add] addElements - add the edges to the graph
        // ---------------------------------------------------------
        final Set<String> dummyData = Collections.singleton("");
        final OperationChain<Void> addOpChain = new OperationChain.Builder()
                .first(new GenerateElements.Builder<String>()
                        .generator(new DoublesSketchElementGenerator())
                        .input(dummyData)
                        .build())
                .then(new AddElements())
                .build();

        graph.execute(addOpChain, user);
        // ---------------------------------------------------------
        print("Added an edge A-B 1000 times, each time with a DoublesSketch containing a normally distributed"
                + " (mean 0, standard deviation 1) random double.");


        // [get] Get all edges
        // ---------------------------------------------------------
        CloseableIterable<? extends Element> allEdges = graph.execute(new GetAllElements(), user);
        // ---------------------------------------------------------
        print("\nAll edges:");
        for (final Element edge : allEdges) {
            print("GET_ALL_EDGES_RESULT", edge.toString());
        }


        // [get 0.25 0.5 0.75 percentiles for edge a b] Get the edge A-B and print an estimate of the 0.25, 0.5 and 0.75 quantiles, i.e. the 25th, 50th and 75th percentiles
        // ---------------------------------------------------------
        final GetElements query = new GetElements.Builder()
                .input(new EdgeSeed("A", "B", DirectedType.UNDIRECTED))
                .build();
        final Element edge;
        try (final CloseableIterable<? extends Element> edges = graph.execute(query, user)) {
            edge = edges.iterator().next();
        }
        final DoublesSketch doublesSketch = (DoublesSketch) edge.getProperty("doublesSketch");
        final double[] quantiles = doublesSketch.getQuantiles(new double[]{0.25D, 0.5D, 0.75D});
        final String quantilesEstimate = "Edge A-B with percentiles of double property - 25th percentile: " + quantiles[0]
                + ", 50th percentile: " + quantiles[1]
                + ", 75th percentile: " + quantiles[2];
        // ---------------------------------------------------------
        print("\nEdge A-B with an estimate of the median value");
        print("GET_0.25,0.5,0.75_PERCENTILES_FOR_EDGE_A_B", quantilesEstimate);


        // [get cdf] Get the edge A-B and print some values from the cumulative density function
        // ---------------------------------------------------------
        final GetElements query2 = new GetElements.Builder()
                .input(new EdgeSeed("A", "B", DirectedType.UNDIRECTED))
                .build();
        final Element edge2;
        try (final CloseableIterable<? extends Element> edges2 = graph.execute(query2, user)) {
            edge2 = edges2.iterator().next();
        }
        final DoublesSketch doublesSketch2 = (DoublesSketch) edge2.getProperty("doublesSketch");
        final double[] cdf = doublesSketch2.getCDF(new double[]{0.0D, 1.0D, 2.0D});
        final String cdfEstimate = "Edge A-B with CDF values at 0: " + cdf[0]
                + ", at 1: " + cdf[1]
                + ", at 2: " + cdf[2];
        // ---------------------------------------------------------
        print("\nEdge A-B with the cumulative density function values at 0, 1, 2");
        print("GET_CDF_FOR_EDGE_A_B_RESULT", cdfEstimate);
        return null;
    }
}
