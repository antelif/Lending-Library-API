package com.antelif.library.domain.common;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Contains methods that create and truncate instants.
 */
public final class InstantConversions {

  private InstantConversions() {
  }

  /**
   * Creates an instant and truncates it to days.
   */
  public static Instant nowInstantToDays() {
    return Instant.now().truncatedTo(ChronoUnit.DAYS);
  }
}
