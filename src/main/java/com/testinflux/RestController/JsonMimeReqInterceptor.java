package com.testinflux.RestController;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class JsonMimeReqInterceptor implements ClientHttpRequestInterceptor {
  @NotNull
  @Override
  public ClientHttpResponse intercept(
      HttpRequest request, @NotNull byte[] body, ClientHttpRequestExecution execution)
      throws IOException {
    HttpHeaders headers = request.getHeaders();
    headers.add("Referer","https://iboard.ssi.com.vn");
    return execution.execute(request, body);
  }
}
