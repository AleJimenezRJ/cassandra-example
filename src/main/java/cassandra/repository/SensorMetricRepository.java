package cassandra.repository;

import cassandra.entity.SensorMetric;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SensorMetricRepository extends CrudRepository<SensorMetric, String> {

    /**
     * Get all metrics for a sensor in a specific month (demonstrates wide column read)
     * This query can return MILLIONS of metrics from a single partition efficiently
     */
    @Query("SELECT * FROM sensor_metrics WHERE sensor_id=?0 AND year=?1 AND month=?2")
    List<SensorMetric> findBySensorAndMonth(String sensorId, int year, int month);

    /**
     * Get metrics for a specific sensor, month, and day with limit
     * Demonstrates efficient time-series access with clustering columns
     */
    @Query("SELECT * FROM sensor_metrics WHERE sensor_id=?0 AND year=?1 AND month=?2 AND day=?3 LIMIT ?4")
    List<SensorMetric> findBySensorAndDay(String sensorId, int year, int month, int day, int limit);

    /**
     * Get latest N metrics for a sensor in a month
     * Leverages descending clustering order for efficient recent data access
     */
    @Query("SELECT * FROM sensor_metrics WHERE sensor_id=?0 AND year=?1 AND month=?2 LIMIT ?3")
    List<SensorMetric> findLatestMetrics(String sensorId, int year, int month, int limit);

    /**
     * Get metrics for a specific sensor, month, day, and hour range
     * Shows how clustering columns enable efficient time range queries
     */
    @Query("SELECT * FROM sensor_metrics WHERE sensor_id=?0 AND year=?1 AND month=?2 AND day=?3 AND hour >= ?4 AND hour <= ?5")
    List<SensorMetric> findBySensorAndHourRange(String sensorId, int year, int month, int day, int hourStart, int hourEnd);
}
