package com.antelif.library.factory;

import com.antelif.library.domain.dto.request.PublisherRequest;

public class PublisherFactory {

  public static PublisherRequest createPublisherRequest(int index) {
    var publisher = new PublisherRequest();
    publisher.setName("name" + index);
    return publisher;
  }
}
