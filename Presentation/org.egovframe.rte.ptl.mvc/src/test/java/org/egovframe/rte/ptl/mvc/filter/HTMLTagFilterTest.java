package org.egovframe.rte.ptl.mvc.filter;

import org.junit.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class HTMLTagFilterTest {

	@Test
	public void HTMLTagFilterDoFilterTest() throws IOException, ServletException {
		HTMLTagFilter tagFilter = new HTMLTagFilter();
		tagFilter.doFilter(new MockHttpServletRequest(), new MockHttpServletResponse(), new MockFilterChain());

	}

	@Test
	public void HTMLTagFilterRequestWrapperTest() {
		HttpServletRequest request = new MockHttpServletRequest();
		request.setAttribute("param01", "param01");
		request.setAttribute("param02", "param02");
		HTMLTagFilterRequestWrapper requestWrapper = new HTMLTagFilterRequestWrapper(request);
		String param = (String) requestWrapper.getAttribute("param01");

		assertEquals("param01", param);

	}

}
