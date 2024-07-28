package com.cinema_reservations.domain;

public enum ReserveStatus {
  AVAILABLE(0), SELECTED(1), RESERVED(2);

  private int status;

  ReserveStatus(int status) {
    this.status = status;
  }

  public int get() {
    return this.status;
  }
}
