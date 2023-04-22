package com.driver.model;

import javax.persistence.*;

@Entity
@Table(name = "cab")
public class Cab {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int cabId;
    private int perKmRate;
    private boolean available;

    @OneToOne
    @JoinColumn
    private Driver driver;
    public Cab() {
    }

    public Cab(int cabId, int perKmRate, boolean available, Driver driver) {
        this.cabId = cabId;
        this.perKmRate = perKmRate;
        this.available = available;
        this.driver = driver;
    }

    public int getCabId() {
        return cabId;
    }

    public void setCabId(int cabId) {
        this.cabId = cabId;
    }

    public int getPerKmRate() {
        return perKmRate;
    }

    public void setPerKmRate(int perKmRate) {
        this.perKmRate = perKmRate;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }
}
