package eu.europeana.pocimagesuperresolution.thumbnail.domain.service;

import eu.europeana.pocimagesuperresolution.thumbnail.domain.model.ThumbnailEnhancer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SuperResolutionServiceTest {

    @Mock
    private ThumbnailEnhancer enhancer;

    @InjectMocks
    private SuperResolutionService service;

    @Test
    void givenThumbnailFile_thenSuccess() {
        // given
        final String expectedOutput = "file://output.extension";
        when(enhancer.enhance(anyString())).thenReturn(expectedOutput);

        // when
        final String output = service.enhance("file://thumbnail.extension");

        // then
        assertEquals(expectedOutput, output);
    }
}
