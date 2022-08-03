package licenta.andreibalinth.backend.controller;

import licenta.andreibalinth.backend.entities.dto.ScheduleEntryDto;
import licenta.andreibalinth.backend.service.ScheduleService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/schedule")
public class ScheduleController {
    ScheduleService service;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getAllForUser(@PathVariable("userId") Long userId){
        return ResponseEntity.ok(service.getAllEntriesForUser(userId));
    }

    @GetMapping("/shoppingList/{userId}")
    public ResponseEntity<?> getShoppingListForUser(@PathVariable("userId") Long userId){
        return ResponseEntity.ok(service.getShoppingListForUser(userId));
    }

    @PostMapping("/add/{userId}")
    public ResponseEntity<?> addForUser(@PathVariable("userId") Long userId, @RequestBody ScheduleEntryDto dto){
        service.addEntry(dto, userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody ScheduleEntryDto dto){
        service.updateEntry(dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{entryId}")
    public ResponseEntity<?> delete(@PathVariable("entryId") Long entryId){
        service.deleteEntry(entryId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{entryId}/{wasCooked}")
    public ResponseEntity<?> deleteAfterCooked(@PathVariable("entryId") Long entryId, @PathVariable("wasCooked") Boolean wasCooked){
        service.deleteEntryAndRemoveIngredients(entryId, wasCooked);
        return ResponseEntity.ok().build();
    }
}
