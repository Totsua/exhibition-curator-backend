package com.example.curator.service;


import com.example.curator.dto.ArtistDTO;
import com.example.curator.dto.ArtworkDTO;
import com.example.curator.exception.ErrorSendingGETRequestException;
import com.example.curator.exception.InvalidArtworkException;
import com.example.curator.exception.UnknownAPIOriginException;
import com.example.curator.model.ArtworkResults;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

@Service
public class ApiServiceImpl implements ApiService{
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final HttpClient client  = HttpClient.newBuilder().build();
    private static final String CHICAGO_ARTWORK_SEARCH_URL = "https://api.artic.edu/api/v1/artworks/";



    @Override
    public ArtworkResults getArtworkSearchResults(String query, Integer page) {
        ArrayList<ArtworkDTO> chiArtworkResults = getChiAPISearchResults(query, page);

        ArrayList<ArtworkDTO> allArtworkResults = new ArrayList<>(chiArtworkResults);

        // todo: Add the Fitzwilliam API methods and place all into allArtworkResults

        return new ArtworkResults(query,page,allArtworkResults);
    }

    @Override
    public ArtworkDTO getApiArtworkDetails(Long id, String apiOrigin){
        ArtworkDTO artwork = new ArtworkDTO();
        switch(apiOrigin){
            case "Chicago Institute":
                artwork = getChiArtById(id.toString());
                break;
            default:
                throw new UnknownAPIOriginException("Error: API Origin \""+ apiOrigin + "\" is unknown.");
        }

        if(artwork.getId() == 0 || artwork.getArtist().getApiID() == 0){
            // todo: throw 'no artwork' exception
            throw new InvalidArtworkException(
                    String.format("There are no artworks with id: %s and apiOrigin: \"%s\"",id,apiOrigin));
        }
        return artwork;
    }

    private ArrayList<ArtworkDTO> getChiAPISearchResults(String query, Integer page){
        ArrayList<ArtworkDTO> artworkResults = new ArrayList<>();
        String searchUrl = String.format("%s?q=%s&page=%d&limit=10", CHICAGO_ARTWORK_SEARCH_URL, query, page);

        JsonNode chiSearchRoot = sendGetRequest(searchUrl);


        // Check to see if total is more than 0, if not then don't check the data
        int total = chiSearchRoot.path("pagination").path("total").asInt();

        if(total <= 0) {
            return artworkResults;
        }
            JsonNode chiArtResults = chiSearchRoot.findPath("data");
            for(JsonNode node : chiArtResults){
                String artId = node.path("id").asText();

                ArtworkDTO art = getChiArtById(artId);

                if(art.getId() != 0 && art.getArtist().getApiID() != 0){
                    artworkResults.add(art);
                }

            }


        return artworkResults;
    }


    private JsonNode sendGetRequest(String url){
        HttpRequest chiSearchRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
//                .header("AIC-User-Agent: exhibition-curator (*Insert Email*)")
                .GET()
                .build();

        try{
            HttpResponse<String> response = client.send(chiSearchRequest,HttpResponse.BodyHandlers.ofString());
            return mapper.readTree(response.body());
        } catch (IOException | InterruptedException e) {
            // Todo 28/05/25: Make new exception that is thrown to show trouble getting api data
            e.printStackTrace();
            throw new ErrorSendingGETRequestException("Server Error: Unable To Read External GET Response");
        }

    }



    private ArtworkDTO getChiArtById(String id){
        String url = CHICAGO_ARTWORK_SEARCH_URL + id;
        JsonNode rootNode = sendGetRequest(url);

        JsonNode data = rootNode.findPath("data");

        ArtworkDTO art = ArtworkDTO.builder()
                .id(data.path("id").asLong(0))
                .title(data.path("title").asText("Unknown"))
                .altText(data.path("thumbnail").path("alt_text").asText("There is no alt text for this artwork"))
                .apiOrigin("Chicago Institute")
                .build();


        ArtistDTO artist = ArtistDTO.builder()
                .apiID(data.path("artist_id").asLong(0))
                .name(data.path("artist_title").asText("Unknown"))
                .build();


        art.setArtist(artist);
        art.setDescription(data.path("short_description").asText("There is no description for this artpiece"));
        //todo: check for imageURL to see if empty
        String imageUrl = rootNode.findPath("config").path("iiif_url").asText() + "/" + data.path("image_id").asText() + "/full/843,/0/default.jpg";
        art.setImageUrl(imageUrl);

        return art;
    }

}
