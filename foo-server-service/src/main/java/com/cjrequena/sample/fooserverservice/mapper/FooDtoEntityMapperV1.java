package com.cjrequena.sample.fooserverservice.mapper;

import com.cjrequena.sample.fooserverservice.domain.FooEntityV1;
import com.cjrequena.sample.fooserverservice.dto.FooDTOV1;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

/**
 * <p>
 * <p>
 * <p>
 * <p>
 * @author cjrequena
 * @version 1.0
 * @since JDK1.8
 * @see
 *
 */
@Mapper(
  componentModel = "spring",
  nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface FooDtoEntityMapperV1 extends DTOEntityMapper<FooDTOV1, FooEntityV1> {
}
