package org.example.mappers;


import org.example.dto.JobsDto;
import org.example.models.Jobs;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface JobsMapper {
    JobsMapper INSTANCE = Mappers.getMapper(JobsMapper.class);
    JobsDto toJobsDto(Jobs j);
    Jobs toModel(JobsDto dto);


}

