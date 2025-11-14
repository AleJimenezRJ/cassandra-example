package cassandra.entity;

import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Entity representing a sensor metric in Cassandra.
 * This table demonstrates wide column design where a single sensor partition
 * can contain MILLIONS of time-series metrics.
 * 
 * Wide Column Design Explanation:
 * - Partition Key: (sensor_id, year, month) - Groups related data together
 * - Clustering Columns: (day, hour, recorded_at) - Sorts data within partition
 * - Each sensor+month can have unlimited metrics (wide rows)
 * - In SQL, this would require multiple tables or billions of rows
 * - In Cassandra, this is ONE partition with efficient time-series access
 */
@Table("sensor_metrics")
public class SensorMetric implements Serializable {

    @PrimaryKeyColumn(name = "sensor_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String sensorId;

    @PrimaryKeyColumn(name = "year", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
    private Integer year;

    @PrimaryKeyColumn(name = "month", ordinal = 2, type = PrimaryKeyType.PARTITIONED)
    private Integer month;

    @PrimaryKeyColumn(name = "day", ordinal = 3, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private Integer day;

    @PrimaryKeyColumn(name = "hour", ordinal = 4, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private Integer hour;

    @PrimaryKeyColumn(name = "recorded_at", ordinal = 5, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private Date recordedAt;

    @Column("metric_type")
    private String metricType;

    @Column("metric_value")
    private Double metricValue;

    @Column("unit")
    private String unit;

    @Column("location")
    private String location;

    @Column("status")
    private String status;

    @Column("metadata")
    private Map<String, String> metadata;

    public SensorMetric() {
    }

    public SensorMetric(String sensorId, Integer year, Integer month, Integer day, Integer hour,
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
