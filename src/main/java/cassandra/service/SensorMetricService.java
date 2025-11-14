package cassandra.service;

import cassandra.dto.SensorMetricDTO;

import java.util.List;

public interface SensorMetricService {

    /**
     * Get all metrics for a sensor in a specific month
     * Demonstrates reading wide columns - can return millions of metrics efficiently
     */
    List<SensorMetricDTO> getMetricsBySensorAndMonth(String sensorId, int year, int month);

    /**
     * Get metrics for a specific day with pagination
     */
    List<SensorMetricDTO> getMetricsBySensorAndDay(String sensorId, int year, int month, int day, int limit);

    /**
     * Get the latest N metrics for a sensor in a month
     */
    List<SensorMetricDTO> getLatestMetrics(String sensorId, int year, int month, int limit);

    /**
     * Get metrics for a specific hour range on a day
     */
    List<SensorMetricDTO> getMetricsByHourRange(String sensorId, int year, int month, int day, int hourStart, int hourEnd);
}
