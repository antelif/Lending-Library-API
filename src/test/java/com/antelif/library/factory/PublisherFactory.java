package com.antelif.library.factory;

import com.antelif.library.domain.dto.request.PublisherRequest;
import com.antelif.library.domain.dto.response.PublisherResponse;

public class PublisherFactory {

  public static PublisherRequest createPublisherRequest(int index) {
    PublisherRequest publisher = new PublisherRequest();
    publisher.setName("name" + index);
    return publisher;
  }

  public static PublisherResponse createPublisherResponse(int index) {
    PublisherResponse publisher = new PublisherResponse();
    publisher.setName("name" + index);
    return publisher;
  }
}
