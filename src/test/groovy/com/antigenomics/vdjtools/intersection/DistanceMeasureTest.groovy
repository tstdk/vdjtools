/*
 * Copyright 2013-2015 Mikhail Shugay (mikhail.shugay@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.antigenomics.vdjtools.intersection

import com.antigenomics.vdjtools.Software
import com.antigenomics.vdjtools.manipulation.DownSampler
import com.antigenomics.vdjtools.util.ExecUtil
import org.junit.Test

import static com.antigenomics.vdjtools.TestUtil.getResource
import static com.antigenomics.vdjtools.io.SampleStreamConnection.load

class DistanceMeasureTest {
    static void checkMetricValue(IntersectMetric intersectMetric, double val) {
        switch (intersectMetric) {
            case IntersectMetric.Correlation:
                assert val >= -1 && val <= 1
                break

            case IntersectMetric.Frequency:
            case IntersectMetric.Frequency2:
            case IntersectMetric.Diversity:
            case IntersectMetric.MorisitaHorn:
            case IntersectMetric.Jaccard:
                assert val >= 0 && val <= 1
                break

            case IntersectMetric.sJSD:
            case IntersectMetric.vJSD:
            case IntersectMetric.vj2JSD:
            case IntersectMetric.vjJSD:
                assert val >= 0
                break
        }

    }

    @Test
    public void test() {
        def software = Software.Simple
        def resStream = getResource("samples/${software.toString().toLowerCase()}.txt.gz")
        def sample = load(resStream, software)

        // downsample
        def downSampler = new DownSampler(sample)
        def nResamples = 50
        int smallCount = sample.count * 0.1, largeCount = sample.count * 0.5

        ExecUtil.quiet()

        nResamples.times {
            def s1 = downSampler.reSample(smallCount),
                s2 = downSampler.reSample(smallCount)

            def smallIntersection = new PairedIntersection(s1, s2, IntersectionType.AminoAcid)

            s1 = downSampler.reSample(largeCount)
            s2 = downSampler.reSample(largeCount)

            def largeIntersection = new PairedIntersection(s1, s2, IntersectionType.AminoAcid),
                selfIntersection = new PairedIntersection(s1, s1, IntersectionType.AminoAcid)

            IntersectMetric.values().each {
                def val1 = smallIntersection.getMetricValue(it),
                    val2 = largeIntersection.getMetricValue(it),
                    val3 = selfIntersection.getMetricValue(it)
                
                // check values
                checkMetricValue(it, val1)
                checkMetricValue(it, val2)
                checkMetricValue(it, val3)

                // Assure that distance has decreased
                // Note that due to the scaling used in diversity measure (overlap size / (sample1 size * sample2 size))
                // when two equally-well intersecting sub-sample pairs are drawn,
                // the pair with the smallest samples is considered the closest one
                // For RepSeq data, the other possible normalization (overlap size / sqrt(sample1 size * sample2 size))
                // will actually bias towards larger samples, due to higher probability of grabbing similar variants
                // It was empirically estimated that pow(sample1 size * sample2 size, 0.8) is the optimal choice
                if (it != IntersectMetric.Diversity)
                    assert it.normalization.normalize(val1) > it.normalization.normalize(val2)

                // self intersection is always the closest one
                assert it.normalization.normalize(val2) > it.normalization.normalize(val3)
            }
        }
    }
}
