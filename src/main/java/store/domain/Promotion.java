package store.domain;

import java.util.Date;

public class Promotion {

  private final String name;
  private final int buy;
  private final int get;
  private final Date startDate;
  private final Date endDate;

  public Promotion(String name, int buy, int get, Date startDate, Date endDate) {
    this.name = name;
    this.buy = buy;
    this.get = get;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public String getName() {
    return name;
  }

  public int getBuy() {
    return buy;
  }

  public int getGet() {
    return get;
  }
}
