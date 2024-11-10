package shamshaev.code.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
public abstract class TimeMapper {
    public String formatDateTime(LocalDateTime time) {
        var formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy в HH:mm");
        return formatter.format(time);
    }
}
