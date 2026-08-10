package com.gogreen.ai.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class PlantDiaryEntryRequestDto {

    @NotNull
    private UUID diaryId;

    @NotNull
    private LocalDate entryDate;

    @Size(max = 2000)
    private String note;

    @Size(max = 500)
    private String photoUrl;
}
