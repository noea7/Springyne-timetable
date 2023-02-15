package lt.techin.springyne.dto.mapper;

import lt.techin.springyne.dto.RoomDto;
import lt.techin.springyne.model.Room;

import java.time.LocalDateTime;

public class RoomMapper {

    public static Room toRoom(RoomDto roomDto) {

        Room room = new Room();
        room.setName(roomDto.getName());
        room.setBuilding(roomDto.getBuilding());
        room.setDescription(roomDto.getDescription());
        room.setDeleted(roomDto.isDeleted());
        room.setLastModifiedDate(roomDto.getLastModifiedDate());

        return room;
    }

    public static RoomDto toRoomDto(Room room){

        RoomDto roomDto = new RoomDto();
        roomDto.setName(room.getName());
        roomDto.setBuilding(room.getBuilding());
        roomDto.setDescription(room.getDescription());
        roomDto.setDeleted(room.isDeleted());
        roomDto.setLastModifiedDate((room.getLastModifiedDate()));

        return roomDto;
    }
}