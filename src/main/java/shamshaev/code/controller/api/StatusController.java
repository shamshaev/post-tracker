package shamshaev.code.controller.api;

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
@RequestMapping("/api/statuses")
public class StatusController {
    private final StatusService statusService;

    @GetMapping
    public Page<StatusDTO> index(@RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "10") int size) {

        return statusService.getAll(page, size);
    }

    @GetMapping("/{id}")
    public StatusDTO show(@PathVariable Long id) {

        return statusService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StatusDTO create(@RequestBody @Valid StatusCreateDTO statusData) {

        return statusService.create(statusData);
    }

    @PutMapping("/{id}")
    public StatusDTO update(@RequestBody @Valid StatusUpdateDTO statusData, @PathVariable Long id) {

        return statusService.update(statusData, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable Long id) {

        statusService.delete(id);
    }
}
