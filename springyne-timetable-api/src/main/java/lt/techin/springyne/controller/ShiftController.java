package lt.techin.springyne.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lt.techin.springyne.dto.ModuleDto;
import lt.techin.springyne.dto.ShiftDto;
import lt.techin.springyne.dto.mapper.ModuleMapper;
import lt.techin.springyne.dto.mapper.ShiftMapper;
import lt.techin.springyne.model.Module;
import lt.techin.springyne.repository.ShiftRepository;
import lt.techin.springyne.model.Shift;
import lt.techin.springyne.service.ModuleService;
import lt.techin.springyne.service.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/shifts")
public class ShiftController {
    @Autowired
    ShiftService shiftService;

    @Autowired
    ObjectMapper objectMapper;

    public ShiftController(ShiftService shiftService) {
        this.shiftService = shiftService;
    }
    @GetMapping
    public List<Shift> getShifts(){
        return shiftService.getShifts();
    }
    @PostMapping
    public ResponseEntity<ShiftDto> createShift(@RequestBody ShiftDto shiftDto) {
        var createdShift = shiftService.createShift(ShiftMapper.toShift(shiftDto));
        return ok(ShiftMapper.toShiftDto(createdShift));
    }
    @PatchMapping("/{shiftId}")
    public ResponseEntity<ShiftDto> editShift(@PathVariable Long shiftId, @RequestBody ShiftDto shiftDto) {
        var editedShift = shiftService.editShift(shiftId, ShiftMapper.toShift(shiftDto));
        return ok(ShiftMapper.toShiftDto(editedShift));
    }
    @GetMapping("/{shiftId}")
    public ResponseEntity<Shift> getShift(@PathVariable Long shiftId){
        var shiftOptional = shiftService.getShift(shiftId);

        var responseEntity = shiftOptional
                .map(shift -> ok(shift))
                .orElseGet(() -> ResponseEntity.notFound().build());
        return responseEntity;
    }
}