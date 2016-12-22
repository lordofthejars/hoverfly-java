/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2016-2016 SpectoLabs Ltd.
 */
package io.specto.hoverfly.junit.core.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Simulation {
    @JsonProperty("data")
    private final HoverflyData hoverflyData;
    @JsonProperty("meta")
    private final HoverflyMetaData hoverflyMetaData;

    @JsonCreator
    public Simulation(@JsonProperty("data") HoverflyData hoverflyData,
                      @JsonProperty("meta") HoverflyMetaData hoverflyMetaData) {
        this.hoverflyData = hoverflyData;
        this.hoverflyMetaData = hoverflyMetaData;
    }

    public HoverflyData getHoverflyData() {
        return hoverflyData;
    }


    public HoverflyMetaData getHoverflyMetaData() {
        return hoverflyMetaData;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert object to json string: ", e);
        }
    }


}