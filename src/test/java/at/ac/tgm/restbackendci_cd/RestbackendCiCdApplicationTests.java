package at.ac.tgm.restbackendci_cd;

import org.springframework.test.context.ActiveProfiles;
import at.ac.tgm.restbackendci_cd.repository.AnalysisRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;



@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RestbackendCiCdApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AnalysisRepo repository;

    @Autowired
    private ObjectMapper objectMapper;

    @org.springframework.beans.factory.annotation.Value("${app.admin.password}")
    private String adminPassword;

    @BeforeEach
    void setup() {
        repository.deleteAll();
    }

    //Erster Test, Get Alles
    @Test
    void contextLoads() {
        // Dieser Test prüft NUR ob die App startet. 
        // Wenn das geht, ist die Datenbank-Verbindung OK.
        org.junit.jupiter.api.Assertions.assertNotNull(repository);
    }

    @Test
    void testGetAllEmpty() throws Exception {
        // Wir holen uns die Antwort manuell um sie bei Fehlern besser zu loggen
        String content = mockMvc.perform(get("/api/analysis")
                        .header("X-API-KEY", adminPassword))
                .andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        
        System.out.println("DEBUG - API Response: " + content);
        
        // Prüfen ob 'content' leer ist (für Spring Data Page Objekte)
        org.junit.jupiter.api.Assertions.assertTrue(
            content.contains("\"content\":[]"), 
            "Das Feld 'content' sollte leer sein, aber die Antwort war: " + content
        );
    }

}
