/*
 * Copyright 2008-2024 MOIS(Ministry of the Interior and Safety).
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
package org.egovframe.rte.bat.core.item.file.mapping;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 상속된 JavaBean property 매핑 회귀 테스트
 *
 * <pre>
 * 개정이력(Modification Information)
 *
 * 수정일		수정자				수정내용
 * ----------------------------------------------
 * 2026.09.17	z3rotig4r			최초 생성
 * </pre>
 */
class EgovObjectMapperInheritedPropertyTest {

	/**
	 * setter가 superclass의 public method로 상속되는 property는
	 * backing field가 subclass에 없어도 정상적으로 매핑되어야 한다.
	 */
	@Test
	void mapObject_inheritedProperty() {
		EgovObjectMapper<ChildVO> mapper = new EgovObjectMapper<>();
		mapper.setType(ChildVO.class);
		mapper.setNames(new String[]{"inheritedValue", "localValue"});
		mapper.afterPropertiesSet();

		ChildVO vo = mapper.mapObject(Arrays.asList("base", "42"));

		assertEquals("base", vo.getInheritedValue());
		assertEquals(Integer.valueOf(42), vo.getLocalValue());
	}

	/**
	 * backing field가 없고 setter/getter만 존재하는 JavaBean property도 매핑되어야 한다.
	 */
	@Test
	void mapObject_propertyWithoutBackingField() {
		EgovObjectMapper<ComputedVO> mapper = new EgovObjectMapper<>();
		mapper.setType(ComputedVO.class);
		mapper.setNames(new String[]{"displayName"});
		mapper.afterPropertiesSet();

		ComputedVO vo = mapper.mapObject(Arrays.asList("shown"));

		assertEquals("shown", vo.getDisplayName());
	}

	public static class BaseVO {
		private String inheritedValue;

		public String getInheritedValue() {
			return inheritedValue;
		}

		public void setInheritedValue(String inheritedValue) {
			this.inheritedValue = inheritedValue;
		}
	}

	public static class ChildVO extends BaseVO {
		private Integer localValue;

		public Integer getLocalValue() {
			return localValue;
		}

		public void setLocalValue(Integer localValue) {
			this.localValue = localValue;
		}
	}

	public static class ComputedVO {
		private String internal;

		public String getDisplayName() {
			return internal;
		}

		public void setDisplayName(String value) {
			this.internal = value;
		}
	}
}
