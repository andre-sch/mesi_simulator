package com.mesi_simulator.domain;

public enum ReserveStatus {
  AVAILABLE(0), RESERVED(1);

  private int code;

  ReserveStatus(int code) {
    this.code = code;
  }

  public int get() {
    return this.code;
  }

  public static ReserveStatus of(int code) {
    switch (code) {
      case 0: return ReserveStatus.AVAILABLE;
      case 1: return ReserveStatus.RESERVED;
      default: throw new RuntimeException("invalid status");
    }
  }

  public static ReserveStatus random() {
    int randomCode = (int) Math.floor(Math.random() * 2);
    return ReserveStatus.of(randomCode);
  }
}
