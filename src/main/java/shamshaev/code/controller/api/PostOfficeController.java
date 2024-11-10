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
import shamshaev.code.dto.PostOfficeCreateDTO;
import shamshaev.code.dto.PostOfficeDTO;
import shamshaev.code.dto.PostOfficeUpdateDTO;
import shamshaev.code.service.PostOfficeService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/post_offices")
public class PostOfficeController {
    private final PostOfficeService postOfficeService;

    @GetMapping
    public Page<PostOfficeDTO> index(@RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "10") int size) {

        return postOfficeService.getAll(page, size);
    }

    @GetMapping("/{id}")
    public PostOfficeDTO show(@PathVariable Long id) {

        return postOfficeService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostOfficeDTO create(@RequestBody @Valid PostOfficeCreateDTO postOfficeData) {

        return postOfficeService.create(postOfficeData);
    }

    @PutMapping("/{id}")
    public PostOfficeDTO update(@RequestBody @Valid PostOfficeUpdateDTO postOfficeData, @PathVariable Long id) {

        return postOfficeService.update(postOfficeData, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable Long id) {

        postOfficeService.delete(id);
    }
}
