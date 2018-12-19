//******************************************************************************
//                                 EventDTO.java
// SILEX-PHIS
// Copyright © INRA 2018
// Creation date: 13 nov. 2018
// Contact: andreas.garcia@inra.fr, anne.tireau@inra.fr, pascal.neveu@inra.fr
//******************************************************************************
package phis2ws.service.resources.dto.event;

import java.util.ArrayList;
import java.util.HashMap;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import phis2ws.service.configuration.DateFormat;
import phis2ws.service.resources.dto.manager.AbstractVerifiedClass;
import phis2ws.service.resources.dto.rdfResourceDefinition.PropertyDTO;
import phis2ws.service.utils.dates.Dates;
import phis2ws.service.view.model.phis.Event;
import phis2ws.service.view.model.phis.Property;

/**
 * DTO representing an event
 * 
 * @author Andréas Garcia<andreas.garcia@inra.fr>
 */
public class EventDTO extends AbstractVerifiedClass {
    
    private final String uri;
    private final String type;
    private final ArrayList<HashMap<String, ArrayList<String>>> concernsList;
    private final String dateTimeString;
    protected ArrayList<PropertyDTO> properties = new ArrayList<>();
    
    /**
     * Constructor to create a DTO from an Event model
     * @param event 
     */
    public EventDTO(Event event) {
        this.uri = event.getUri();
        this.type = event.getType();
        this.concernsList = event.getConcernsList();
        
        DateTime eventDateTime = event.getDateTime();
        if(eventDateTime != null){
            this.dateTimeString = DateTimeFormat
                    .forPattern(DateFormat.YMDTHMSZZ.toString())
                    .print(eventDateTime);
        }
        else{
            this.dateTimeString = null;
        }
        event.getProperties().forEach((property) -> {
            properties.add(new PropertyDTO(property));
        });
    }

    /**
     * Generates an event model from de DTO
     * @return the Event model
     */
    @Override
    public Event createObjectFromDTO() {
        
        ArrayList<Property> eventProperties = new ArrayList<>();
        this.properties.forEach((property) -> {
            eventProperties.add(property.createObjectFromDTO());
        });
        
        return new Event(
                this.uri
                , this.type
                , this.concernsList
                , Dates.stringToDateTimeWithGivenPattern(
                    this.dateTimeString
                    , DateFormat.YMDTHMSZ.toString())
                , eventProperties);
    }
}
