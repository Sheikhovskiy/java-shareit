package ru.practicum.shareit.request;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestInfoDto;

import java.util.List;

@UtilityClass
public class RequestMapper {

    public Request toRequestFromRequestCreateDto(RequestCreateDto requestCreateDto) {

        Request request = new Request();
        request.setDescription(requestCreateDto.getDescription());

        return request;
    }

    public RequestInfoDto toRequestInfoDtoFromRequest(Request request) {

        RequestInfoDto requestInfoDto = new RequestInfoDto();

        requestInfoDto.setId(request.getId());
        requestInfoDto.setDescription(request.getDescription());
        requestInfoDto.setCreated(request.getCreated());
        if (request.getItemList() != null) {
            requestInfoDto.setItems(ItemMapper.toListItemDtoFromListItem(request.getItemList()));
        }

        return requestInfoDto;
    }

    public List<RequestInfoDto> toListRequestInfoDtoFromListRequest(List<Request> requestList) {

        return requestList.stream()
                .map(RequestMapper::toRequestInfoDtoFromRequest)
                .toList();
    }


}
