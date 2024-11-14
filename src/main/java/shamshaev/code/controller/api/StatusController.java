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
import shamshaev.code.dto.StatusCreateDTO;
import shamshaev.code.dto.StatusDTO;
import shamshaev.code.dto.StatusUpdateDTO;
import shamshaev.code.service.StatusService;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class StatusController {
    private final StatusService statusService;

    @Operation(summary = "Gets all tracking statuses for all postal items")
    @GetMapping("/statuses")
    public Page<StatusDTO> index(@RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "10") int size) {

        return statusService.getAll(page, size);
    }

    @Operation(summary = "Gets specific tracking status")
    @GetMapping("/statuses/{id}")
    public StatusDTO show(@PathVariable Long id) {

        return statusService.findById(id);
    }

    @Operation(summary = "Creates new tracking status for each step of specific postal item delivery")
    @PostMapping("/statuses")
    @ResponseStatus(HttpStatus.CREATED)
    public StatusDTO create(@RequestBody @Valid StatusCreateDTO statusData) {

        return statusService.create(statusData);
    }

    @Operation(summary = "Updates specific tracking status")
    @PutMapping("/statuses/{id}")
    public StatusDTO update(@RequestBody @Valid StatusUpdateDTO statusData, @PathVariable Long id) {

        return statusService.update(statusData, id);
    }

    @Operation(summary = "Deletes specific tracking status")
    @DeleteMapping("/statuses/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable Long id) {

        statusService.delete(id);
    }
}
