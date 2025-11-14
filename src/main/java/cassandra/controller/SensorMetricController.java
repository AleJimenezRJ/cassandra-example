package cassandra.controller;

import cassandra.dto.SensorMetricDTO;
import cassandra.service.SensorMetricService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/sensors")
@Tag(name = "Sensor Metrics (Wide Column Demo)", 
     description = "Demonstrates Cassandra's wide column design for time-series data. " +
                   "Unlike SQL, a single sensor partition can store MILLIONS of metrics efficiently. " +
                   "This showcases how CQL handles massive amounts of related data in one partition.")
public class SensorMetricController {

    private final SensorMetricService sensorMetricService;

    @Autowired
    public SensorMetricController(SensorMetricService sensorMetricService) {
        this.sensorMetricService = sensorMetricService;
    }

    @Operation(summary = "Get all metrics for a sensor in a specific month", 
               description = "WIDE COLUMN DEMO: Returns all metrics for a sensor+month partition. " +
                           "In production, this could return MILLIONS of records from ONE partition. " +
                           "In SQL, this would require complex joins or separate tables. " +
                           "Cassandra stores this as wide rows with clustering columns for efficient access.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved sensor metrics"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{sensorId}/metrics/month/{year}/{month}")
    public ResponseEntity<List<SensorMetricDTO>> getMetricsByMonth(
            @Parameter(description = "Sensor ID", required = true, example = "TEMP-001")
            @PathVariable String sensorId,
            
            @Parameter(description = "Year", required = true, example = "2025")
            @PathVariable int year,
            
            @Parameter(description = "Month (1-12)", required = true, example = "11")
            @PathVariable int month) {
        
        try {
            List<SensorMetricDTO> metrics = sensorMetricService.getMetricsBySensorAndMonth(sensorId, year, month);
            return new ResponseEntity<>(metrics, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get metrics for a specific day with limit", 
               description = "Retrieves metrics for a sensor on a specific day. " +
                           "Uses clustering columns (day, hour, timestamp) for efficient time-series queries. " +
                           "The limit parameter enables pagination through potentially millions of metrics.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved sensor metrics"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{sensorId}/metrics/day/{year}/{month}/{day}")
    public ResponseEntity<List<SensorMetricDTO>> getMetricsByDay(
            @Parameter(description = "Sensor ID", required = true, example = "TEMP-001")
            @PathVariable String sensorId,
            
            @Parameter(description = "Year", required = true, example = "2025")
            @PathVariable int year,
            
            @Parameter(description = "Month (1-12)", required = true, example = "11")
            @PathVariable int month,
            
            @Parameter(description = "Day (1-31)", required = true, example = "13")
            @PathVariable int day,
            
            @Parameter(description = "Maximum number of metrics to retrieve")
            @RequestParam(defaultValue = "100") int limit) {
        
        try {
            List<SensorMetricDTO> metrics = sensorMetricService.getMetricsBySensorAndDay(sensorId, year, month, day, limit);
            return new ResponseEntity<>(metrics, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get latest N metrics for a sensor in a month", 
               description = "Leverages DESCENDING clustering order to efficiently retrieve recent metrics. " +
                           "In Cassandra, the newest data is at the 'top' of the partition, enabling O(1) access. " +
                           "SQL would require expensive ORDER BY operations on massive tables.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved latest metrics"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{sensorId}/metrics/latest/{year}/{month}")
    public ResponseEntity<List<SensorMetricDTO>> getLatestMetrics(
            @Parameter(description = "Sensor ID", required = true, example = "TEMP-001")
            @PathVariable String sensorId,
            
            @Parameter(description = "Year", required = true, example = "2025")
            @PathVariable int year,
            
            @Parameter(description = "Month (1-12)", required = true, example = "11")
            @PathVariable int month,
            
            @Parameter(description = "Number of latest metrics to retrieve")
            @RequestParam(defaultValue = "50") int limit) {
        
        try {
            List<SensorMetricDTO> metrics = sensorMetricService.getLatestMetrics(sensorId, year, month, limit);
            return new ResponseEntity<>(metrics, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get metrics for a specific hour range on a day", 
               description = "Demonstrates range queries on clustering columns. " +
                           "Cassandra can efficiently filter by hour ranges within a partition. " +
                           "Perfect for time-series analytics like 'show me all metrics between 8am-5pm'.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved metrics for hour range"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{sensorId}/metrics/range/{year}/{month}/{day}")
    public ResponseEntity<List<SensorMetricDTO>> getMetricsByHourRange(
            @Parameter(description = "Sensor ID", required = true, example = "CPU-001")
            @PathVariable String sensorId,
            
            @Parameter(description = "Year", required = true, example = "2025")
            @PathVariable int year,
            
            @Parameter(description = "Month (1-12)", required = true, example = "11")
            @PathVariable int month,
            
            @Parameter(description = "Day (1-31)", required = true, example = "13")
            @PathVariable int day,
            
            @Parameter(description = "Start hour (0-23)", required = true)
            @RequestParam int hourStart,
            
            @Parameter(description = "End hour (0-23)", required = true)
            @RequestParam int hourEnd) {
        
        try {
            if (hourStart < 0 || hourStart > 23 || hourEnd < 0 || hourEnd > 23 || hourStart > hourEnd) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            
            List<SensorMetricDTO> metrics = sensorMetricService.getMetricsByHourRange(sensorId, year, month, day, hourStart, hourEnd);
            return new ResponseEntity<>(metrics, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get current month metrics (convenience endpoint)", 
               description = "Returns all metrics for the current month. Useful for real-time monitoring dashboards.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved current month metrics")
    @GetMapping("/{sensorId}/metrics/current")
    public ResponseEntity<List<SensorMetricDTO>> getCurrentMonthMetrics(
            @Parameter(description = "Sensor ID", required = true, example = "TEMP-001")
            @PathVariable String sensorId) {
        
        try {
            LocalDate now = LocalDate.now();
            List<SensorMetricDTO> metrics = sensorMetricService.getMetricsBySensorAndMonth(
                sensorId, 
                now.getYear(), 
                now.getMonthValue()
            );
            return new ResponseEntity<>(metrics, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Health check", 
               description = "Simple endpoint to verify the sensor metrics API is running")
    @ApiResponse(responseCode = "200", description = "API is healthy")
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return new ResponseEntity<>("Sensor Metrics API is running! Wide column power activated! 🚀", HttpStatus.OK);
    }
}
