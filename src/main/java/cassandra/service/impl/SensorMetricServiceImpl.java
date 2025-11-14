package cassandra.service.impl;

import cassandra.dto.SensorMetricDTO;
import cassandra.entity.SensorMetric;
import cassandra.repository.SensorMetricRepository;
import cassandra.service.SensorMetricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SensorMetricServiceImpl implements SensorMetricService {

    private final SensorMetricRepository sensorMetricRepository;

    @Autowired
    public SensorMetricServiceImpl(SensorMetricRepository sensorMetricRepository) {
        this.sensorMetricRepository = sensorMetricRepository;
    }

    @Override
    public List<SensorMetricDTO> getMetricsBySensorAndMonth(String sensorId, int year, int month) {
        List<SensorMetric> metrics = sensorMetricRepository.findBySensorAndMonth(sensorId, year, month);
        return convertToMetricDTOs(metrics);
    }

    @Override
    public List<SensorMetricDTO> getMetricsBySensorAndDay(String sensorId, int year, int month, int day, int limit) {
        List<SensorMetric> metrics = sensorMetricRepository.findBySensorAndDay(sensorId, year, month, day, limit);
        return convertToMetricDTOs(metrics);
    }

    @Override
    public List<SensorMetricDTO> getLatestMetrics(String sensorId, int year, int month, int limit) {
        List<SensorMetric> metrics = sensorMetricRepository.findLatestMetrics(sensorId, year, month, limit);
        return convertToMetricDTOs(metrics);
    }

    @Override
    public List<SensorMetricDTO> getMetricsByHourRange(String sensorId, int year, int month, int day, int hourStart, int hourEnd) {
        List<SensorMetric> metrics = sensorMetricRepository.findBySensorAndHourRange(sensorId, year, month, day, hourStart, hourEnd);
        return convertToMetricDTOs(metrics);
    }

    private List<SensorMetricDTO> convertToMetricDTOs(List<SensorMetric> metrics) {
        List<SensorMetricDTO> metricDTOs = new ArrayList<>();
        
        for (SensorMetric metric : metrics) {
            SensorMetricDTO dto = new SensorMetricDTO(
                    metric.getSensorId(),
                    metric.getYear(),
                    metric.getMonth(),
                    metric.getDay(),
                    metric.getHour(),
                    metric.getRecordedAt(),
                    metric.getMetricType(),
                    metric.getMetricValue(),
                    metric.getUnit(),
                    metric.getLocation(),
                    metric.getStatus(),
                    metric.getMetadata()
            );
            metricDTOs.add(dto);
        }
        
        return metricDTOs;
    }
}
