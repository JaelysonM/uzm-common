package com.uzm.common.spigot.entities.beam.protocol;

class EIDGen {
  private static int lastIssuedEID = 2000000000;
  
  static int generateEID() {
    return lastIssuedEID++;
  }
}
