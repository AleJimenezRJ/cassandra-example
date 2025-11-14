package cassandra.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.Map;

public class SensorMetricDTO {

    private String sensorId;
    private Integer year;
    private Integer month;
    private Integer day;
    private Integer hour;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date recordedAt;
    
    private String metricType;
    private Double metricValue;
    private String unit;
    private String location;
    private String status;
    private Map<String, String> metadata;

    public SensorMetricDTO() {
    }

    public SensorMetricDTO(String sensorId, Integer year, Integer month, Integer day, Integer hour,
                          Date recordedAt, String metricType, Double metricValue, String unit,
                          String location, String status, Map<String, String> metadata) {
        this.sensorId = sensorId;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.recordedAt = recordedAt;
        this.metricType = metricType;
        this.metricValue = metricValue;
        this.unit = unit;
        this.location = location;
        this.status = status;
        this.metadata = metadata;
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Date getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(Date recordedAt) {
        this.recordedAt = recordedAt;
    }

    public String getMetricType() {
        return metricType;
    }

    public void setMetricType(String metricType) {
        this.metricType = metricType;
    }

    public Double getMetricValue() {
        return metricValue;
    }

    public void setMetricValue(Double metricValue) {
        this.metricValue = metricValue;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }
}
