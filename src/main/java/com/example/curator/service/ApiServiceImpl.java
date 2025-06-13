package com.example.curator.service;


import com.example.curator.dto.ArtistDTO;
import com.example.curator.dto.ArtworkDTO;
import com.example.curator.exception.APIPageOutOfBoundsException;
import com.example.curator.exception.ErrorSendingGETRequestException;
import com.example.curator.exception.InvalidArtworkException;
import com.example.curator.exception.UnknownAPIOriginException;
import com.example.curator.model.ArtworkResults;
import com.example.curator.model.ChicagoSearchResults;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Random;

@Slf4j
@Service
public class ApiServiceImpl implements ApiService{
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final HttpClient client  = HttpClient.newBuilder().build();
    private static final String CHICAGO_ARTWORK_SEARCH_URL = "https://api.artic.edu/api/v1/artworks/";
    private static final int PAGE_SIZE = 5;
    private static final int MAX_RANDOM_ATTEMPTS = 20;
    private static final int TOTAL_MET_OBJECTS = 922041;


    @Override
    public ArtworkResults getArtworkSearchResults(String query, Integer page) {

        ArrayList<ArtworkDTO> allArtworkResults;
        int total_pages;

        try{
            ChicagoSearchResults chicagoSearchResults = getChiAPISearchResults(query, page);
            total_pages = chicagoSearchResults.getTotal_pages();
            allArtworkResults = new ArrayList<>(chicagoSearchResults.getArtworks());

        }catch (APIPageOutOfBoundsException e){
            log.warn("Chicago API page out of bounds for query '{}' page {}: {}", query, page, e.getMessage());
            // We mainly work on the Chicago api because met is inconsistent
            // therefore if chicago runs out the error is thrown
            throw e;
        }

        try{
            ArrayList<ArtworkDTO> metArtworksResults = getMetSearchResults(query, page);
            allArtworkResults.addAll(metArtworksResults);
        }catch (APIPageOutOfBoundsException e){
            // We log if the met throws an exception
            log.info("Met API page out of bounds for query '{}' page {}: {}", query, page, e.getMessage());
        }


        return new ArtworkResults(query,page,allArtworkResults,total_pages);
    }

    @Override
    public ArtworkDTO getRandomMetArtwork() {
        ArtworkDTO artwork = null;

        Random random = new Random();

        int attempts = 0;

        while (attempts < MAX_RANDOM_ATTEMPTS) {
            attempts++;
            int randomObjectId = random.nextInt(TOTAL_MET_OBJECTS) + 1;
            String url = "https://collectionapi.metmuseum.org/public/collection/v1/objects/" + randomObjectId;

            JsonNode result = sendGetRequest(url);

            boolean hasMessage = result.has("message");


            boolean hasValidConstituents = result.has("constituents") &&
                    !result.get("constituents").isNull() &&
                    result.get("constituents").isArray() &&
                    !result.get("constituents").isEmpty();

            if (!hasMessage && hasValidConstituents) {
                JsonNode constituent = result.get("constituents").get(0);
                long artistApiID = constituent.path("constituentID").asLong(0L);

                if (artistApiID == 0L) {
                    continue;
                }

                // We only want artworks with images
                String imageUrl = result.path("primaryImage").asText("");
                if (imageUrl.isEmpty()){
                    continue;
                }

                ArtistDTO artist = ArtistDTO.builder()
                        .name(constituent.path("name").asText("Unknown Artist"))
                        .apiID(artistApiID)
                        .build();

                artwork = ArtworkDTO.builder()
                        .id(result.path("objectID").asLong())
                        .title(result.path("title").asText("Untitled Art"))
                        .description("No Description Provided From The Metropolitan Museum")
                        .imageUrl(imageUrl)
                        .apiOrigin("The MET")
                        .altText("Artwork from the MET: " + result.path("title").asText("Untitled Art"))
                        .artist(artist)
                        .build();

                break;
            }
        }

        if(attempts == MAX_RANDOM_ATTEMPTS){
            throw new APIPageOutOfBoundsException("Max Attempts Tried");
        }

        return artwork;
    }



    @Override
    public ArtworkDTO getApiArtworkDetails(Long id, String apiOrigin){
        ArtworkDTO artwork = new ArtworkDTO();
        switch (apiOrigin) {
            case "Chicago Institute":
                artwork = getChiArtById(id.toString());
                break;
            /*case "Fitzwilliam":

                break;*/
             case "The MET":
                 artwork = getMetArtById(id.toString());
                 break;
            default:
                throw new UnknownAPIOriginException("Error: API Origin \"" + apiOrigin + "\" is unknown.");
        }

        return artwork;
    }

    private JsonNode sendGetRequest(String url){
        HttpRequest chiSearchRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
//               todo for new user: add your email to the header
//               .header("AIC-User-Agent","exhibition-curator (*Insert Email*)")
                .GET()
                .build();

        try{
            HttpResponse<String> response = client.send(chiSearchRequest,HttpResponse.BodyHandlers.ofString());
            return mapper.readTree(response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new ErrorSendingGETRequestException("Server Error: Unable To Read External GET Response");
        }

    }



    private ChicagoSearchResults getChiAPISearchResults(String query, Integer page){
        ArrayList<ArtworkDTO> artworkResults = new ArrayList<>();
        ChicagoSearchResults chicagoSearchResults = new ChicagoSearchResults(artworkResults,0);
        query = query.replace(" ","%20");
        String searchUrl = CHICAGO_ARTWORK_SEARCH_URL + "search?q=" + query.trim() +"&page=" + page +"&limit=10";

        JsonNode chiSearchRoot = sendGetRequest(searchUrl);

        int total_pages = chiSearchRoot.path("pagination").path("total_pages").asInt();
        total_pages = (Math.max(total_pages, 1));
        if(page > total_pages){
            throw new APIPageOutOfBoundsException("Chicago Institute: Page Invalid");
        }

        // Check to see if total is more than 0, if not then don't check the data
        int total = chiSearchRoot.path("pagination").path("total").asInt();

        if(total <= 0) {
            return chicagoSearchResults;
        }
            JsonNode chiArtResults = chiSearchRoot.findPath("data");
            for(JsonNode node : chiArtResults){
                // skip the node if it doesn't have id
                if(!node.has("id")){
                    continue;
                }

                String artId = node.path("id").asText();

                try{
                    ArtworkDTO art = getChiArtById(artId);
                    if(art.getId() != 0 && art.getArtist().getApiID() != 0){
                        artworkResults.add(art);
                    }
                }catch (InvalidArtworkException e){log.debug("Invalid artwork skipped: {}", e.getMessage());}

            }

            chicagoSearchResults.setTotal_pages(total_pages);

        return chicagoSearchResults;
    }




    private ArrayList<ArtworkDTO> getMetSearchResults(String query, Integer page){
        ArrayList<ArtworkDTO> artworks = new ArrayList<>();
        query = query.replace(" ", "%20");

        String searchUrl = "https://collectionapi.metmuseum.org/public/collection/v1/search?q="+query+"&hasImages=true";

        JsonNode results = sendGetRequest(searchUrl);

        if(results.path("total").asInt() <= 0){
            return artworks;
        }


        // we only want 10 results
        int start = (page - 1) * PAGE_SIZE;
        int end = start + 9;

        JsonNode dataIdList = results.get("objectIDs");

        if(dataIdList == null){
            return artworks;
        }

        if(start >= dataIdList.size()){
            throw new APIPageOutOfBoundsException("Out of bounds");
        }


        for(int i = start; i <= end && i < dataIdList.size(); i++){
            int id  = dataIdList.get(i).asInt();

            String objectUrl = "https://collectionapi.metmuseum.org/public/collection/v1/objects/" + id;
            JsonNode result = sendGetRequest(objectUrl);

            ArtistDTO artist;
            ArtworkDTO artwork;


            if(result.has("message")){
                continue;
            }

            boolean hasValidConstituents = result.has("constituents") &&
                    !result.get("constituents").isNull() &&
                    result.get("constituents").isArray() &&
                    !result.get("constituents").isEmpty();

            if (hasValidConstituents) {
                JsonNode constituent = result.get("constituents").get(0);
                long artistApiID = constituent.path("constituentID").asLong(1L);

                    artist = ArtistDTO.builder()
                            .name(constituent.path("name").asText("Unknown Artist"))
                            .apiID(artistApiID)
                            .build();
                }else {
                    artist = ArtistDTO.builder()
                            .name("Unknown Artist")
                            .apiID(1L)
                            .build();
            }

                String imageUrl = result.path("primaryImage").asText("");
                if (imageUrl.isEmpty()) {
                    continue;
                }


                 artwork = ArtworkDTO.builder()
                        .id(result.path("objectID").asLong())
                        .title(result.path("title").asText("Untitled Art"))
                        .description("No Description Provided From \"The Metropolitan Museum\"")
                        .imageUrl(imageUrl)
                        .apiOrigin("The MET")
                        .altText("Artwork from the MET: " + result.path("title").asText("Untitled Art"))
                        .artist(artist)
                        .build();

                artworks.add(artwork);

            }


        return artworks;
    }

    private ArtworkDTO getChiArtById(String id){
        String url = CHICAGO_ARTWORK_SEARCH_URL + id;
        JsonNode rootNode = sendGetRequest(url);
        if(rootNode.findPath("status").asText().equals("404")){
            throw new InvalidArtworkException(
                    String.format("There are no artworks with id: %s and apiOrigin: \"Chicago Institute\"",id));
        }

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

    private ArtworkDTO getMetArtById(String id){
        String url = "https://collectionapi.metmuseum.org/public/collection/v1/objects/" + id;
        JsonNode result = sendGetRequest(url);
        ArtworkDTO artwork;
        ArtistDTO artist;

        if(result.has("message")){
            throw new InvalidArtworkException("Invalid id: " + id + " for Met Museum API");
        }


        if (result.has("constituents") && result.get("constituents").isArray() && !result.get("constituents").isEmpty()) {
            JsonNode constituent = result.get("constituents").get(0);
            artist = ArtistDTO.builder()
                    .name(constituent.path("name").asText("Unknown Artist"))
                    .apiID(constituent.path("constituentID").asLong(1L))
                    .build();
        } else {
            artist = ArtistDTO.builder()
                    .name("Unknown Artist")
                    .apiID(1L)
                    .build();
        }

        String imageUrl = result.path("primaryImage").asText("");

        artwork = ArtworkDTO.builder()
                .id(result.path("objectID").asLong(0L))
                .title(result.path("title").asText("Untitled Art"))
                .description("No Description Provided From The Metropolitan Museum")
                .imageUrl(imageUrl)
                .apiOrigin("The MET")
                .altText("Artwork from the MET: " + result.path("title").asText("Untitled Art"))
                .artist(artist)
                .build();

            return artwork;
    }

}
