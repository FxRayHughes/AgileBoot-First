package com.agileboot.domain.system.config.command;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Data;

/**
 * @author valarchie
 */
@Data
public class ConfigUpdateCommand {

    @NotNull
    @Positive
    private Long configId;
    @NotNull
    @NotEmpty
    private String configValue;

}
