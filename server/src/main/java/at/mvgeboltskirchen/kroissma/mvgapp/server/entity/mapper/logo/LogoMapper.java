package at.mvgeboltskirchen.kroissma.mvgapp.server.entity.mapper.logo;

import at.mvgeboltskirchen.kroissma.mvgapp.rest.logo.LogoDTO;
import at.mvgeboltskirchen.kroissma.mvgapp.server.entity.Logo;
import at.mvgeboltskirchen.kroissma.mvgapp.server.entity.mapper.logo.LogoSummaryMapper.LogoSummary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = LogoSummaryMapper.class)
public interface LogoMapper {

    @Mapping(source = "imagePath", target = "imageBytes", qualifiedBy = LogoSummary.class)
    public LogoDTO logoToLogoDTO(Logo one);


}
