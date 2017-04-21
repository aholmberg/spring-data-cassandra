/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.cassandra.convert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.cassandra.mapping.CassandraSimpleTypeHolder;
import org.springframework.data.mapping.model.SimpleTypeHolder;

/**
 * Value object to capture custom conversion. {@link CassandraCustomConversions} also act as factory for
 * {@link SimpleTypeHolder}
 *
 * @author Mark Paluch
 * @since 2.0
 */
public class CassandraCustomConversions extends org.springframework.data.convert.CustomConversions {

	private static final StoreConversions STORE_CONVERSIONS;
	private static final List<Object> STORE_CONVERTERS;

	static {

		List<Object> converters = new ArrayList<>();

		converters.addAll(CassandraConverters.getConvertersToRegister());
		converters.addAll(CassandraJodaTimeConverters.getConvertersToRegister());
		converters.addAll(CassandraJsr310Converters.getConvertersToRegister());
		converters.addAll(CassandraThreeTenBackPortConverters.getConvertersToRegister());

		STORE_CONVERTERS = Collections.unmodifiableList(converters);
		STORE_CONVERSIONS = StoreConversions.of(new CassandraSimpleTypeHolder(), STORE_CONVERTERS);
	}

	/**
	 * Create a new {@link CassandraCustomConversions} instance registering the given converters.
	 *
	 * @param converters
	 */
	public CassandraCustomConversions(List<?> converters) {
		super(STORE_CONVERSIONS,
				Stream.concat(converters.stream(), STORE_CONVERTERS.stream()).collect(Collectors.toList()));
	}
}
