package com.example.curator.service;

import com.example.curator.dto.ApiArtworkIdDTO;
import com.example.curator.dto.ArtistDTO;
import com.example.curator.dto.ArtworkDTO;
import com.example.curator.dto.ExhibitionDTO;
import com.example.curator.model.Artist;
import com.example.curator.model.Artwork;
import com.example.curator.model.ArtworkResults;
import com.example.curator.model.Exhibition;
import com.example.curator.repository.ArtistRepository;
import com.example.curator.repository.ArtworkRepository;
import com.example.curator.repository.ExhibitionRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CuratorServiceImpl implements CuratorService{
    @Autowired
    ApiService apiService;
    @Autowired
    ExhibitionRepository exhibitionRepository;
    @Autowired
    ArtworkRepository artworkRepository;
    @Autowired
    ArtistRepository artistRepository;

    @Override
    public ArtworkResults getArtworkSearchResults(String query, Integer page) {
        if(page == null || page < 1){
            // todo: if page is below one or null throw custom error
        }

        ArtworkResults artworkSearchResults = apiService.getArtworkSearchResults(query,page);
        return artworkSearchResults;
    }

    @Override
    public ArtworkDTO getApiArtworkDetails(ApiArtworkIdDTO apiArtworkIdDTO) {

        ArtworkDTO artwork = apiService.getApiArtworkDetails(apiArtworkIdDTO.getArtId(), apiArtworkIdDTO.getApiOrigin());
        // todo: catch custom artwork exception
        return artwork;
    }

    @Override
    public ExhibitionDTO createExhibition(String title) {
        if(exhibitionRepository.existsByTitle(title)){
            System.out.println("Exhibition already exists with that title");
            // todo: throw custom 'exhibition already exists' exception
        }


        Exhibition exhibition = Exhibition.builder().title(title).description("").artworks(new ArrayList<>()).build();
        Exhibition exhibitionDTO = exhibitionRepository.save(exhibition);
        return exhibitionToDTOMapper(exhibitionDTO);
    }

    @Override
    public List<ExhibitionDTO> getAllExhibitions() {
        List<Exhibition> exhibitionList = new ArrayList<>();
        exhibitionRepository.findAll().forEach(exhibitionList::add);
        return exhibitionList.stream().map(this::exhibitionToDTOMapper).toList();
    }

    // Exhibition details that are updated should only be title and description
    @Override
    public ExhibitionDTO updateExhibitionDetails(Long id, ExhibitionDTO exhibitionDTOUpdate){
        if(!exhibitionRepository.existsById(id)){
            System.out.println("no exhibition with id: " + id);
            //todo: throw custom 'no item found' exception

        }
        Optional<Exhibition> optionalExhibitionInDB = exhibitionRepository.findById(id);
        Exhibition exhibitionInDB = optionalExhibitionInDB.get();
        if(!StringUtils.isBlank(exhibitionDTOUpdate.getTitle())){
            exhibitionInDB.setTitle(exhibitionDTOUpdate.getTitle());
        }
        if(exhibitionDTOUpdate.getDescription() != null){
            exhibitionInDB.setDescription(exhibitionDTOUpdate.getDescription());
        }

        return exhibitionToDTOMapper(exhibitionRepository.save(exhibitionInDB));
    }


    @Override
    public void deleteExhibition(Long id) {
        if(exhibitionRepository.existsById(id)) {
            exhibitionRepository.deleteById(id);
        }
        else {
            System.out.println("no entry with that id");
            //todo: throw custom no item with that id exception
        }
    }



    // todo: optimise repository calls ( do the find as an optional object then do .isPresent() as the if check)
    //  don't save artwork first, do artist checks first
    //  check if the artwork is already in the exhibition, if so throw an exception
    @Override
    public ExhibitionDTO saveExhibitionArt(Long id, ApiArtworkIdDTO artworkDTO) {
        if(!exhibitionRepository.existsById(id)){
            // todo: throw custom 'no exhibition with that id' exception
        }
        Exhibition exhibitionInDB = exhibitionRepository.findById(id).get();
        ArtworkDTO artworkApi = apiService.getApiArtworkDetails(artworkDTO.getArtId(), artworkDTO.getApiOrigin());
        Artwork artwork = new Artwork();
        Artist artist = new Artist();

        if(artistRepository.existsByApiIdAndApiOrigin(artworkApi.getArtist().getApiID(),artworkApi.getApiOrigin())){
            artist = artistRepository.findByApiIdAndApiOrigin(artworkApi.getArtist().getApiID(),artworkApi.getApiOrigin()).get();
        }
        else {
             artist = Artist.builder()
                    .name(artworkApi.getArtist().getName())
                    .apiOrigin(artworkApi.getApiOrigin())
                    .apiId(artworkApi.getArtist().getApiID())
                    .build();
             artist = artistRepository.save(artist);
        }


        if(artworkRepository.existsByApiIdAndApiOrigin(artworkApi.getId(), artworkApi.getApiOrigin())){
            artwork = artworkRepository.findByApiIdAndApiOrigin(artworkApi.getId(),artworkApi.getApiOrigin()).get();
        }else{
            artwork = dtoToArtwork(artworkApi);
            artwork.setArtist(artist);
            artwork = artworkRepository.save(artwork);
        }

        if(exhibitionInDB.getArtworks().contains(artwork)){
            // todo: throw 'artwork already in exhibition' error
        }

        ArrayList<Artwork> artworkArrayList = new ArrayList<>(exhibitionInDB.getArtworks());

        artworkArrayList.add(artwork);
        exhibitionInDB.setArtworks(artworkArrayList);

        return exhibitionToDTOMapper(exhibitionRepository.save(exhibitionInDB));
    }




    @Override
    public ExhibitionDTO deleteExhibitionArt(Long exhibitionId, ApiArtworkIdDTO artworkDTO) {
        if(!exhibitionRepository.existsById(exhibitionId)){
            // todo: throw custom 'no exhibition with that id' exception
        }
        Exhibition exhibitionInDB = exhibitionRepository.findById(exhibitionId).get();
        Optional<Artwork> optionalArtwork = artworkRepository.findByApiIdAndApiOrigin(artworkDTO.getArtId(), artworkDTO.getApiOrigin());
        if(optionalArtwork.isEmpty()){
            // todo: throw 'artwork not saved on database' error
        }
        Artwork artwork = optionalArtwork.get();

        if(!exhibitionInDB.getArtworks().contains(artwork)){
            // todo: throw 'artwork not in exhibition' error
        }

        ArrayList<Artwork> artworkArrayList = new ArrayList<>(exhibitionInDB.getArtworks());

        artworkArrayList.remove(artwork);
        exhibitionInDB.setArtworks(artworkArrayList);
        return exhibitionToDTOMapper(exhibitionRepository.save(exhibitionInDB));

    }

    private ExhibitionDTO exhibitionToDTOMapper(Exhibition exhibition) {
        ArrayList<ArtworkDTO> artworks = new ArrayList<>();
        for(Artwork artwork: exhibition.getArtworks()){
            artworks.add(artworkToDTO(artwork));
        }

        return ExhibitionDTO.builder()
                .id(exhibition.getId())
                .title(exhibition.getTitle())
                .description(exhibition.getDescription())
                .artworks(artworks)
                .build();
    }

    private ArtworkDTO artworkToDTO(Artwork artwork){
        return ArtworkDTO.builder()
                .id(artwork.getApiId())
                .title(artwork.getTitle())
                .description(artwork.getDescription())
                .altText(artwork.getAltText())
                .apiOrigin(artwork.getApiOrigin())
                .imageUrl(artwork.getImageUrl())
                .artist(new ArtistDTO(artwork.getArtist().getApiId(),artwork.getArtist().getName()))
                .build();
    }
    private Artwork dtoToArtwork(ArtworkDTO artworkDTO){
       Artist artist = Artist.builder()
               .name(artworkDTO.getArtist().getName())
               .apiOrigin(artworkDTO.getApiOrigin())
               .apiId(artworkDTO.getArtist().getApiID())
               .build();

        return Artwork.builder()
                .apiId(artworkDTO.getId())
                .title(artworkDTO.getTitle())
                .description(artworkDTO.getDescription())
                .altText(artworkDTO.getAltText())
                .artist(artist)
                .imageUrl(artworkDTO.getImageUrl())
                .apiOrigin(artworkDTO.getApiOrigin())
                .build();
    }
}
