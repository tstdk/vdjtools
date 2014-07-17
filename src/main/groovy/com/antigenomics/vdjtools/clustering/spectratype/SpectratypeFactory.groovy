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


package com.antigenomics.vdjtools.clustering.spectratype

import com.antigenomics.vdjtools.clustering.ClusteringFactory
import com.antigenomics.vdjtools.clustering.SingleSampleClustering
import com.antigenomics.vdjtools.sample.Sample

class SpectratypeFactory implements ClusteringFactory {

    @Override
    SingleSampleClustering create(Sample sample) {
        new Spectratype(sample)
    }
}
