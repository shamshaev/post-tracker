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
import shamshaev.code.dto.PostalItemCreateDTO;
import shamshaev.code.dto.PostalItemDTO;
import shamshaev.code.dto.PostalItemUpdateDTO;
import shamshaev.code.service.PostalItemService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1.0")
public class PostalItemController {
    private final PostalItemService postalItemService;

    @Operation(summary = "Gets all postal items")
    @GetMapping("/postal_items")
    public Page<PostalItemDTO> index(@RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "10") int size) {

        return postalItemService.getAll(page, size);
    }

    @Operation(summary = "Gets specific postal item including all its tracking statuses")
    @GetMapping("/postal_items/{id}")
    public PostalItemDTO show(@PathVariable Long id) {

        return postalItemService.findById(id);
    }

    @Operation(summary = "Creates new postal item")
    @PostMapping("/postal_items")
    @ResponseStatus(HttpStatus.CREATED)
    public PostalItemDTO create(@RequestBody @Valid PostalItemCreateDTO postalItemData) {

        return postalItemService.create(postalItemData);
    }

    @Operation(summary = "Updates specific postal item")
    @PutMapping("/postal_items/{id}")
    public PostalItemDTO update(@RequestBody @Valid PostalItemUpdateDTO postalItemData, @PathVariable Long id) {

        return postalItemService.update(postalItemData, id);
    }

    @Operation(summary = "Deletes specific postal item")
    @DeleteMapping("/postal_items/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable Long id) {

        postalItemService.delete(id);
    }
}
