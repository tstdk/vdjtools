/**
 Copyright 2014 Mikhail Shugay (mikhail.shugay@gmail.com)

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package com.antigenomics.vdjtools.clustering

import com.antigenomics.vdjtools.sample.Sample

class MultiSampleClusterization {
    private final Map<String, ClonotypeCluster[]> groupedClusters = new HashMap<String, ClonotypeCluster[]>()
    private final Sample[] samples

    //int getNumberOfClusters()

    //MultiSampleCluster getAt(int clusterIndex)

    MultiSampleClusterization(SingleSampleClustering... clusterizations) {
        this.samples = clusterizations*.parentSample as Sample[]

        int nSamples = clusterizations.size()
        for (int i = 0; i < nSamples; i++) {
            def clusterization = clusterizations[i]
            clusterization.clusters.each { cluster ->
                def clusterArr = groupedClusters[cluster.signature]
                if (!clusterArr)
                    groupedClusters.put(cluster.signature, new ClonotypeCluster[nSamples])
                clusterArr[i] = cluster
            }
        }

        groupedClusters.sort { it.key }
    }
}
