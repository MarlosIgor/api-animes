package br.com.api.animes.controller;

import br.com.api.animes.model.Anime;
import br.com.api.animes.model.requests.AnimePostRequestBody;
import br.com.api.animes.model.requests.AnimePutRequestBody;
import br.com.api.animes.service.AnimeService;
import br.com.api.animes.util.AnimeCreator;
import br.com.api.animes.util.AnimePostRequestBodyCreator;
import br.com.api.animes.util.AnimePutRequestBodyCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
class AnimeControllerTest {
    // Indica que a classe de teste AnimeControllerTest será estendida com o SpringExtension para configurar o ambiente de teste

    @InjectMocks
    private AnimeController animeController;
    // Cria uma instância de AnimeController e injeta os mocks necessários

    @Mock
    private AnimeService animeServiceMock;
    // Cria um mock do AnimeService para simular o comportamento do serviço durante os testes

    @BeforeEach
    void setUp() {
        // Configuração inicial para simular comportamentos e retornos do serviço mockado


        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));
        // Cria uma instância de PageImpl contendo uma lista com um único elemento de Anime

        // Configura o retorno do método animeServiceMock.listAll() para uma lista paginada contendo um anime válido
        BDDMockito.when(animeServiceMock.listAll(ArgumentMatchers
                .any())).thenReturn(animePage);

        // Configura o retorno do método animeServiceMock.listAllNonPageable() para uma lista contendo um anime válido
        BDDMockito.when(animeServiceMock.listAllNonPageable())
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        // Configura o retorno do método animeServiceMock.findById() para um anime válido
        BDDMockito.when(animeServiceMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(AnimeCreator.createValidAnime());

        // Configura o retorno do método animeServiceMock.findByName() para uma lista contendo um anime válido
        BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        // Configura o retorno do método animeServiceMock.save() para um anime válido
        BDDMockito.when(animeServiceMock.save(ArgumentMatchers
                .any(AnimePostRequestBody.class))).thenReturn(AnimeCreator.createValidAnime());

        // Configura o retorno do método animeServiceMock.replace() para um anime de atualização válido
        BDDMockito.when(animeServiceMock.replace(ArgumentMatchers
                .any(AnimePutRequestBody.class))).thenReturn(AnimeCreator.createValidUpdateAnime());

        // Configura o método animeServiceMock.delete() para não retornar nada (void)
        BDDMockito.doNothing().when(animeServiceMock).delete(ArgumentMatchers.anyLong());
    }

    @Test
    @DisplayName("list returns list of anime inside page object when successful")
    void list_ReturnsListOfAnimesInsidePageObject_WhenSuccessful() {
        // Testa o método list do animeController e verifica se a página de animes retornada não é nula

        String expectedName = AnimeCreator.createValidAnime().getName();

        Page<Anime> animePage = animeController.list(null).getBody();

        Assertions.assertThat(animePage).isNotNull();

        // Verifica se a lista de animes na página não está vazia e tem tamanho 1
        Assertions.assertThat(animePage.toList())
                .isNotEmpty()
                .hasSize(1);

        // Verifica se o nome do primeiro anime na lista é igual ao nome esperado
        Assertions.assertThat(animePage.toList().get(0).getName())
                .isEqualTo(expectedName);
    }

    @Test
    @DisplayName("listAll returns list of anime when successful")
    void listAll_ReturnsListOfAnimes_WhenSuccessful() {
        // Testa o método listAll do animeController e verifica se a lista de animes retornada não é nula

        String expectedName = AnimeCreator.createValidAnime().getName();

        // Obtém a lista de animes do corpo da resposta do método listAll
        List<Anime> animeList = animeController.listAll().getBody();

        Assertions.assertThat(animeList)
                .isNotNull()// Verifica se a lista não é nula
                .isNotEmpty()// Verifica se a lista não está vazia
                .hasSize(1);// Verifica se a lista tem tamanho 1

        // Verifica se o nome do primeiro anime na lista é igual ao nome esperado
        Assertions.assertThat(animeList.get(0).getName())
                .isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findById returns anime when successful")
    void findById_ReturnsAnimes_WhenSuccessful() {
        // Testa o método findById do animeController e verifica se o anime retornado não é nulo

        Long expectedId = AnimeCreator.createValidAnime().getId();

        // Obtém o anime do corpo da resposta do método findById
        Anime anime = animeController.findById(1L).getBody();

        Assertions.assertThat(anime).isNotNull();// Verifica se o anime não é nulo
        Assertions.assertThat(anime.getId())
                .isNotNull()// Verifica se o ID do anime não é nulo
                .isEqualTo(expectedId);// Verifica se o ID do anime é igual ao ID esperado
    }

    @Test
    @DisplayName("findByName returns a list of anime when successful")
    void findByname_ReturnsListOfAnimes_WhenSuccessful() {
        // Testa o método findByName do animeController e verifica se a lista de animes retornada não é nula

        String expectedName = AnimeCreator.createValidAnime().getName();

        // Obtém a lista de animes do corpo da resposta do método findByName
        List<Anime> animeList = animeController.findByName("Dragon Ball Z").getBody();

        Assertions.assertThat(animeList)
                .isNotNull()// Verifica se a lista não é nula
                .isNotEmpty()// Verifica se a lista não está vazia
                .hasSize(1);// Verifica se a lista tem tamanho 1

        // Verifica se o nome do primeiro anime na lista é igual ao nome esperado
        Assertions.assertThat(animeList.get(0).getName())
                .isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByName returns an empty list of anime when anime is not found")
    void findByName_ReturnsEmptyListOfAnimes_WhenAnimeIsNotFound() {
        // Testa o método findByName do animeController quando nenhum anime é encontrado

        // Configura o serviço mockado para retornar uma lista vazia quando o método findByName é chamado
        BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        // Obtém a lista de animes do corpo da resposta do método findByName
        List<Anime> animeList = animeController.findByName("Dragon Ball Z").getBody();

        Assertions.assertThat(animeList)
                .isNotNull()// Verifica se a lista não é nula
                .isEmpty();// Verifica se a lista está vazia
    }

    @Test
    @DisplayName("save returns anime when successful")
    void save_ReturnsAnimes_WhenSuccessful() {
        // Testa o método save do animeController e verifica se o anime retornado não é nulo

        // Cria um AnimePostRequestBody válido para ser usado como entrada no método save
        AnimePostRequestBody animePostRequestBody = AnimePostRequestBodyCreator.createAnimePostRequestBody();

        // Obtém o anime do corpo da resposta do método save
        Anime anime = animeController.save(animePostRequestBody).getBody();

        Assertions.assertThat(anime)
                .isNotNull()// Verifica se o anime não é nulo
                .isEqualTo(AnimeCreator.createValidAnime());// Verifica se o anime é igual ao anime válido criado anteriormente
    }

    @Test
    @DisplayName("replace updates anime when successful")
    void replace_UpdatesAnimes_WhenSuccessful() {
        // Testa o método replace do animeController e verifica se a resposta é HttpStatus.NO_CONTENT

        // Cria um AnimePutRequestBody válido para ser usado como entrada no método replace
        AnimePutRequestBody animePutRequestBody = AnimePutRequestBodyCreator.createAnimePutRequestBody();

        // Chama o método replace e obtém a resposta do tipo ResponseEntity<Anime>
        ResponseEntity<Anime> anime = animeController.replace(animePutRequestBody);

        Assertions.assertThat(anime.getStatusCode())
                .isEqualTo(HttpStatus.NO_CONTENT);// Verifica se o código de status da resposta é HttpStatus.NO_CONTENT
    }

    @Test
    @DisplayName("delete removes anime when successful")
    void delete_RemovesAnimes_WhenSuccessful() {
        // Testa o método delete do animeController e verifica se a resposta é HttpStatus.NO_CONTENT

        Assertions.assertThatCode(() -> animeController.delete(1L))
                .doesNotThrowAnyException();// Verifica se nenhuma exceção é lançada ao chamar o método delete

        // Chama o método delete e obtém a resposta do tipo ResponseEntity<Void>
        ResponseEntity<Void> entity = animeController.delete(1L);

        Assertions.assertThat(entity)
                .isNotNull();// Verifica se a resposta não é nula
        Assertions.assertThat(entity.getStatusCode())
                .isEqualTo(HttpStatus.NO_CONTENT);// Verifica se o código de status da resposta é HttpStatus.NO_CONTENT
    }

}