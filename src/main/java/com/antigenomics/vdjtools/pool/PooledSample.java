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

package com.antigenomics.vdjtools.pool;

import com.antigenomics.vdjtools.sample.Clonotype;
import com.antigenomics.vdjtools.ClonotypeContainer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class PooledSample implements ClonotypeContainer {
    private final List<Clonotype> clonotypes;
    private final long count;

    public PooledSample(SampleAggregator<StoringClonotypeAggregator> sampleAggregator) {
        this.clonotypes = new ArrayList<>(sampleAggregator.getDiversity());

        long count = 0;

        for (StoringClonotypeAggregator clonotypeAggregator : sampleAggregator) {
            int x = clonotypeAggregator.getCount();
            count += x;
            clonotypes.add(new Clonotype(clonotypeAggregator.getClonotype(),
                    this,
                    x));
        }

        this.count = count;

        Collections.sort(clonotypes);
    }


    @Override
    public double getFreq() {
        return 1.0;
    }

    @Override
    public long getCount() {
        return count;
    }

    @Override
    public int getDiversity() {
        return clonotypes.size();
    }

    @Override
    public Clonotype getAt(int index) {
        return clonotypes.get(index);
    }

    @Override
    public boolean isSorted() {
        return true;
    }

    @Override
    public Iterator<Clonotype> iterator() {
        return clonotypes.iterator();
    }
}
