package lt.techin.springyne.dto.mapper;


import lt.techin.springyne.dto.ErrorFieldDto;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;


public class ErrorFieldMapper {

    public static ErrorFieldDto toErrorFieldDto(ObjectError objectError) {
        var errorFieldDto = new ErrorFieldDto();

        if (objectError instanceof FieldError) {
            var fieldError = (FieldError) objectError;

            errorFieldDto.setName(fieldError.getField());
            errorFieldDto.setRejectedValue(String.valueOf(fieldError.getRejectedValue()));
        }

        errorFieldDto.setError(objectError.getDefaultMessage());

        return errorFieldDto;
    }

}