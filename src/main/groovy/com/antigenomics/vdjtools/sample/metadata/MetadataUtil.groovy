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

package com.antigenomics.vdjtools.sample.metadata

import org.apache.commons.io.FilenameUtils

/**
 * Some useful utils for metadata manipulation 
 */
public class MetadataUtil {
    private static final Map<String, Integer> sampleHash = new HashMap<>()

    /**
     * Converts a file name to sample id 
     * @param fileName file name to convert
     * @return sample id, a shortcut for file name without any path and extension
     */
    public static String fileName2id(String fileName) {
        FilenameUtils.getBaseName(
                fileName.endsWith(".gz") ?
                        FilenameUtils.getBaseName(fileName) :
                        fileName)
    }

    /**
     * Creates sample metadata object and assigns it to a generic metadata table 
     * @param sampleId short unique identifier of a sample
     * @return sample metadata object assigned to a generic metadata table
     */
    public static SampleMetadata createSampleMetadata(String sampleId) {
        def idCount = (sampleHash[sampleId] ?: 0) + 1
        sampleHash.put(sampleId, idCount)
        defaultMetadataTable.createRow((idCount > 0 ? "$idCount." : "") + sampleId, new ArrayList<String>())
    }

    /**
     * Gets a generic metadata table
     * @return metadata table which contains all statically-created metadata entries
     */
    public static MetadataTable getDefaultMetadataTable() {
        MetadataTable.GENERIC_METADATA_TABLE
    }
}
