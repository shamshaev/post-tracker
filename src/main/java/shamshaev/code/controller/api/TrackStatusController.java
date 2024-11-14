package shamshaev.code.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import shamshaev.code.dto.TrackStatusCreateDTO;
import shamshaev.code.dto.TrackStatusDTO;
import shamshaev.code.dto.TrackStatusUpdateDTO;
import shamshaev.code.service.TrackStatusService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1.0")
public class TrackStatusController {
    private final TrackStatusService statusService;

    @Operation(summary = "Gets all tracking statuses for all postal items")
    @GetMapping("/statuses")
    public Page<TrackStatusDTO> index(@RequestParam(defaultValue = "1") int page,
                                      @RequestParam(defaultValue = "10") int size) {

        return statusService.getAll(page, size);
    }

    @Operation(summary = "Gets specific tracking status")
    @GetMapping("/statuses/{id}")
    public TrackStatusDTO show(@PathVariable Long id) {

        return statusService.findById(id);
    }

    @Operation(summary = "Creates new tracking status for each step of specific postal item delivery")
    @PostMapping("/statuses")
    @ResponseStatus(HttpStatus.CREATED)
    public TrackStatusDTO create(@RequestBody @Valid TrackStatusCreateDTO statusData) {

        return statusService.create(statusData);
    }

    @Operation(summary = "Updates specific tracking status")
    @PutMapping("/statuses/{id}")
    public TrackStatusDTO update(@RequestBody @Valid TrackStatusUpdateDTO statusData, @PathVariable Long id) {

        return statusService.update(statusData, id);
    }

    @Operation(summary = "Deletes specific tracking status")
    @DeleteMapping("/statuses/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable Long id) {

        statusService.delete(id);
    }
}
